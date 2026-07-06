import { useCallback, useMemo, useState } from "react";
import { AudioOutlined, PlayCircleOutlined } from "@ant-design/icons";
import { Button, Flex, Segmented, Space, Tag, Typography } from "antd";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Text, Paragraph } = Typography;

export function SpeakingPage() {
  const { speaking } = useAppServices();
  const loader = useCallback(() => speaking.getCatalog(), [speaking]);
  const { data, loading, error } = useAsyncData(loader, [loader]);
  const [mode, setMode] = useState<string>();

  const selectedMode = mode ?? data?.modes[0];
  const scenarios = useMemo(() => data?.scenarios ?? [], [data]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Practice Route"
              title="口语练习"
              description="面向餐厅、商务、旅行等真实场景，支持后续接入录音、评估和对话会话接口。"
              extra={
                <Segmented
                  options={data.modes}
                  value={selectedMode}
                  onChange={(value) => setMode(String(value))}
                />
              }
            />
          </section>

          <section className="feature-grid">
            {scenarios.map((scenario) => (
              <article className="scenario-card" key={scenario.id}>
                <div className={`scenario-card__tone scenario-card__tone--${scenario.tone}`} />
                <div className="scenario-card__body scenario-card__body--spacious">
                  <Flex justify="space-between" align="center">
                    <Tag bordered={false} className="soft-tag">
                      {selectedMode}
                    </Tag>
                    <Text type="secondary">{scenario.duration}</Text>
                  </Flex>
                  <div>
                    <Title level={4}>{scenario.title}</Title>
                    <Paragraph>{scenario.summary}</Paragraph>
                    <Text type="secondary">{scenario.accent}</Text>
                  </div>
                  <Space wrap>
                    <Button type="primary" icon={<PlayCircleOutlined />}>
                      开始练习
                    </Button>
                    <Button icon={<AudioOutlined />}>录音测试</Button>
                  </Space>
                </div>
              </article>
            ))}
          </section>

          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Script Preview"
              title={data.scriptPreviewTitle}
              description="这里后续可以接入剧本详情接口、语音合成接口和会话创建接口。"
            />
            <div className="script-preview">
              {data.scriptPreviewLines.map((line) => (
                <div className="script-line" key={line}>
                  {line}
                </div>
              ))}
            </div>
          </section>
        </div>
      ) : null}
    </AsyncPage>
  );
}
