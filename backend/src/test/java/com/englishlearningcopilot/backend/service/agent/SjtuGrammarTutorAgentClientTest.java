package com.englishlearningcopilot.backend.service.agent;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.dto.GrammarTutorMessage;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SjtuGrammarTutorAgentClientTest {

    @Test
    void buildsQuestionAnswerExplanationAndRelatedMistakeContext() {
        GrammarTutorProperties properties = new GrammarTutorProperties(
                "https://example.test/v1", "test-key", "deepseek-chat", 0.3, 500, 30000
        );
        SjtuGrammarTutorAgentClient client = new SjtuGrammarTutorAgentClient(properties, new ObjectMapper());

        List<Map<String, String>> messages = client.buildMessages(
                question(1, "Tense"),
                "B",
                List.of(new GrammarTutorMessage("assistant", "先找谓语")),
                "为什么？",
                List.of(question(2, "Tense"))
        );

        assertThat(messages.getFirst().get("content"))
                .contains("你叫 LUMI")
                .contains("英语学习助手")
                .contains("禁止使用任何 Markdown")
                .contains("自然语言纯文本");
        assertThat(messages.get(1).get("content"))
                .contains("Choose the answer.")
                .contains("正确答案：A")
                .contains("学习者选择：B")
                .contains("同分类历史错题");
        assertThat(messages).contains(Map.of("role", "assistant", "content", "先找谓语"));
        assertThat(messages.getLast()).containsEntry("role", "user").containsEntry("content", "为什么？");
    }

    private static GrammarQuestion question(Integer id, String category) {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(id);
        question.setGrammarCategory(category);
        question.setQuestionText("Choose the answer.");
        question.setOptionA("A");
        question.setOptionB("B");
        question.setOptionC("C");
        question.setOptionD("D");
        question.setAnswer("A");
        question.setExplanation("Because.");
        return question;
    }
}
