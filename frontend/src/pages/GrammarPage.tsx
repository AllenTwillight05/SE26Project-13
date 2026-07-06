import { useCallback } from "react";
import { Button, Flex, Progress, Tag, Typography } from "antd";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph, Text } = Typography;

export function GrammarPage() {
  const { grammar } = useAppServices();
  const loader = useCallback(() => grammar.getSnapshot(), [grammar]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Grammar"
              title="语法练习"
              description={data.focus}
              extra={
                <Tag bordered={false} className="soft-tag">
                  句型理解 + 口语应用
                </Tag>
              }
            />
          </section>

          <section className="feature-grid">
            {data.topics.map((topic) => (
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
        </div>
      ) : null}
    </AsyncPage>
  );
}

