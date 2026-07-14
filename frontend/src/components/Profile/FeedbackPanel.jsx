import { CustomerServiceOutlined } from "@ant-design/icons";
import { Button, Typography } from "antd";
import { PageSectionHeader } from "../common/PageSectionHeader";

const { Title, Text } = Typography;

// 最近反馈面板只展示评估结果，音频回放等交互后续再接具体接口。
export function FeedbackPanel({ feedback }) {
  return (
    <section className="glass-panel">
      <PageSectionHeader
        eyebrow="Feedback"
        title="最近一次反馈"
        extra={
          <Button type="text" icon={<CustomerServiceOutlined />}>
            {feedback.playbackActionLabel}
          </Button>
        }
      />
      <div className="metrics-grid">
        {feedback.metrics.map((metric) => (
          <div className="metric-chip" key={metric.key}>
            <Text type="secondary">{metric.label}</Text>
            <Title level={3}>{metric.value}</Title>
          </div>
        ))}
      </div>
    </section>
  );
}
