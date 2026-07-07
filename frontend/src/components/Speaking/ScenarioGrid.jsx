import { AudioOutlined, PlayCircleOutlined } from "@ant-design/icons";
import { Button, Flex, Space, Tag, Typography } from "antd";

const { Title, Text, Paragraph } = Typography;

export function ScenarioGrid({ scenarios, selectedMode }) {
  return (
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
  );
}
