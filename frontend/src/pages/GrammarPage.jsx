import { useCallback } from "react";
import { Space, Tag, Typography } from "antd";
import { GrammarMasteryPanel } from "../components/Grammar/GrammarMasteryPanel";
import { GrammarProgressRing } from "../components/Grammar/GrammarProgressRing";
import { GrammarTopicGrid } from "../components/Grammar/GrammarTopicGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph } = Typography;

const grammarOverview = {
  masteryLabel: "当前语法掌握率",
  masteryRate: 76,
  stats: [
    { value: "24 组", label: "已掌握" },
    { value: "6 组", label: "待巩固" },
    { value: "12 题", label: "今日待练" }
  ]
};

const grammarProgress = {
  completed: 5,
  total: 12
};

const grammarTopics = [
  {
    id: "collocation-preposition",
    title: "固定搭配与介词",
    summary: "训练常见动词、形容词与介词的搭配关系，减少中式表达和介词误用。",
    examples: [
      "She is interested in environmental policy.",
      "We need to rely on accurate data."
    ],
    progress: 72,
    tag: "搭配"
  },
  {
    id: "grammar-vocabulary",
    title: "词汇",
    summary: "通过语境判断词形、词义和搭配选择，强化语法题里的词汇辨析能力。",
    examples: [
      "The proposal requires careful consideration.",
      "His response was both accurate and concise."
    ],
    progress: 64,
    tag: "辨析"
  }
];

export function GrammarPage() {
  const { grammar } = useAppServices();
  const loader = useCallback(() => grammar.getSnapshot(), [grammar]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack grammar-page">
          <section className="vocabulary-hero glass-panel">
            <div className="vocabulary-hero__copy">
              <Space align="center" wrap>
                <Tag bordered={false} className="soft-tag soft-tag--dark">
                  Grammar Practice
                </Tag>
              </Space>
              <Title>语法练习</Title>
              <Paragraph>{data.focus}</Paragraph>
            </div>

            <GrammarProgressRing
              completed={grammarProgress.completed}
              total={grammarProgress.total}
            />

            <GrammarMasteryPanel overview={grammarOverview} />
          </section>

          <GrammarTopicGrid topics={grammarTopics} />
        </div>
      ) : null}
    </AsyncPage>
  );
}
