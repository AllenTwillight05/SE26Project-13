import { Flex, Progress, Space, Typography } from "antd";
import { PageSectionHeader } from "../common/PageSectionHeader";

const { Text } = Typography;

function progressColor(tone) {
  if (tone === "teal") {
    return "#0f766e";
  }

  if (tone === "gold") {
    return "#c08a1d";
  }

  return "#111111";
}

export function ProgressPanel({ progress }) {
  return (
    <section className="glass-panel">
      <PageSectionHeader eyebrow="Progress" title="能力进度" />
      <Space direction="vertical" size={18} className="full-width">
        {progress.map((metric) => (
          <div key={metric.id}>
            <Flex justify="space-between">
              <Text type="secondary">{metric.label}</Text>
              <Text>{metric.value}%</Text>
            </Flex>
            <Progress
              percent={metric.value}
              showInfo={false}
              strokeColor={progressColor(metric.tone)}
            />
          </div>
        ))}
      </Space>
    </section>
  );
}
