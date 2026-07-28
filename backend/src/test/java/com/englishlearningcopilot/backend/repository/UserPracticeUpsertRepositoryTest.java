package com.englishlearningcopilot.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "debug=false")
@Transactional
class UserPracticeUpsertRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserWordbookRepository userWordbookRepository;

    @Autowired
    private UserGrammarbookRepository userGrammarbookRepository;

    @Test
    void wordbookAndGrammarbookUpsertsAreIdempotent() {
        AppUser user = userRepository.save(user("practice-upsert"));

        userWordbookRepository.insertIfAbsent(user.getId(), 101L);
        userWordbookRepository.insertIfAbsent(user.getId(), 101L);
        userGrammarbookRepository.upsertPracticeResult(user.getId(), 202, false);
        userGrammarbookRepository.upsertPracticeResult(user.getId(), 202, true);

        assertThat(userWordbookRepository.findByUserIdAndVocabularyId(user.getId(), 101L))
                .isPresent()
                .get()
                .extracting(wordbook -> wordbook.isFavorited())
                .isEqualTo(false);
        assertThat(userGrammarbookRepository.findByUserIdAndGrammarQuestionId(user.getId(), 202))
                .isPresent()
                .get()
                .extracting(grammarbook -> grammarbook.isIncorrect())
                .isEqualTo(true);
    }

    private static AppUser user(String username) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed-password");
        user.setDisplayName("Practice Upsert");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }
}
