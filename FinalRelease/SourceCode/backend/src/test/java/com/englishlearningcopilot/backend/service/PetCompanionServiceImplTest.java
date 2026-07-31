package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.PetChatRequest;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.englishlearningcopilot.backend.service.impl.PetCompanionServiceImpl;
import com.englishlearningcopilot.backend.service.impl.PetCompanionServiceImpl.PetVectorEntry;
import com.englishlearningcopilot.backend.service.impl.PetCompanionServiceImpl.PetVectorIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PetCompanionServiceImplTest {

    @Mock
    private SpeakingScenarioRepository speakingScenarioRepository;

    @Mock
    private VocabularyRepository vocabularyRepository;

    @TempDir
    private Path tempDir;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void rebuildIndexBuildsSpeakingAndVocabularyVectorsFromDatabase() throws Exception {
        Path indexPath = tempDir.resolve("pet-index.json");
        when(speakingScenarioRepository.findByActiveTrueOrderByTitleAsc())
                .thenReturn(List.of(scenario("G-05-clinic", "Clinic", "symptom,prescription")));
        when(vocabularyRepository.findAll()).thenReturn(List.of(
                vocabulary("starter", "apple", "zk"),
                vocabulary("business", "商务", "cet6"),
                vocabulary("academic", "学术", "ielts"),
                vocabulary("ignored", "ignored", "unknown"),
                vocabulary("missing", "missing", null)
        ));

        PetCompanionServiceImpl service = service(indexPath);
        PetVectorIndex index = service.rebuildIndex();

        assertThat(index.version()).isEqualTo("pet-vector-index-v3");
        assertThat(index.speaking()).extracting(PetVectorEntry::key).containsExactly("G-05-clinic");
        assertThat(index.vocabulary()).extracting(PetVectorEntry::key)
                .containsExactly("starter", "basic", "intermediate", "advanced");
        assertThat(index.idf()).isNotEmpty();
        assertThat(Files.exists(indexPath)).isTrue();
    }

    @Test
    void chatCreatesMissingIndexAndMatchesClinicNeedWithAdvancedVocabulary() {
        Path indexPath = tempDir.resolve("missing-index.json");
        when(speakingScenarioRepository.findByActiveTrueOrderByTitleAsc())
                .thenReturn(List.of(
                        scenario("G-05-clinic", "Clinic", "symptom,prescription"),
                        scenario("G-10-phone-call", "Phone Call", "calling about,confirm,message")
                ));
        when(vocabularyRepository.findAll()).thenReturn(List.of(
                vocabulary("medicine", "药", "cet4"),
                vocabulary("professional", "专业词汇", "ielts")
        ));

        var response = service(indexPath).chat(new PetChatRequest("我生病了，但是英语很好，需要专业词汇"));

        assertThat(response.speaking()).isNotNull();
        assertThat(response.speaking().scenarioId()).isEqualTo("G-05-clinic");
        assertThat(response.vocabulary().level()).isEqualTo("advanced");
        assertThat(response.retrievedContexts()).anyMatch(context -> context.contains("Top1"));
    }

    @Test
    void chatReadsCurrentIndexWithoutQueryingDatabaseAgain() throws Exception {
        Path indexPath = tempDir.resolve("current-index.json");
        objectMapper.writeValue(indexPath.toFile(), new PetVectorIndex(
                "pet-vector-index-v3",
                "now",
                indexPath.toString(),
                List.of(new PetVectorEntry("speaking", "G-10-phone-call", "Phone Call", "/speaking/G-10-phone-call",
                        "phone", Map.of("phone", 2.0), 2.0)),
                List.of(new PetVectorEntry("vocabulary", "intermediate", "中级", "/vocabulary/practice/intermediate",
                        "phone", Map.of("phone", 1.0), 1.0)),
                Map.of("phone", 1.0)
        ));

        var response = service(indexPath).chat(new PetChatRequest("phone"));

        assertThat(response.speaking().scenarioId()).isEqualTo("G-10-phone-call");
        assertThat(response.vocabulary().level()).isEqualTo("intermediate");
        verify(speakingScenarioRepository, never()).findByActiveTrueOrderByTitleAsc();
        verify(vocabularyRepository, never()).findAll();
    }

    @Test
    void chatRebuildsOutdatedIndexVersion() throws Exception {
        Path indexPath = tempDir.resolve("old-index.json");
        objectMapper.writeValue(indexPath.toFile(), new PetVectorIndex(
                "pet-vector-index-v1",
                "old",
                indexPath.toString(),
                List.of(),
                List.of(),
                Map.of()
        ));
        when(speakingScenarioRepository.findByActiveTrueOrderByTitleAsc())
                .thenReturn(List.of(scenario("G-10-phone-call", "Phone Call", "phone,confirm")));
        when(vocabularyRepository.findAll()).thenReturn(List.of(vocabulary("meeting", "会议", "cet6")));

        var response = service(indexPath).chat(new PetChatRequest("phone confirm"));

        assertThat(response.speaking().scenarioId()).isEqualTo("G-10-phone-call");
        assertThat(objectMapper.readValue(indexPath.toFile(), PetVectorIndex.class).version())
                .isEqualTo("pet-vector-index-v3");
    }

    @Test
    void chatRebuildsCorruptedIndexAndFallsBackWhenQueryHasNoMatch() throws Exception {
        Path indexPath = tempDir.resolve("broken-index.json");
        Files.writeString(indexPath, "{not json");
        when(speakingScenarioRepository.findByActiveTrueOrderByTitleAsc()).thenReturn(List.of());
        when(vocabularyRepository.findAll()).thenReturn(List.of());

        var response = service(indexPath).chat(new PetChatRequest("x"));

        assertThat(response.speaking()).isNull();
        assertThat(response.vocabulary().level()).isEqualTo("advanced");
        assertThat(response.vocabulary().route()).isEqualTo("/vocabulary/practice/advanced");
    }

    @Test
    void chatFallsBackToLastVocabularyEntryWhenAdvancedIsMissing() throws Exception {
        Path indexPath = tempDir.resolve("no-advanced-index.json");
        objectMapper.writeValue(indexPath.toFile(), new PetVectorIndex(
                "pet-vector-index-v3",
                "now",
                indexPath.toString(),
                List.of(new PetVectorEntry("speaking", "blank", "Blank", "/speaking/blank", "",
                        Map.of(), 0)),
                List.of(new PetVectorEntry("vocabulary", "basic", "基础", "/vocabulary/practice/basic",
                        "", Map.of(), 0)),
                Map.of()
        ));

        var response = service(indexPath).chat(new PetChatRequest(""));

        assertThat(response.speaking()).isNull();
        assertThat(response.vocabulary().level()).isEqualTo("basic");
    }

    @Test
    void chatReusesCachedIndexAfterFirstBuild() {
        Path indexPath = tempDir.resolve("cached-index.json");
        when(speakingScenarioRepository.findByActiveTrueOrderByTitleAsc())
                .thenReturn(List.of(scenario("G-10-phone-call", "Phone Call", "phone,confirm")));
        when(vocabularyRepository.findAll()).thenReturn(List.of(vocabulary("meeting", "浼氳", "cet6")));
        PetCompanionServiceImpl service = service(indexPath);

        service.chat(new PetChatRequest("phone"));
        service.chat(new PetChatRequest("phone"));

        verify(speakingScenarioRepository, times(1)).findByActiveTrueOrderByTitleAsc();
        verify(vocabularyRepository, times(1)).findAll();
    }

    @Test
    void constructorRejectsUnreadableAliasResource() throws Exception {
        ObjectMapper failingMapper = org.mockito.Mockito.mock(ObjectMapper.class);
        when(failingMapper.readValue(
                any(InputStream.class),
                org.mockito.ArgumentMatchers.<TypeReference<Map<String, String>>>any()
        ))
                .thenThrow(new IOException("broken aliases"));

        assertThatThrownBy(() -> new PetCompanionServiceImpl(
                speakingScenarioRepository,
                vocabularyRepository,
                failingMapper,
                tempDir.resolve("index.json").toString()
        )).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to read pet speaking aliases.");
    }

    @Test
    void helperMethodsHandleBlankStopWordsAndPreviewBounds() {
        PetCompanionServiceImpl service = service(tempDir.resolve("helpers-index.json"));

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "repeat", "", 1)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "repeat", "word", 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "repeat", "word", 2))
                .isEqualTo("word word ");

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "preview", " short   text ")).isEqualTo("short text");
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "preview", "x".repeat(501))).hasSize(500);

        List<String> tokens = new ArrayList<>();
        ReflectionTestUtils.invokeMethod(service, "addToken", tokens, "");
        ReflectionTestUtils.invokeMethod(service, "addToken", tokens, "the");
        ReflectionTestUtils.invokeMethod(service, "addToken", tokens, "useful");
        assertThat(tokens).containsExactly("useful");
    }

    private PetCompanionServiceImpl service(Path indexPath) {
        return new PetCompanionServiceImpl(
                speakingScenarioRepository,
                vocabularyRepository,
                objectMapper,
                indexPath.toString()
        );
    }

    private static SpeakingScenario scenario(String id, String title, String keywords) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId(id);
        scenario.setTitle(title);
        scenario.setDescription(title + " description");
        scenario.setDifficulty("B1");
        scenario.setAccent("US");
        scenario.setDuration("12 min");
        scenario.setSummary(title + " summary");
        scenario.setTone("friendly");
        scenario.setGoal(title + " goal");
        scenario.setKeywords(keywords);
        scenario.setRolePrompt("role");
        scenario.setOpeningMessage("hello");
        scenario.setSampleDialogue("Coach: hello. Learner: hello.");
        scenario.setTargetTurns(5);
        scenario.setScoringRubric("rubric");
        return scenario;
    }

    private static Vocabulary vocabulary(String word, String translation, String tag) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setWord(word);
        vocabulary.setTranslation(translation);
        vocabulary.setBriefTranslation(translation);
        vocabulary.setDefinition(word + " definition");
        vocabulary.setTag(tag);
        return vocabulary;
    }
}
