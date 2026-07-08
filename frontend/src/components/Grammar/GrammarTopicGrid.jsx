import { Button, Flex, Progress, Tag, Typography } from "antd";

const { Title, Paragraph, Text } = Typography;

// 语法主题网格只负责主题和例句展示，练习提交逻辑后续放到语法服务里。
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
            <Text type="secondary">掌握度</Text>
            <Button>开始练习</Button>
          </Flex>
        </article>
      ))}
    </section>
  );
}
