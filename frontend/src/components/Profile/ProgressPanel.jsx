import { Flex, Progress, Space, Typography } from "antd";
import { PageSectionHeader } from "../common/PageSectionHeader";

const { Text } = Typography;

// 进度条颜色集中在这里映射，避免页面层散落视觉判断。
function progressColor(tone) {
  if (tone === "teal") {
    return "#0f766e";
  }

  if (tone === "gold") {
    return "#c08a1d";
  }

  return "#111111";
}

// 能力进度面板只负责展示各项进度，详细趋势图后续可在本组件内扩展。
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
