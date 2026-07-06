import { useCallback } from "react";
import {
  AudioOutlined,
  BookOutlined,
  PlayCircleOutlined,
  ReadOutlined,
  UserOutlined
} from "@ant-design/icons";
import { Button, Progress, Space, Tag, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import { AsyncPage } from "../components/common/AsyncPage";
import { MetricIcon } from "../components/common/MetricIcon";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph, Text } = Typography;

const moduleCards = [
  {
    title: "口语",
    description: "情境对话、录音练习与口语反馈入口。",
    path: "/speaking",
    icon: <AudioOutlined />
  },
  {
    title: "词汇",
    description: "高频词、错词回顾与复习队列入口。",
    path: "/vocabulary",
    icon: <BookOutlined />
  },
  {
    title: "语法",
    description: "语法主题、例句理解与句型训练入口。",
    path: "/grammar",
    icon: <ReadOutlined />
  },
  {
    title: "个人",
    description: "学习计划、能力进度和最近反馈入口。",
    path: "/profile",
    icon: <UserOutlined />
  }
];

export function DashboardPage() {
  const navigate = useNavigate();
  const { dashboard, profile } = useAppServices();

  const loader = useCallback(async () => {
    const [overview, profileSnapshot] = await Promise.all([
      dashboard.getOverview(),
      profile.getSnapshot()
    ]);

    return {
      overview,
      profileSnapshot
    };
  }, [dashboard, profile]);

  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel home-summary">
            <div>
              <Space align="center" wrap>
                <Tag bordered={false} className="soft-tag soft-tag--dark">
                  {data.overview.productTag}
                </Tag>
                <Tag bordered={false} className="soft-tag">
                  {data.overview.stackTag}
                </Tag>
              </Space>
              <Title>{data.overview.headline}</Title>
              <Paragraph>{data.overview.description}</Paragraph>
            </div>
            <Button
              type="primary"
              size="large"
              icon={<PlayCircleOutlined />}
              onClick={() => navigate("/speaking")}
            >
              开始口语练习
            </Button>
          </section>

          <section className="home-panel-grid">
            {moduleCards.map((card) => (
              <button
                className="home-panel"
                key={card.path}
                type="button"
                onClick={() => navigate(card.path)}
              >
                <span className="home-panel__icon">{card.icon}</span>
                <span>
                  <Text strong>{card.title}</Text>
                  <span className="helper-text">{card.description}</span>
                </span>
              </button>
            ))}
          </section>

          <section className="split-panel">
            <div className="glass-panel">
              <Text className="eyebrow">Overview</Text>
              <Title level={4}>今日概览</Title>
              <div className="metrics-grid">
                {data.overview.quickStats.map((stat) => (
                  <div className="metric-chip" key={stat.label}>
                    <div className="stat-tile__icon">
                      <MetricIcon icon={stat.icon} />
                    </div>
                    <Text type="secondary">{stat.label}</Text>
                    <Title level={3}>{stat.value}</Title>
                  </div>
                ))}
              </div>
            </div>

            <div className="glass-panel">
              <Text className="eyebrow">Progress</Text>
              <Title level={4}>个人进度</Title>
              <Space direction="vertical" size={18} className="full-width">
                {data.profileSnapshot.dailyPlan.progress.map((metric) => (
                  <div key={metric.id}>
                    <div className="list-row">
                      <Text type="secondary">{metric.label}</Text>
                      <Text>{metric.value}%</Text>
                    </div>
                    <Progress
                      percent={metric.value}
                      showInfo={false}
                      strokeColor={
                        metric.tone === "teal"
                          ? "#0f766e"
                          : metric.tone === "gold"
                            ? "#c08a1d"
                            : "#111111"
                      }
                    />
                  </div>
                ))}
              </Space>
            </div>
          </section>
        </div>
      ) : null}
    </AsyncPage>
  );
}
