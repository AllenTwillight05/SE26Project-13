import { useCallback } from "react";
import { CheckCircleFilled, CustomerServiceOutlined } from "@ant-design/icons";
import { Avatar, Button, Flex, List, Progress, Space, Statistic, Tag, Typography } from "antd";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Text } = Typography;

export function ProfilePage() {
  const { profile } = useAppServices();
  const loader = useCallback(() => profile.getSnapshot(), [profile]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="profile-hero glass-panel">
            <Flex justify="space-between" align="center" gap={20} wrap="wrap">
              <Space size={16}>
                <Avatar size={64} className="profile-avatar">
                  EL
                </Avatar>
                <div>
                  <Text className="eyebrow">Profile</Text>
                  <Title level={3}>{data.learnerName}</Title>
                  <Text type="secondary">当前水平：{data.level}</Text>
                </div>
              </Space>
              <Space size={16} wrap>
                <Statistic title="连续学习" value={data.streak} />
                <Statistic title="本周提升" value={data.dailyPlan.weeklyImprovement} />
              </Space>
            </Flex>
          </section>

          <section className="split-panel">
            <section className="glass-panel">
              <PageSectionHeader
                eyebrow="Learning Plan"
                title="今日学习计划"
                extra={
                  <Tag bordered={false} className="soft-tag">
                    自动规划已开启
                  </Tag>
                }
              />
              <List
                dataSource={data.dailyPlan.items}
                renderItem={(item) => (
                  <List.Item className="plain-list-item">
                    <div className="plan-row">
                      <div>
                        <Text strong>{item.task}</Text>
                        <div className="plan-meta">
                          {item.time} · {item.meta}
                        </div>
                      </div>
                      {item.done ? <CheckCircleFilled className="success-icon" /> : null}
                    </div>
                  </List.Item>
                )}
              />
            </section>

            <section className="glass-panel">
              <PageSectionHeader eyebrow="Progress" title="能力进度" />
              <Space direction="vertical" size={18} className="full-width">
                {data.dailyPlan.progress.map((metric) => (
                  <div key={metric.id}>
                    <Flex justify="space-between">
                      <Text type="secondary">{metric.label}</Text>
                      <Text>{metric.value}%</Text>
                    </Flex>
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
            </section>
          </section>

          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Feedback"
              title="最近一次反馈"
              extra={
                <Button type="text" icon={<CustomerServiceOutlined />}>
                  {data.feedback.playbackActionLabel}
                </Button>
              }
            />
            <div className="metrics-grid">
              {data.feedback.metrics.map((metric) => (
                <div className="metric-chip" key={metric.key}>
                  <Text type="secondary">{metric.label}</Text>
                  <Title level={3}>{metric.value}</Title>
                </div>
              ))}
            </div>
          </section>
        </div>
      ) : null}
    </AsyncPage>
  );
}

