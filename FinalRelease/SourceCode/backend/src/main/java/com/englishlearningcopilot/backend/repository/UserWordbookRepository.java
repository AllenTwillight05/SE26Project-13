package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.UserWordbook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserWordbookRepository extends JpaRepository<UserWordbook, Long> {

    Optional<UserWordbook> findByUserIdAndVocabularyId(Long userId, Long vocabularyId);

    @Modifying
    @Query(value = """
            INSERT INTO user_wordbook (user_id, vocabulary_id, favorited)
            VALUES (:userId, :vocabularyId, FALSE)
            ON DUPLICATE KEY UPDATE user_id = user_id
            """, nativeQuery = true)
    int insertIfAbsent(
            @Param("userId") Long userId,
            @Param("vocabularyId") Long vocabularyId
    );

    List<UserWordbook> findByUserIdOrderByIdDesc(Long userId);

    @Query("""
            SELECT wordbook.vocabularyId AS vocabularyId,
                   COUNT(DISTINCT wordbook.userId) AS learnerCount
            FROM UserWordbook wordbook
            GROUP BY wordbook.vocabularyId
            ORDER BY COUNT(DISTINCT wordbook.userId) DESC, wordbook.vocabularyId ASC
            """)
    List<VocabularyLeaderboardProjection> findVocabularyLeaderboard(Pageable pageable);
}
