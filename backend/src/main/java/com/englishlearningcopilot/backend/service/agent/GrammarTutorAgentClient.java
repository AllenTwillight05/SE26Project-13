package com.englishlearningcopilot.backend.service.agent;

import com.englishlearningcopilot.backend.dto.GrammarTutorMessage;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import java.util.List;

public interface GrammarTutorAgentClient {

    String explain(
            GrammarQuestion question,
            String selectedAnswer,
            List<GrammarTutorMessage> history,
            String userMessage,
            List<GrammarQuestion> relatedMistakes
    );
}
