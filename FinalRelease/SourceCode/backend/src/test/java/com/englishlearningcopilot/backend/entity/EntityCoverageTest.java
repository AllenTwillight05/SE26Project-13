package com.englishlearningcopilot.backend.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class EntityCoverageTest {

    @Test
    void userDailyPracticeLogStoresFieldsAndSetsCreatedAtOnPersist() {
        UserDailyPracticeLog log = new UserDailyPracticeLog();
        log.setUserId(7L);
        log.setPlanDate(LocalDate.now());
        log.setPracticeType("VOCABULARY");
        log.setItemId("10");

        log.prePersist();

        assertThat(log.getUserId()).isEqualTo(7L);
        assertThat(log.getPlanDate()).isEqualTo(LocalDate.now());
        assertThat(log.getPracticeType()).isEqualTo("VOCABULARY");
        assertThat(log.getItemId()).isEqualTo("10");
        assertThat(log.getCreatedAt()).isNotNull();
    }

    @Test
    void userLearningPlanStoresGoalsAndUpdatesTimestamps() {
        UserLearningPlan plan = new UserLearningPlan();
        plan.setUserId(7L);
        plan.setDailyVocabularyGoal(30);
        plan.setDailyGrammarGoal(15);
        plan.setEnabled(false);

        plan.prePersist();
        Instant createdAt = plan.getCreatedAt();
        plan.preUpdate();

        assertThat(plan.getUserId()).isEqualTo(7L);
        assertThat(plan.getDailyVocabularyGoal()).isEqualTo(30);
        assertThat(plan.getDailyGrammarGoal()).isEqualTo(15);
        assertThat(plan.isEnabled()).isFalse();
        assertThat(createdAt).isNotNull();
        assertThat(plan.getUpdatedAt()).isNotNull();
    }

    @Test
    void userDailyLearningProgressStoresProgressAndLifecycleFields() {
        UserDailyLearningProgress progress = new UserDailyLearningProgress();
        progress.setUserId(7L);
        progress.setPlanDate(LocalDate.now());
        progress.setVocabularyCompleted(2);
        progress.setGrammarCompleted(3);
        progress.setVocabularyGoal(10);
        progress.setGrammarGoal(8);
        progress.setCompleted(true);
        Instant completedAt = Instant.now();
        progress.setCompletedAt(completedAt);

        progress.prePersist();
        progress.preUpdate();

        assertThat(progress.getUserId()).isEqualTo(7L);
        assertThat(progress.getVocabularyCompleted()).isEqualTo(2);
        assertThat(progress.getGrammarCompleted()).isEqualTo(3);
        assertThat(progress.getVocabularyGoal()).isEqualTo(10);
        assertThat(progress.getGrammarGoal()).isEqualTo(8);
        assertThat(progress.isCompleted()).isTrue();
        assertThat(progress.getCompletedAt()).isEqualTo(completedAt);
        assertThat(progress.getCreatedAt()).isNotNull();
        assertThat(progress.getUpdatedAt()).isNotNull();
    }

    @Test
    void userWordProgressDefaultsDueAndTracksReviewFields() {
        UserWordProgress progress = new UserWordProgress();
        progress.setId(99L);
        progress.setUserId(7L);
        progress.setQuestionId("10");
        progress.setQuestionType("vocabulary");
        progress.setDifficulty(3.0);
        progress.setStability(4.0);
        progress.setInterval(5);
        progress.setReps(6);
        progress.setLapses(1);
        progress.setState(2);
        Instant due = Instant.now().plusSeconds(60);
        Instant lastReview = Instant.now();
        progress.setDue(due);
        progress.setLastReview(lastReview);

        progress.prePersist();
        progress.preUpdate();

        assertThat(progress.getId()).isEqualTo(99L);
        assertThat(progress.getUserId()).isEqualTo(7L);
        assertThat(progress.getQuestionId()).isEqualTo("10");
        assertThat(progress.getQuestionType()).isEqualTo("vocabulary");
        assertThat(progress.getDifficulty()).isEqualTo(3.0);
        assertThat(progress.getStability()).isEqualTo(4.0);
        assertThat(progress.getInterval()).isEqualTo(5);
        assertThat(progress.getReps()).isEqualTo(6);
        assertThat(progress.getLapses()).isEqualTo(1);
        assertThat(progress.getState()).isEqualTo(2);
        assertThat(progress.getDue()).isEqualTo(due);
        assertThat(progress.getLastReview()).isEqualTo(lastReview);
        assertThat(progress.getCreatedAt()).isNotNull();
        assertThat(progress.getUpdatedAt()).isNotNull();
    }

    @Test
    void userWordProgressPrePersistDefaultsDueWhenMissing() {
        UserWordProgress progress = new UserWordProgress();
        progress.setUserId(7L);
        progress.setQuestionId("10");
        progress.setQuestionType("vocabulary");

        progress.prePersist();

        assertThat(progress.getDue()).isNotNull();
        assertThat(progress.getDifficulty()).isEqualTo(2.5);
        assertThat(progress.getStability()).isEqualTo(2.5);
        assertThat(progress.getInterval()).isZero();
        assertThat(progress.getReps()).isZero();
        assertThat(progress.getLapses()).isZero();
        assertThat(progress.getState()).isZero();
    }

    @Test
    void vocabularyStoresAllDictionaryFields() {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(10L);
        vocabulary.setWord("accept");
        vocabulary.setPhonetic("/accept/");
        vocabulary.setDefinition("definition");
        vocabulary.setTranslation("translation");
        vocabulary.setBriefTranslation("brief");
        vocabulary.setCollins("3");
        vocabulary.setOxford("1");
        vocabulary.setTag("cet4");
        vocabulary.setBnc("1000");
        vocabulary.setFrq("10");
        vocabulary.setExchange("accepted");
        vocabulary.setUkAudio("uk.mp3");
        vocabulary.setUsAudio("us.mp3");
        vocabulary.setChineseOptions(List.of("接受"));
        vocabulary.setEnglishOptions(List.of("accept"));

        assertThat(vocabulary.getId()).isEqualTo(10L);
        assertThat(vocabulary.getWord()).isEqualTo("accept");
        assertThat(vocabulary.getPhonetic()).isEqualTo("/accept/");
        assertThat(vocabulary.getDefinition()).isEqualTo("definition");
        assertThat(vocabulary.getTranslation()).isEqualTo("translation");
        assertThat(vocabulary.getBriefTranslation()).isEqualTo("brief");
        assertThat(vocabulary.getCollins()).isEqualTo("3");
        assertThat(vocabulary.getOxford()).isEqualTo("1");
        assertThat(vocabulary.getTag()).isEqualTo("cet4");
        assertThat(vocabulary.getBnc()).isEqualTo("1000");
        assertThat(vocabulary.getFrq()).isEqualTo("10");
        assertThat(vocabulary.getExchange()).isEqualTo("accepted");
        assertThat(vocabulary.getUkAudio()).isEqualTo("uk.mp3");
        assertThat(vocabulary.getUsAudio()).isEqualTo("us.mp3");
        assertThat(vocabulary.getChineseOptions()).containsExactly("接受");
        assertThat(vocabulary.getEnglishOptions()).containsExactly("accept");
    }

    @Test
    void lightweightEntitiesStoreMutableFields() {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(1);
        question.setOptionE("E");
        assertThat(question.getOptionE()).isEqualTo("E");

        UserGrammarbook grammarbook = new UserGrammarbook();
        grammarbook.setId(2L);
        grammarbook.setUserId(7L);
        grammarbook.setGrammarQuestionId(1);
        grammarbook.setIncorrect(true);
        grammarbook.setFavorited(true);
        assertThat(grammarbook.getId()).isEqualTo(2L);
        assertThat(grammarbook.getUserId()).isEqualTo(7L);
        assertThat(grammarbook.getGrammarQuestionId()).isEqualTo(1);
        assertThat(grammarbook.isIncorrect()).isTrue();
        assertThat(grammarbook.isFavorited()).isTrue();

        UserWordbook wordbook = new UserWordbook();
        wordbook.setId(3L);
        wordbook.setUserId(7L);
        wordbook.setVocabularyId(10L);
        wordbook.setFavorited(true);
        assertThat(wordbook.getId()).isEqualTo(3L);
        assertThat(wordbook.getUserId()).isEqualTo(7L);
        assertThat(wordbook.getVocabularyId()).isEqualTo(10L);
        assertThat(wordbook.isFavorited()).isTrue();

        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setActive(false);
        assertThat(scenario.isActive()).isFalse();
    }

    @Test
    void speakingScenarioStoresFieldsAndLifecycleTimestamps() {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId("daily-checkin");
        scenario.setTitle("Daily Check-in");
        scenario.setDescription("Practice a short check-in.");
        scenario.setDifficulty("Beginner");
        scenario.setAccent("American");
        scenario.setDuration("5 min");
        scenario.setSummary("Short daily conversation");
        scenario.setTone("Friendly");
        scenario.setGoal("Answer naturally");
        scenario.setKeywords("greeting, mood");
        scenario.setRolePrompt("You are a friendly tutor.");
        scenario.setOpeningMessage("How are you today?");
        scenario.setSampleDialogue("A: Hi\nB: Hello");
        scenario.setTargetTurns(4);
        scenario.setScoringRubric("Score by relevance.");

        scenario.prePersist();
        Instant createdAt = scenario.getCreatedAt();
        scenario.preUpdate();

        assertThat(scenario.getId()).isEqualTo("daily-checkin");
        assertThat(scenario.getTitle()).isEqualTo("Daily Check-in");
        assertThat(scenario.getDescription()).isEqualTo("Practice a short check-in.");
        assertThat(scenario.getDifficulty()).isEqualTo("Beginner");
        assertThat(scenario.getAccent()).isEqualTo("American");
        assertThat(scenario.getDuration()).isEqualTo("5 min");
        assertThat(scenario.getSummary()).isEqualTo("Short daily conversation");
        assertThat(scenario.getTone()).isEqualTo("Friendly");
        assertThat(scenario.getGoal()).isEqualTo("Answer naturally");
        assertThat(scenario.getKeywords()).isEqualTo("greeting, mood");
        assertThat(scenario.getRolePrompt()).isEqualTo("You are a friendly tutor.");
        assertThat(scenario.getOpeningMessage()).isEqualTo("How are you today?");
        assertThat(scenario.getSampleDialogue()).isEqualTo("A: Hi\nB: Hello");
        assertThat(scenario.getTargetTurns()).isEqualTo(4);
        assertThat(scenario.getScoringRubric()).isEqualTo("Score by relevance.");
        assertThat(createdAt).isNotNull();
        assertThat(scenario.getUpdatedAt()).isNotNull();
    }

    @Test
    void speakingSessionPrePersistDefaultsStartedAtWhenMissing() {
        SpeakingSession session = new SpeakingSession();
        session.setUser(new AppUser());
        session.setScenario(new SpeakingScenario());
        session.setTargetTurns(3);
        session.setCurrentTurn(1);
        session.setSelectedTopic("Travel");

        session.prePersist();
        session.preUpdate();

        assertThat(session.getStartedAt()).isNotNull();
        assertThat(session.getCreatedAt()).isNotNull();
        assertThat(session.getUpdatedAt()).isNotNull();
        assertThat(session.getStatus()).isEqualTo(SpeakingSessionStatus.ACTIVE);
        assertThat(session.getTargetTurns()).isEqualTo(3);
        assertThat(session.getCurrentTurn()).isEqualTo(1);
        assertThat(session.getSelectedTopic()).isEqualTo("Travel");
    }
}
