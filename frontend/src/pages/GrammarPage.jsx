import { useCallback, useMemo, useState } from "react";
import { BookOutlined, HistoryOutlined, SearchOutlined } from "@ant-design/icons";
import { Button, Empty, Flex, Input, Space, Tag, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import { GrammarTopicGrid } from "../components/Grammar/GrammarTopicGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { FSRSPanel } from "../components/common/FSRSPanel";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph } = Typography;

const grammarPracticeTopics = [
  {
    id: "clause",
    title: "从句",
    summary: "覆盖定语从句、名词性从句和状语从句，训练句子结构判断与连接词选择。",
    examples: [
      "The person we spoke to made no answer at first.",
      "This is the hotel where we planned to stay."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "tense-voice",
    title: "时态与语态",
    summary: "训练时态、被动语态和时间线判断，提升复杂句中的谓语选择准确率。",
    examples: [
      "Everything had been taken before he returned.",
      "By the time you come home, I will have made dinner."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "vocabulary-logic",
    title: "词汇与逻辑",
    summary: "通过上下文选择准确词义、逻辑连接和表达关系，适合综合语法题。",
    examples: [
      "His response was both accurate and concise.",
      "Therefore, the conclusion needs more evidence."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "nonfinite-verb",
    title: "非谓语动词",
    summary: "聚焦不定式、动名词和分词结构，训练主被动关系和句法功能判断。",
    examples: [
      "Lost in thought, he almost ran into the car.",
      "Time should be made good use of to learn our lessons."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "preposition-collocation",
    title: "介词与固定搭配",
    summary: "整理高频介词、动词短语和固定搭配，减少介词误用。",
    examples: [
      "She is interested in environmental policy.",
      "We need to rely on accurate data."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "pronoun-determiner",
    title: "代词与限定词",
    summary: "训练代词指代、限定词搭配和数量关系，提升句子内部一致性判断。",
    examples: [
      "Each student should bring their own notebook.",
      "Most of the cars are very expensive."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "subject-verb-agreement",
    title: "主谓一致",
    summary: "识别真正主语与谓语形式，处理插入语、并列结构和数量短语。",
    examples: [
      "Joseph Wharton, along with his ventures, helped shape the industry.",
      "A list of names is on the desk."
    ],
    progress: 0,
    tag: "专项"
  },
  {
    id: "modal-subjunctive",
    title: "情态动词与虚拟语气",
    summary: "训练情态含义、推测表达和虚拟语气结构，强化语气与语义匹配。",
    examples: [
      "If I were you, I would clarify the agenda first.",
      "He insisted that the plan should be changed."
    ],
    progress: 0,
    tag: "专项"
  }
];

export function GrammarPage() {
  const navigate = useNavigate();
  const { grammar } = useAppServices();
  const [searchText, setSearchText] = useState("");
  const loader = useCallback(async () => {
    const memory = await grammar.getMemory();
    return { memory };
  }, [grammar]);
  const { data, loading, error } = useAsyncData(loader, [loader]);
  const filteredTopics = useMemo(() => {
    const keyword = searchText.trim().toLowerCase();

    if (!keyword) {
      return grammarPracticeTopics;
    }

    return grammarPracticeTopics.filter((topic) => topic.title.toLowerCase().includes(keyword));
  }, [searchText]);

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
              <Paragraph>按语法知识点进入专项训练，先从选择题题库开始建立正确率反馈。</Paragraph>
            </div>

            <FSRSPanel
              title="当前语法掌握率"
              retentionRate={data.memory.retentionRate}
              mastered={`${data.memory.mastered} 题`}
              dueCount={`${data.memory.dueCount} 题`}
              labelMastered="已掌握"
              labelDue="今日待复习"
              onDueClick={() => navigate("/grammar/practice/review")}
            />
          </section>

          <section className="glass-panel grammar-search-panel">
            <Flex align="center" className="grammar-search-row" gap={10} justify="space-between" wrap>
              <Input
                allowClear
                onChange={(event) => setSearchText(event.target.value)}
                placeholder="搜索语法类型"
                prefix={<SearchOutlined />}
                size="large"
                value={searchText}
              />
              <Space wrap>
                <Button
                  htmlType="button"
                  icon={<HistoryOutlined />}
                  onClick={() => navigate("/grammar/practice/review")}
                  size="large"
                >
                  复习
                </Button>
                <Button
                  htmlType="button"
                  icon={<BookOutlined />}
                  onClick={() => navigate("/grammar/notebook")}
                  size="large"
                >
                  练习本
                </Button>
              </Space>
            </Flex>
          </section>

          {filteredTopics.length ? (
            <GrammarTopicGrid
              onStart={(topic) => navigate(`/grammar/practice/${encodeURIComponent(topic.title)}`)}
              topics={filteredTopics}
            />
          ) : (
            <section className="glass-panel">
              <Empty description="没有匹配的语法类型" image={Empty.PRESENTED_IMAGE_SIMPLE} />
            </section>
          )}
        </div>
      ) : null}
    </AsyncPage>
  );
}
