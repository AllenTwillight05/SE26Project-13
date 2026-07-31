package com.englishlearningcopilot.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.service.dispatch.SpeakingTurnTaskWorker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(properties = {
        "debug=false",
        "speaking.dispatch.mode=queued",
        "speaking.dispatch.poll-interval-ms=60000",
        "xfyun.asr.enabled=false",
        "app.speaking.upload-dir=target/queued-speaking-test-uploads"
})
@AutoConfigureMockMvc
class QueuedSpeakingDispatchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpeakingTurnTaskWorker worker;

    @Autowired
    private SpeakingTurnTaskRepository taskRepository;

    @Autowired
    private SpeakingMessageRepository messageRepository;

    @Autowired
    private SpeakingSessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        clearPracticeData();
    }

    @AfterEach
    void tearDown() {
        clearPracticeData();
    }

    @Test
    void acceptedTaskIsIdempotentThenBecomesACompletedTurn() throws Exception {
        String token = registerAndExtractToken();
        Long sessionId = createSession(token);
        MockMultipartFile audio = new MockMultipartFile(
                "audio", "recording.webm", "audio/webm", new byte[] {0x1a, 0x45, 0x1f}
        );

        MvcResult accepted = mockMvc.perform(multipart("/api/speaking/sessions/" + sessionId + "/messages")
                        .file(audio)
                        .param("attemptId", "stable-browser-attempt")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.attemptId").value("stable-browser-attempt"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();
        Long taskId = objectMapper.readTree(accepted.getResponse().getContentAsString()).get("taskId").asLong();

        mockMvc.perform(multipart("/api/speaking/sessions/" + sessionId + "/messages")
                        .file(new MockMultipartFile("audio", "retry.webm", "audio/webm", new byte[] {9, 9, 9}))
                        .param("attemptId", "stable-browser-attempt")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.taskId").value(taskId));

        worker.processReadyTasks();

        mockMvc.perform(get("/api/speaking/sessions/" + sessionId + "/turn-tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REPLY_READY"))
                .andExpect(jsonPath("$.turn.userMessage.sender").value("USER"))
                .andExpect(jsonPath("$.turn.agentMessage.sender").value("AGENT"))
                .andExpect(jsonPath("$.turn.session.currentTurn").value(1));
    }

    private String registerAndExtractToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"queued-speaker","email":"queued@example.com","password":"Password123","displayName":"Queued"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private Long createSession(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/speaking/sessions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + "\"scenarioId\":\"business-opening\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    private void clearPracticeData() {
        taskRepository.deleteAll();
        messageRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
