package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.SpeakingSessionStatus;
import com.englishlearningcopilot.backend.entity.UserDailyPracticeLog;
import com.englishlearningcopilot.backend.repository.GrammarQuestionRepository;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserGrammarbookRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
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
class DashboardProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLearningPlanRepository userLearningPlanRepository;

    @Autowired
    private UserDailyPracticeLogRepository userDailyPracticeLogRepository;

    @Autowired
    private UserDailyLearningProgressRepository userDailyLearningProgressRepository;

    @Autowired
    private UserWordProgressRepository userWordProgressRepository;

    @Autowired
    private UserWordbookRepository userWordbookRepository;

    @Autowired
    private UserGrammarbookRepository userGrammarbookRepository;

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private GrammarQuestionRepository grammarQuestionRepository;

    @Autowired
    private SpeakingScenarioRepository speakingScenarioRepository;

    @Autowired
    private SpeakingSessionRepository speakingSessionRepository;

    @Autowired
    private SpeakingMessageRepository speakingMessageRepository;

    @BeforeEach
    void setUp() {
        speakingMessageRepository.deleteAll();
        speakingSessionRepository.deleteAll();
        speakingScenarioRepository.deleteAll();
        userDailyPracticeLogRepository.deleteAll();
        userDailyLearningProgressRepository.deleteAll();
        userLearningPlanRepository.deleteAll();
        userWordProgressRepository.deleteAll();
        userWordbookRepository.deleteAll();
        userGrammarbookRepository.deleteAll();
        vocabularyRepository.deleteAll();
        grammarQuestionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void dashboardAndProfileReflectDatabasePracticeAndLatestSpeakingFeedback() throws Exception {
        String token = registerAndExtractToken("profile-user");
        AppUser user = userRepository.findByUsername("profile-user").orElseThrow();
        SpeakingScenario scenario = saveScenario("daily-chat", "Daily Chat");
        SpeakingSession session = saveCompletedSession(user, scenario);
        saveUserMessage(session, "This is difficult.", 55, 60, 50, 65);
        saveUserMessage(session, "I can explain my plan.", 85, 88, 82, 86);
        savePracticeLog(user.getId(), "VOCABULARY", "101");
        savePracticeLog(user.getId(), "GRAMMAR", "201");

        mockMvc.perform(get("/api/dashboard/weekly-overview")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speakingDuration").value("1 min"))
                .andExpect(jsonPath("$.pronunciationReference").value("71 / 100"))
                .andExpect(jsonPath("$.vocabularyLearned").value("1 词"))
                .andExpect(jsonPath("$.grammarPracticed").value("1 题"));

        MvcResult snapshotResult = mockMvc.perform(get("/api/profile/snapshot")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.learnerName").value("profile-user"))
                .andExpect(jsonPath("$.feedback.scenarioTitle").value("Daily Chat"))
                .andExpect(jsonPath("$.feedback.totalScore").value(71))
                .andExpect(jsonPath("$.feedback.pronunciation").value(74))
                .andExpect(jsonPath("$.feedback.fluency").value(66))
                .andExpect(jsonPath("$.feedback.integrity").value(76))
                .andExpect(jsonPath("$.feedback.issueSentence").value("This is difficult."))
                .andReturn();

        JsonNode snapshot = objectMapper.readTree(snapshotResult.getResponse().getContentAsString());
        assertThat(snapshot.get("dailyPlan").get("vocabulary").get("total").asInt()).isGreaterThanOrEqualTo(0);
    }

    private SpeakingScenario saveScenario(String id, String title) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId(id);
        scenario.setTitle(title);
        scenario.setDescription("Practice daily conversation.");
        scenario.setDifficulty("Beginner");
        scenario.setAccent("American");
        scenario.setDuration("5 min");
        scenario.setSummary("Daily practice");
        scenario.setTone("Friendly");
        scenario.setGoal("Speak clearly");
        scenario.setKeywords("daily, chat");
        scenario.setRolePrompt("You are a tutor.");
        scenario.setOpeningMessage("Hello.");
        scenario.setTargetTurns(2);
        scenario.setScoringRubric("Score content and pronunciation.");
        return speakingScenarioRepository.save(scenario);
    }

    private SpeakingSession saveCompletedSession(AppUser user, SpeakingScenario scenario) {
        SpeakingSession session = new SpeakingSession();
        session.setUser(user);
        session.setScenario(scenario);
        session.setStatus(SpeakingSessionStatus.COMPLETED);
        session.setStartedAt(Instant.now().minusSeconds(600));
        session.setCompletedAt(Instant.now());
        session.setTargetTurns(2);
        session.setCurrentTurn(2);
        return speakingSessionRepository.save(session);
    }

    private void saveUserMessage(
            SpeakingSession session,
            String content,
            double total,
            double accuracy,
            double fluency,
            double integrity
    ) {
        SpeakingMessage message = new SpeakingMessage();
        message.setSession(session);
        message.setSender(SpeakingMessageSender.USER);
        message.setContent(content);
        message.setTranscribedText(content);
        message.setDurationMs(30_000L);
        message.setPronunciationScore(total);
        message.setPronunciationDetail("""
                {"totalScore":%s,"accuracy":%s,"fluency":%s,"integrity":%s,"speed":0}
                """.formatted(total, accuracy, fluency, integrity));
        speakingMessageRepository.save(message);
    }

    private void savePracticeLog(Long userId, String type, String itemId) {
        UserDailyPracticeLog log = new UserDailyPracticeLog();
        log.setUserId(userId);
        log.setPlanDate(LocalDate.now());
        log.setPracticeType(type);
        log.setItemId(itemId);
        userDailyPracticeLogRepository.save(log);
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
