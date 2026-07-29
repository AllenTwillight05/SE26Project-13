package com.englishlearningcopilot.backend.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import java.util.List;
import org.junit.jupiter.api.Test;

class DtoCoverageTest {

    @Test
    void dueVocabularyCardMapsNullBriefTranslationAndOptionsToEmptyValues() {
        Vocabulary vocabulary = vocabulary();
        vocabulary.setBriefTranslation(null);
        vocabulary.setChineseOptions(null);
        vocabulary.setEnglishOptions(null);

        DueVocabularyCard card = DueVocabularyCard.from(vocabulary);

        assertThat(card.briefTranslation()).isEmpty();
        assertThat(card.chineseOptions()).isEmpty();
        assertThat(card.englishOptions()).isEmpty();
    }

    @Test
    void dueVocabularyCardMapsFullVocabularyFields() {
        Vocabulary vocabulary = vocabulary();
        vocabulary.setBriefTranslation("brief");
        vocabulary.setChineseOptions(List.of("释义"));
        vocabulary.setEnglishOptions(List.of("accept"));

        DueVocabularyCard card = DueVocabularyCard.from(vocabulary);

        assertThat(card.id()).isEqualTo(10L);
        assertThat(card.word()).isEqualTo("accept");
        assertThat(card.briefTranslation()).isEqualTo("brief");
        assertThat(card.chineseOptions()).containsExactly("释义");
        assertThat(card.englishOptions()).containsExactly("accept");
    }

    @Test
    void speakingScenarioResponseSplitsKeywordsAndIgnoresBlankItems() {
        SpeakingScenario scenario = scenario("travel, , airport");

        SpeakingScenarioResponse response = SpeakingScenarioResponse.from(scenario);

        assertThat(response.keywords()).containsExactly("travel", "airport");
        assertThat(response.level()).isEqualTo(response.difficulty());
    }

    @Test
    void speakingScenarioResponseUsesEmptyKeywordsForNullOrBlankText() {
        assertThat(SpeakingScenarioResponse.from(scenario(null)).keywords()).isEmpty();
        assertThat(SpeakingScenarioResponse.from(scenario(" ")).keywords()).isEmpty();
    }

    @Test
    void grammarPracticeQuestionResponseIncludesOptionalOptionEWhenPresent() {
        GrammarQuestion question = grammarQuestion();
        question.setOptionE("E");

        GrammarPracticeQuestionResponse response = GrammarPracticeQuestionResponse.from(question);

        assertThat(response.options()).containsExactly("A", "B", "C", "D", "E");
    }

    @Test
    void grammarNotebookQuestionResponseSkipsBlankOptionE() {
        GrammarQuestion question = grammarQuestion();
        question.setOptionE(" ");

        GrammarNotebookQuestionResponse response = GrammarNotebookQuestionResponse.from(question, true, false);

        assertThat(response.options()).containsExactly("A", "B", "C", "D");
        assertThat(response.wrong()).isTrue();
        assertThat(response.favorited()).isFalse();
    }

    @Test
    void recordOnlyDtosExposeValues() {
        assertThat(new GrammarProgressResponse(2, 5).completed()).isEqualTo(2);
        assertThat(new ProfileSnapshotResponse.FeedbackMetric("score", "Score", "90").value()).isEqualTo("90");
    }

    @Test
    void speakingMessageResponseMapsMessageFields() {
        SpeakingMessage message = new SpeakingMessage();
        message.setSender(SpeakingMessageSender.USER);
        message.setContent("hello");
        message.setSpokenText("hello");
        message.setTranscribedText("hello");
        message.setAudioUrl("/audio.webm");
        message.setDurationMs(1000L);

        SpeakingMessageResponse response = SpeakingMessageResponse.from(message);

        assertThat(response.sender()).isEqualTo("USER");
        assertThat(response.audioUrl()).isEqualTo("/audio.webm");
    }

    private static Vocabulary vocabulary() {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(10L);
        vocabulary.setWord("accept");
        vocabulary.setPhonetic("/accept/");
        vocabulary.setDefinition("definition");
        vocabulary.setTranslation("translation");
        vocabulary.setCollins("3");
        vocabulary.setOxford("1");
        vocabulary.setTag("cet4");
        vocabulary.setBnc("1000");
        vocabulary.setFrq("10");
        vocabulary.setExchange("accepted");
        vocabulary.setUkAudio("uk.mp3");
        vocabulary.setUsAudio("us.mp3");
        return vocabulary;
    }

    private static SpeakingScenario scenario(String keywords) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId("airport");
        scenario.setTitle("Airport");
        scenario.setDescription("desc");
        scenario.setDifficulty("B1");
        scenario.setAccent("US");
        scenario.setDuration("5 min");
        scenario.setSummary("summary");
        scenario.setTone("friendly");
        scenario.setGoal("goal");
        scenario.setKeywords(keywords);
        scenario.setOpeningMessage("hello");
        scenario.setSampleDialogue("dialogue");
        scenario.setTargetTurns(3);
        scenario.setScoringRubric("rubric");
        return scenario;
    }

    private static GrammarQuestion grammarQuestion() {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(1);
        question.setQuestionText("Choose.");
        question.setOptionA("A");
        question.setOptionB("B");
        question.setOptionC("C");
        question.setOptionD("D");
        question.setAnswer("A");
        question.setGrammarCategory("Tense");
        question.setExplanation("Because.");
        return question;
    }
}
