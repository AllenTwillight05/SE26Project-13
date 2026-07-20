import { Flex, Progress, Typography } from "antd";

const { Title, Text } = Typography;

export function FSRSPanel({
  title,
  retentionRate,
  mastered,
  dueCount,
  labelMastered = "已掌握",
  labelDue = "今日待复习",
  onDueClick
}) {
  const percent = Math.max(0, Math.min(100, Number(retentionRate) || 0));
  const dueRoleProps = onDueClick
    ? {
        role: "button",
        tabIndex: 0,
        onClick: onDueClick,
        onKeyDown: (event) => {
          if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            onDueClick();
          }
        }
      }
    : {};

  return (
    <div className="memory-panel">
      <Text strong>{title}</Text>
      <Flex className="fsrs-panel__main" justify="center" align="center">
        <Progress
          className="fsrs-panel__ring"
          format={(value) => `${value}%`}
          percent={percent}
          size={132}
          strokeColor="#3d4f93"
          trailColor="rgba(17, 17, 17, 0.08)"
          type="circle"
        />
      </Flex>
      <div className="memory-stat-grid">
        <div className="memory-stat">
          <Title level={3}>{mastered}</Title>
          <Text type="secondary">{labelMastered}</Text>
        </div>
        <div className={onDueClick ? "memory-stat memory-stat--action" : "memory-stat"} {...dueRoleProps}>
          <Title level={3}>{dueCount}</Title>
          <Text type="secondary">{labelDue}</Text>
        </div>
      </div>
    </div>
  );
}
