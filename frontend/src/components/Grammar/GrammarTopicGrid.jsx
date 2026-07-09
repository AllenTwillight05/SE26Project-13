import { Button, Flex, Progress, Tag, Typography } from "antd";

const { Title, Paragraph, Text } = Typography;

export function GrammarTopicGrid({ topics }) {
  return (
    <section className="feature-grid">
      {topics.map((topic) => (
        <article className="deck-card" key={topic.id}>
          <Flex justify="space-between" align="center">
            <Title level={4}>{topic.title}</Title>
            <Tag bordered={false} className="soft-tag">
              {topic.tag}
            </Tag>
          </Flex>
          <Paragraph>{topic.summary}</Paragraph>
          <div className="script-preview">
            {topic.examples.map((example) => (
              <div className="script-line" key={example}>
                {example}
              </div>
            ))}
          </div>
          <Progress percent={topic.progress} showInfo={false} />
          <Flex justify="space-between" align="center">
            <Text type="secondary">正确率</Text>
            <Button>开始练习</Button>
          </Flex>
        </article>
      ))}
    </section>
  );
}
