import { Typography } from "antd";

const { Text } = Typography;

function formatScore(value, fallback = "-") {
  if (typeof value !== "number" || Number.isNaN(value)) {
    return fallback;
  }
  return Number.isInteger(value) ? String(value) : value.toFixed(1);
}

function TurnScoreMetric({ label, value, unit = "/100" }) {
  return (
    <div className="turn-score-metric">
      <Text type="secondary" className="turn-score-metric__label">{label}</Text>
      <div className="turn-score-metric__value-row">
        <Text strong className="turn-score-metric__value">{formatScore(value)}</Text>
        {value !== null && value !== undefined && unit ? (
          <Text type="secondary" className="turn-score-metric__unit">{unit}</Text>
        ) : null}
      </div>
    </div>
  );
}

export function SpeakingTurnFeedbackCard({ turn }) {
  const score = turn.score;

  return (
    <div className="glass-panel feedback-turn-card">
      <div className="feedback-turn-card__header">
        <Text strong>第 {turn.turnIndex} 轮</Text>
        {score ? (
          <Text className="feedback-turn-card__badge">
            {formatScore(score.totalScore)} / 100
          </Text>
        ) : (
          <Text type="secondary">暂无评分</Text>
        )}
      </div>

      <div className="feedback-turn-copy">
        <div>
          <Text type="secondary" className="turn-copy-label">你的回答</Text>
          <Text className="turn-copy-text">{turn.userText || "暂无转写文本"}</Text>
        </div>
        <div>
          <Text type="secondary" className="turn-copy-label">对话回复</Text>
          <Text className="turn-copy-text">{turn.agentText || "暂无回复"}</Text>
        </div>
      </div>

      {score ? (
        <div className="turn-score-grid">
          <TurnScoreMetric label="参考分" value={score.totalScore} />
          <TurnScoreMetric label="准确度" value={score.accuracy} />
          <TurnScoreMetric label="流利度" value={score.fluency} />
          {score.speed > 0 ? <TurnScoreMetric label="语速" value={score.speed} unit="WPM" /> : null}
        </div>
      ) : null}
    </div>
  );
}
