import { Button, Flex, Progress, Tag, Typography } from "antd";

const { Title, Paragraph, Text } = Typography;

// 词汇卡组只负责词卡展示，加入复习队列的真实行为后续接 service。
export function VocabularyDeckGrid({ cards }) {
  return (
    <section className="feature-grid">
      {cards.map((item) => (
        <article className="deck-card" key={item.id}>
          <Flex justify="space-between" align="center">
            <Title level={4}>{item.word}</Title>
            <Tag bordered={false} className="soft-tag">
              {item.tag}
            </Tag>
          </Flex>
          <Paragraph>{item.usage}</Paragraph>
          <Progress percent={item.progress} showInfo={false} strokeLinecap="round" />
          <Flex justify="space-between" align="center">
            <Text type="secondary">掌握度</Text>
            <Text>{item.progress}%</Text>
          </Flex>
          <Button>加入复习队列</Button>
        </article>
      ))}
    </section>
  );
}
