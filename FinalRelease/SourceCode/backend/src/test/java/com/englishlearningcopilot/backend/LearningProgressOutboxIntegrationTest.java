package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.repository.LearningProgressOutboxEventRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.service.LearningProgressOutboxProcessor;
import com.englishlearningcopilot.backend.service.LearningProgressOutboxService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "debug=false")
class LearningProgressOutboxIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LearningProgressOutboxEventRepository eventRepository;

    @Autowired
    private UserDailyPracticeLogRepository practiceLogRepository;

    @Autowired
    private UserDailyLearningProgressRepository progressRepository;

    @Autowired
    private LearningProgressOutboxService outboxService;

    @Autowired
    private LearningProgressOutboxProcessor processor;

    @Test
    void durableEventIsProcessedOnceWhenTheSamePracticeIsQueuedAgain() {
        AppUser user = userRepository.save(user("outbox-integration-user"));

        outboxService.enqueueVocabularyPractice(user.getId(), 42L);
        outboxService.enqueueVocabularyPractice(user.getId(), 42L);

        assertThat(eventRepository.findAll())
                .filteredOn(event -> event.getUserId().equals(user.getId()))
                .hasSize(1)
                .allSatisfy(event -> assertThat(event.getStatus())
                        .isEqualTo(LearningProgressOutboxEvent.STATUS_PENDING));
        assertThat(practiceLogRepository.countByUserIdAndPlanDateAndPracticeTypeAndItemId(
                user.getId(), LocalDate.now(), "VOCABULARY", "42"
        )).isZero();

        processor.processReadyEvents();
        processor.processReadyEvents();

        assertThat(practiceLogRepository.countByUserIdAndPlanDateAndPracticeTypeAndItemId(
                user.getId(), LocalDate.now(), "VOCABULARY", "42"
        )).isEqualTo(1);
        assertThat(progressRepository.findByUserIdAndPlanDate(user.getId(), LocalDate.now()))
                .get()
                .extracting(progress -> progress.getVocabularyCompleted())
                .isEqualTo(1);
        assertThat(eventRepository.findAll())
                .filteredOn(event -> event.getUserId().equals(user.getId()))
                .allSatisfy(event -> {
                    assertThat(event.getStatus()).isEqualTo(LearningProgressOutboxEvent.STATUS_COMPLETED);
                    assertThat(event.getProcessedAt()).isNotNull();
                });
    }

    private static AppUser user(String username) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed-password");
        user.setDisplayName("Outbox integration user");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }
}
