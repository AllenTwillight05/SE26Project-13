package com.englishlearningcopilot.backend.service.agent;

import com.englishlearningcopilot.backend.dto.GrammarTutorMessage;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "grammar.tutor.provider", havingValue = "mock")
public class MockGrammarTutorAgentClient implements GrammarTutorAgentClient {

    @Override
    public String explain(
            GrammarQuestion question,
            String selectedAnswer,
            List<GrammarTutorMessage> history,
            String userMessage,
            List<GrammarQuestion> relatedMistakes
    ) {
        String explanation = question.getExplanation() == null || question.getExplanation().isBlank()
                ? "这道题需要结合句子结构和选项含义判断。"
                : question.getExplanation();
        return "这道题考查“" + question.getGrammarCategory() + "”。正确答案是 "
                + question.getAnswer() + "，你选择了 " + selectedAnswer + "。\n\n"
                + explanation + "\n\n"
                + "可以先找出句子的核心主谓结构，再判断空格在句中承担的成分。你具体卡在哪一步？";
    }
}
