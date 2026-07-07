import { useCallback } from "react";
import { PlayCircleOutlined } from "@ant-design/icons";
import { Button, Progress, Space, Tag, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import { DashboardModuleGrid } from "../components/Dashboard/DashboardModuleGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { MetricIcon } from "../components/common/MetricIcon";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Paragraph, Text } = Typography;

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

          <DashboardModuleGrid onNavigate={navigate} />

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
