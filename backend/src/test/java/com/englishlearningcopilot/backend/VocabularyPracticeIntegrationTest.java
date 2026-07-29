package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import com.englishlearningcopilot.backend.repository.LearningProgressOutboxEventRepository;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(properties = "debug=false")
@AutoConfigureMockMvc
class VocabularyPracticeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LearningProgressOutboxEventRepository learningProgressOutboxEventRepository;

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private UserWordbookRepository userWordbookRepository;

    @Autowired
    private UserWordProgressRepository userWordProgressRepository;

    @Autowired
    private UserDailyPracticeLogRepository userDailyPracticeLogRepository;

    @Autowired
    private UserDailyLearningProgressRepository userDailyLearningProgressRepository;

    @Autowired
    private UserLearningPlanRepository userLearningPlanRepository;

    @Autowired
    private SpeakingMessageRepository speakingMessageRepository;

    @Autowired
    private SpeakingSessionRepository speakingSessionRepository;

    @BeforeEach
    void setUp() {
        speakingMessageRepository.deleteAll();
        speakingSessionRepository.deleteAll();
        learningProgressOutboxEventRepository.deleteAll();
        userDailyPracticeLogRepository.deleteAll();
        userDailyLearningProgressRepository.deleteAll();
        userLearningPlanRepository.deleteAll();
        userWordProgressRepository.deleteAll();
        userWordbookRepository.deleteAll();
        vocabularyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void vocabularyPracticeHttpFlowPersistsWordbookReviewProgressAndDailyProgress() throws Exception {
        Vocabulary vocabulary = saveVocabulary("airport", "zk");
        String token = registerAndExtractToken("vocab-user");

        mockMvc.perform(post("/api/profile/learning-plan")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dailyVocabularyGoal": 1,
                                  "dailyGrammarGoal": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyVocabularyGoal").value(1));

        mockMvc.perform(get("/api/vocabulary/practice-words?level=starter")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].word").value("airport"));

        mockMvc.perform(post("/api/vocabulary/practice-ratings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "vocabularyId": %d,
                                  "score": 3
                                }
                                """.formatted(vocabulary.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vocabulary rating received."));

        AppUser user = userRepository.findByUsername("vocab-user").orElseThrow();
        assertThat(userWordbookRepository.findByUserIdAndVocabularyId(user.getId(), vocabulary.getId())).isPresent();
        assertThat(userWordProgressRepository.findByUserIdAndQuestionIdAndQuestionType(
                user.getId(),
                String.valueOf(vocabulary.getId()),
                "vocabulary"
        )).isPresent();
        assertThat(learningProgressOutboxEventRepository.findAll())
                .anySatisfy(event -> {
                    assertThat(event.getUserId()).isEqualTo(user.getId());
                    assertThat(event.getPlanDate()).isEqualTo(LocalDate.now());
                    assertThat(event.getPracticeType()).isEqualTo("VOCABULARY");
                    assertThat(event.getItemId()).isEqualTo(String.valueOf(vocabulary.getId()));
                    assertThat(event.getStatus()).isEqualTo(LearningProgressOutboxEvent.STATUS_PENDING);
                });

        mockMvc.perform(get("/api/vocabulary/wordbook-words")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].word").value("airport"));
    }

    private Vocabulary saveVocabulary(String word, String tag) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setWord(word);
        vocabulary.setTag(tag);
        vocabulary.setBriefTranslation("机场");
        return vocabularyRepository.save(vocabulary);
    }

    private String registerAndExtractToken(String username) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "email": "%s@example.com",
                                  "password": "Password123",
                                  "displayName": "%s"
                                }
                                """.formatted(username, username, username)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("token").asText();
    }
}
