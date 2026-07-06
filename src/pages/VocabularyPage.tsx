import { useCallback } from "react";
import { Button, Flex, Progress, Tag, Typography } from "antd";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph, Text } = Typography;

export function VocabularyPage() {
  const { vocabulary } = useAppServices();
  const loader = useCallback(() => vocabulary.getSnapshot(), [vocabulary]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Vocabulary Route"
              title="词汇强化卡组"
              description="适合后续接入词库、错词本、复习队列和 TTS 发音服务。"
              extra={
                <Tag bordered={false} className="soft-tag">
                  {data.retentionHint}
                </Tag>
              }
            />
          </section>

          <section className="feature-grid">
            {data.cards.map((item) => (
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
        </div>
      ) : null}
    </AsyncPage>
  );
}

