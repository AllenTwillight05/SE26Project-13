package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.service.LearningPlanService;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(properties = "debug=false")
class LearningPlanInitializationIntegrationTest {

    private static final String USERNAME = "concurrent-plan-learner";

    @Autowired
    private LearningPlanService learningPlanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLearningPlanRepository userLearningPlanRepository;

    @Autowired
    private UserDailyLearningProgressRepository userDailyLearningProgressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanUp() {
        userRepository.findByUsername(USERNAME).ifPresent(user -> {
            userDailyLearningProgressRepository.deleteAll(
                    userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(user.getId())
            );
            userDailyLearningProgressRepository.findByUserIdAndPlanDate(user.getId(), LocalDate.now())
                    .ifPresent(userDailyLearningProgressRepository::delete);
            userLearningPlanRepository.findByUserId(user.getId()).ifPresent(userLearningPlanRepository::delete);
            userRepository.delete(user);
        });
    }

    @BeforeEach
    void clearPreviousTestData() {
        cleanUp();
    }

    @Test
    void concurrentFirstPlanRequestsCreateExactlyOnePlanWithoutErrors() throws Exception {
        AppUser user = createUser();

        runConcurrently(() -> learningPlanService.getLearningPlan(USERNAME));

        assertThat(userLearningPlanRepository.findByUserId(user.getId())).isPresent();
        assertThat(userLearningPlanRepository.countByUserId(user.getId())).isEqualTo(1);
    }

    @Test
    void concurrentFirstDailyStatusRequestsCreateExactlyOneProgressWithoutErrors() throws Exception {
        AppUser user = createUser();
        learningPlanService.getLearningPlan(USERNAME);

        runConcurrently(() -> learningPlanService.getDailyStatus(USERNAME));

        assertThat(userDailyLearningProgressRepository.findByUserIdAndPlanDate(user.getId(), LocalDate.now()))
                .isPresent();
        assertThat(userDailyLearningProgressRepository.countByUserIdAndPlanDate(user.getId(), LocalDate.now()))
                .isEqualTo(1);
    }

    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setUsername(USERNAME);
        user.setEmail(USERNAME + "@example.com");
        user.setDisplayName("Concurrent learner");
        user.setPasswordHash(passwordEncoder.encode("Password123"));
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private void runConcurrently(ThrowingRunnable action) throws Exception {
        int requestCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(requestCount);
        CountDownLatch ready = new CountDownLatch(requestCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Void>> futures = new ArrayList<>();

        try {
            for (int index = 0; index < requestCount; index++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    if (!start.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Concurrent test did not start in time.");
                    }
                    action.run();
                    return null;
                }));
            }

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();
            for (Future<Void> future : futures) {
                future.get(Duration.ofSeconds(10).toMillis(), TimeUnit.MILLISECONDS);
            }
        } finally {
            executor.shutdownNow();
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }
}
