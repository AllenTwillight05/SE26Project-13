package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import com.englishlearningcopilot.backend.repository.GrammarQuestionRepository;
import com.englishlearningcopilot.backend.repository.LearningProgressOutboxEventRepository;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserGrammarbookRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
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
class GrammarPracticeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GrammarQuestionRepository grammarQuestionRepository;

    @Autowired
    private LearningProgressOutboxEventRepository learningProgressOutboxEventRepository;

    @Autowired
    private UserGrammarbookRepository userGrammarbookRepository;

    @Autowired
    private UserWordProgressRepository userWordProgressRepository;

    @Autowired
    private UserWordbookRepository userWordbookRepository;

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
        userGrammarbookRepository.deleteAll();
        grammarQuestionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void grammarPracticeHttpFlowPersistsNotebookReviewProgressAndDailyProgress() throws Exception {
        GrammarQuestion question = saveGrammarQuestion(9001, "Tense");
        String token = registerAndExtractToken("grammar-user");

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
                .andExpect(jsonPath("$.dailyGrammarGoal").value(1));

        mockMvc.perform(get("/api/grammar/practice-questions?category=Tense")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(question.getId()))
                .andExpect(jsonPath("$[0].grammar_category").value("Tense"));

        mockMvc.perform(post("/api/grammar/practice-results")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": %d,
                                  "incorrect": true
                                }
                                """.formatted(question.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grammar practice result received."));

        mockMvc.perform(post("/api/grammar/practice-ratings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": %d,
                                  "score": 3
                                }
                                """.formatted(question.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grammar rating received."));

        AppUser user = userRepository.findByUsername("grammar-user").orElseThrow();
        assertThat(userGrammarbookRepository.findByUserIdAndGrammarQuestionId(user.getId(), question.getId()))
                .isPresent()
                .get()
                .extracting(row -> row.isIncorrect())
                .isEqualTo(true);
        assertThat(userWordProgressRepository.findByUserIdAndQuestionIdAndQuestionType(
                user.getId(),
                String.valueOf(question.getId()),
                "grammar"
        )).isPresent();
        assertThat(learningProgressOutboxEventRepository.findAll())
                .anySatisfy(event -> {
                    assertThat(event.getUserId()).isEqualTo(user.getId());
                    assertThat(event.getPlanDate()).isEqualTo(LocalDate.now());
                    assertThat(event.getPracticeType()).isEqualTo("GRAMMAR");
                    assertThat(event.getItemId()).isEqualTo(String.valueOf(question.getId()));
                    assertThat(event.getStatus()).isEqualTo(LearningProgressOutboxEvent.STATUS_PENDING);
                });

        mockMvc.perform(get("/api/grammar/notebook-questions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(question.getId()))
                .andExpect(jsonPath("$[0].wrong").value(true));
    }

    private GrammarQuestion saveGrammarQuestion(Integer id, String category) {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(id);
        question.setQuestionText("I ____ English every day.");
        question.setOptionA("study");
        question.setOptionB("studies");
        question.setOptionC("studied");
        question.setOptionD("studying");
        question.setAnswer("A");
        question.setGrammarCategory(category);
        question.setExplanation("Use the base verb with I.");
        return grammarQuestionRepository.save(question);
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
