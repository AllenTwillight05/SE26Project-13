import { useState } from "react";
import { Segmented, Typography } from "antd";

const { Title, Text } = Typography;

const categoryOptions = [
  { label: "口语", value: "speaking" },
  { label: "词汇", value: "vocabulary" },
  { label: "语法", value: "grammar" }
];

function getItemKey(item, category) {
  if (category === "vocabulary") {
    return item.vocabularyId ?? item.word;
  }

  if (category === "grammar") {
    return item.grammarCategory ?? item.topic;
  }

  return item.topic;
}

function renderLearningItem(item, category) {
  if (category === "vocabulary") {
    return (
      <div className="dashboard-community__copy">
        <Text strong>{item.word}</Text>
        <span className="helper-text">{item.briefTranslation}</span>
        {item.learnerCount !== undefined ? (
          <span className="helper-text">{item.learnerCount} 人学过</span>
        ) : null}
      </div>
    );
  }

  if (category === "grammar") {
    return (
      <div className="dashboard-community__copy">
        <Text strong>{item.grammarCategory ?? item.topic}</Text>
        {item.learnerCount !== undefined ? (
          <span className="helper-text">{item.learnerCount} 人学过</span>
        ) : null}
      </div>
    );
  }

  return (
    <div className="dashboard-community__copy">
      <Text strong>{item.topic}</Text>
      <span className="helper-text">{item.description}</span>
    </div>
  );
}

export function DashboardCommunityLearning({ learningTrends }) {
  const [activeCategory, setActiveCategory] = useState("vocabulary");
  const activeItems = learningTrends[activeCategory] ?? [];

  return (
    <section className="glass-panel dashboard-community">
      <div className="dashboard-community__header">
        <Title level={4}>大家都在学</Title>
        <Segmented
          options={categoryOptions}
          value={activeCategory}
          onChange={setActiveCategory}
        />
      </div>

      <div className="dashboard-community__list">
        {activeItems.map((item, index) => (
          <article
            className="dashboard-community__item"
            key={getItemKey(item, activeCategory)}
          >
            <span
              className={`dashboard-community__rank dashboard-community__rank--${item.rank ?? index + 1}`}
            >
              {item.rank ?? index + 1}
            </span>
            {renderLearningItem(item, activeCategory)}
          </article>
        ))}
      </div>
    </section>
  );
}
