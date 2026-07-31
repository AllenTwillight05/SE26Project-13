package com.englishlearningcopilot.backend.service.dispatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.entity.SpeakingTurnTask;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTaskStatus;
import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SpeakingTurnTaskServiceTest {

    @Mock
    private SpeakingTurnTaskRepository taskRepository;

    @Test
    void atomicallyRegistersThenReturnsTheTaskForTheSameAttempt() {
        SpeakingTurnTask task = task(9L, "attempt-1");
        when(taskRepository.findBySessionIdAndAttemptId(9L, "attempt-1")).thenReturn(Optional.of(task));
        SpeakingTurnTaskService service = new SpeakingTurnTaskService(taskRepository);

        SpeakingTurnTask result = service.createPendingTask(9L, 12L, " attempt-1 ");

        assertThat(result).isSameAs(task);
        verify(taskRepository).insertIfAbsent(9L, 12L, "attempt-1");
        verify(taskRepository).findBySessionIdAndAttemptId(9L, "attempt-1");
    }

    @Test
    void rejectsInvalidAttemptBeforeTouchingTheRepository() {
        SpeakingTurnTaskService service = new SpeakingTurnTaskService(taskRepository);

        assertThatThrownBy(() -> service.createPendingTask(9L, 12L, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("attemptId");

        verify(taskRepository, never()).insertIfAbsent(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    private static SpeakingTurnTask task(Long sessionId, String attemptId) {
        SpeakingTurnTask task = new SpeakingTurnTask();
        ReflectionTestUtils.setField(task, "id", 100L);
        task.setSessionId(sessionId);
        task.setAttemptId(attemptId);
        task.setStatus(SpeakingTurnTaskStatus.PENDING);
        return task;
    }
}
