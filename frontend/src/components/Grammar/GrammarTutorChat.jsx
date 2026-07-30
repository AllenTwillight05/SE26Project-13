import { useEffect, useMemo, useRef, useState } from "react";
import { BulbOutlined, RobotOutlined, SendOutlined } from "@ant-design/icons";
import { Alert, Button, Flex, Input, Tag, Typography } from "antd";

const { Paragraph, Text, Title } = Typography;
const MAX_TURNS = 5;

const welcomeMessage = {
  id: "welcome",
  role: "assistant",
  content: "还有哪里不明白？你可以问我为什么选这个答案、其他选项错在哪里，或者请我换一种方式讲解。",
  greeting: true
};

export function GrammarTutorChat({ onSend, questionId, selectedAnswer }) {
  const [messages, setMessages] = useState([welcomeMessage]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [relatedMistakeCount, setRelatedMistakeCount] = useState(0);
  const listRef = useRef(null);

  const userTurnCount = useMemo(
    () => messages.filter((message) => message.role === "user").length,
    [messages]
  );
  const reachedTurnLimit = userTurnCount >= MAX_TURNS;

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [loading, messages]);

  async function handleSend() {
    const content = input.trim();
    if (!content || loading || reachedTurnLimit) {
      return;
    }

    const history = messages
      .filter((message) => !message.greeting)
      .map(({ role, content: previousContent }) => ({ role, content: previousContent }))
      .slice(-8);
    const userMessage = { id: `user-${Date.now()}`, role: "user", content };
    setMessages((current) => [...current, userMessage]);
    setInput("");
    setError("");
    setLoading(true);

    try {
      const response = await onSend({
        grammarQuestionId: questionId,
        selectedAnswer,
        message: content,
        history
      });
      setMessages((current) => [
        ...current,
        { id: `assistant-${Date.now()}`, role: "assistant", content: response.reply }
      ]);
      setRelatedMistakeCount(response.relatedMistakeCount ?? 0);
    } catch (requestError) {
      setMessages((current) => current.filter((message) => message.id !== userMessage.id));
      setInput(content);
      setError(
        requestError?.status === 401
          ? "登录状态已失效，请重新登录后继续提问。"
          : "语法导师暂时没有回应，请稍后重试。"
      );
    } finally {
      setLoading(false);
    }
  }

  function handleKeyDown(event) {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      handleSend();
    }
  }

  return (
    <section className="grammar-tutor" aria-labelledby="grammar-tutor-title">
      <Flex justify="space-between" align="start" gap={12} wrap>
        <div>
          <Text className="eyebrow">AI Grammar Tutor</Text>
          <Title id="grammar-tutor-title" level={4}>
            <RobotOutlined /> 对话讲解
          </Title>
        </div>
        <Tag bordered={false} icon={<BulbOutlined />} className="soft-tag">
          短对话 {userTurnCount}/{MAX_TURNS}
        </Tag>
      </Flex>

      <div className="grammar-tutor__messages" ref={listRef} aria-live="polite">
        {messages.map((message) => (
          <div
            className={`grammar-tutor__message grammar-tutor__message--${message.role}`}
            key={message.id}
          >
            <Text strong>{message.role === "assistant" ? "语法导师" : "你"}</Text>
            <Paragraph>{message.content}</Paragraph>
          </div>
        ))}
        {loading ? (
          <div className="grammar-tutor__message grammar-tutor__message--assistant">
            <Text strong>语法导师</Text>
            <Paragraph className="grammar-tutor__thinking">正在结合这道题思考…</Paragraph>
          </div>
        ) : null}
      </div>

      {relatedMistakeCount > 0 ? (
        <Text type="secondary" className="grammar-tutor__context-note">
          本次讲解已参考你在同类语法中的 {relatedMistakeCount} 道历史错题。
        </Text>
      ) : null}
      {error ? <Alert type="error" showIcon message={error} /> : null}
      {reachedTurnLimit ? (
        <Alert type="info" showIcon message="本题的短对话已结束。你可以继续练习，或重新打开本题再问。" />
      ) : (
        <Flex gap={10} align="end" className="grammar-tutor__composer">
          <Input.TextArea
            aria-label="向语法导师提问"
            autoSize={{ minRows: 2, maxRows: 4 }}
            maxLength={1000}
            onChange={(event) => setInput(event.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="例如：为什么这里不能用 which？"
            value={input}
          />
          <Button
            aria-label="发送问题"
            disabled={!input.trim()}
            htmlType="button"
            icon={<SendOutlined />}
            loading={loading}
            onClick={handleSend}
            type="primary"
          >
            发送
          </Button>
        </Flex>
      )}
    </section>
  );
}
