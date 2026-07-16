package com.englishlearningcopilot.backend.service.impl;

import com.englishlearningcopilot.backend.dto.DashboardCommunityLearningTrendsResponse;
import com.englishlearningcopilot.backend.dto.DashboardGrammarTrendResponse;
import com.englishlearningcopilot.backend.dto.VocabularyLeaderboardItemResponse;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.repository.GrammarLeaderboardProjection;
import com.englishlearningcopilot.backend.repository.UserGrammarbookRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
import com.englishlearningcopilot.backend.repository.VocabularyLeaderboardProjection;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.englishlearningcopilot.backend.service.DashboardService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int VOCABULARY_LEADERBOARD_LIMIT = 10;
    private static final int GRAMMAR_LEADERBOARD_LIMIT = 10;

    private final UserGrammarbookRepository userGrammarbookRepository;
    private final UserWordbookRepository userWordbookRepository;
    private final VocabularyRepository vocabularyRepository;

    public DashboardServiceImpl(
            UserGrammarbookRepository userGrammarbookRepository,
            UserWordbookRepository userWordbookRepository,
            VocabularyRepository vocabularyRepository
    ) {
        this.userGrammarbookRepository = userGrammarbookRepository;
        this.userWordbookRepository = userWordbookRepository;
        this.vocabularyRepository = vocabularyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardCommunityLearningTrendsResponse getCommunityLearningTrends() {
        return new DashboardCommunityLearningTrendsResponse(
                Collections.emptyList(),
                getVocabularyLeaderboard(),
                getGrammarLeaderboard()
        );
    }

    private List<VocabularyLeaderboardItemResponse> getVocabularyLeaderboard() {
        List<VocabularyLeaderboardProjection> leaderboardRows = userWordbookRepository.findVocabularyLeaderboard(
                PageRequest.of(0, VOCABULARY_LEADERBOARD_LIMIT)
        );
        List<Long> vocabularyIds = leaderboardRows.stream()
                .map(VocabularyLeaderboardProjection::getVocabularyId)
                .toList();
        Map<Long, Vocabulary> vocabularyById = vocabularyRepository.findAllById(vocabularyIds).stream()
                .collect(Collectors.toMap(Vocabulary::getId, Function.identity()));

        List<VocabularyLeaderboardItemResponse> leaderboard = new ArrayList<>();
        for (VocabularyLeaderboardProjection row : leaderboardRows) {
            Vocabulary vocabulary = vocabularyById.get(row.getVocabularyId());

            if (vocabulary == null) {
                continue;
            }

            leaderboard.add(new VocabularyLeaderboardItemResponse(
                    leaderboard.size() + 1,
                    vocabulary.getId(),
                    vocabulary.getWord(),
                    nullToEmpty(vocabulary.getBriefTranslation()),
                    row.getLearnerCount()
            ));
        }

        return leaderboard;
    }

    private List<DashboardGrammarTrendResponse> getGrammarLeaderboard() {
        List<GrammarLeaderboardProjection> leaderboardRows = userGrammarbookRepository.findGrammarLeaderboard(
                PageRequest.of(0, GRAMMAR_LEADERBOARD_LIMIT)
        );

        List<DashboardGrammarTrendResponse> leaderboard = new ArrayList<>();
        for (GrammarLeaderboardProjection row : leaderboardRows) {
            leaderboard.add(new DashboardGrammarTrendResponse(
                    leaderboard.size() + 1,
                    row.getGrammarCategory(),
                    row.getLearnerCount()
            ));
        }

        return leaderboard;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
