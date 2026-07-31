package com.englishlearningcopilot.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.SpeakingTurnTaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "debug=false")
@Transactional
class SpeakingTurnTaskRepositoryTest {

    @Autowired
    private SpeakingTurnTaskRepository taskRepository;

    @Test
    void insertIfAbsentKeepsOneTaskForRepeatedSessionAttempt() {
        taskRepository.insertIfAbsent(500L, 700L, "retry-safe-attempt");
        taskRepository.insertIfAbsent(500L, 700L, "retry-safe-attempt");

        assertThat(taskRepository.findBySessionIdAndAttemptId(500L, "retry-safe-attempt"))
                .isPresent()
                .get()
                .satisfies(task -> {
                    assertThat(task.getUserMessageId()).isEqualTo(700L);
                    assertThat(task.getStatus()).isEqualTo(SpeakingTurnTaskStatus.PENDING);
                    assertThat(task.getAttemptCount()).isZero();
                });
    }
}
