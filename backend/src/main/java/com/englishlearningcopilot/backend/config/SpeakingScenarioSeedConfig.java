package com.englishlearningcopilot.backend.config;

import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeakingScenarioSeedConfig {

    @Bean
    CommandLineRunner seedSpeakingScenarios(SpeakingScenarioRepository scenarioRepository) {
        return args -> {
            List<SpeakingScenario> scenarios = List.of(
                    scenario(
                            "business-opening",
                            "Business Meeting",
                            "Practice opening a meeting, clarifying agenda items, and confirming delivery timelines.",
                            "B2",
                            "American business English",
                            "18 min",
                            "Train polite meeting openings and project update conversations.",
                            "blue",
                            "Open a meeting naturally, move the agenda forward, and confirm next steps.",
                            "agenda,clarify,timeline,follow up",
                            "You are a professional meeting partner. Keep the conversation realistic, concise, and focused on agenda, risks, timeline, and follow-up actions.",
                            "Good morning. Could you briefly introduce today's agenda?",
                            6,
                            "Score fluency, grammar, vocabulary range, relevance, and business appropriateness."
                    ),
                    scenario(
                            "airport-checkin",
                            "Airport Check-in",
                            "Practice checking in, choosing seats, handling luggage, and asking for rescheduling help.",
                            "A2",
                            "Travel English",
                            "12 min",
                            "Train common airport check-in and rescheduling expressions.",
                            "gold",
                            "Complete basic check-in, seat selection, and rescheduling communication.",
                            "check in,boarding pass,aisle seat,reschedule",
                            "You are an airport check-in staff member. Ask practical travel questions and guide the learner through the check-in flow.",
                            "Hello. May I see your passport and booking reference?",
                            5,
                            "Score clarity, task completion, grammar, vocabulary, and politeness."
                    ),
                    scenario(
                            "dinner-smalltalk",
                            "Dinner Small Talk",
                            "Practice casual openings, natural follow-up questions, and polite recommendations.",
                            "B1",
                            "Natural conversation",
                            "15 min",
                            "Train relaxed small talk and smooth follow-up questions.",
                            "mint",
                            "Start a light conversation and keep it going with natural questions.",
                            "small talk,recommend,by the way,sounds great",
                            "You are a friendly dinner companion. Keep the dialogue casual and encourage the learner to ask follow-up questions.",
                            "This restaurant is popular. Have you been here before?",
                            6,
                            "Score fluency, interaction, grammar, vocabulary, and naturalness."
                    ),
                    scenario(
                            "clinic-visit",
                            "Clinic Visit",
                            "Practice describing symptoms, answering doctor questions, and confirming medicine instructions.",
                            "B1",
                            "Medical English",
                            "16 min",
                            "Train basic clinic communication and symptom descriptions.",
                            "rose",
                            "Describe symptoms clearly and understand simple medical instructions.",
                            "symptom,prescription,pharmacy,appointment",
                            "You are a clinic doctor. Ask careful but simple questions about symptoms, timeline, medicine, and allergies.",
                            "Hello. What brings you in today?",
                            6,
                            "Score clarity, symptom description, grammar, vocabulary, and relevance."
                    )
            );

            for (SpeakingScenario scenario : scenarios) {
                if (!scenarioRepository.existsById(scenario.getId())) {
                    scenarioRepository.save(scenario);
                }
            }
        };
    }

    private SpeakingScenario scenario(
            String id,
            String title,
            String description,
            String difficulty,
            String accent,
            String duration,
            String summary,
            String tone,
            String goal,
            String keywords,
            String rolePrompt,
            String openingMessage,
            int targetTurns,
            String scoringRubric
    ) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId(id);
        scenario.setTitle(title);
        scenario.setDescription(description);
        scenario.setDifficulty(difficulty);
        scenario.setAccent(accent);
        scenario.setDuration(duration);
        scenario.setSummary(summary);
        scenario.setTone(tone);
        scenario.setGoal(goal);
        scenario.setKeywords(keywords);
        scenario.setRolePrompt(rolePrompt);
        scenario.setOpeningMessage(openingMessage);
        scenario.setTargetTurns(targetTurns);
        scenario.setScoringRubric(scoringRubric);
        scenario.setActive(true);
        return scenario;
    }
}
