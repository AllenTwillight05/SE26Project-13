import { LoginOutlined, UserOutlined } from "@ant-design/icons";
import { Avatar, Button, Flex, Result, Tag, Typography } from "antd";
import { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { FeedbackPanel } from "../components/Profile/FeedbackPanel";
import { LearningPlanPanel } from "../components/Profile/LearningPlanPanel";
import { ProfileHero } from "../components/Profile/ProfileHero";
import { ProgressPanel } from "../components/Profile/ProgressPanel";
import { AsyncPage } from "../components/common/AsyncPage";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Text, Title } = Typography;

export function ProfilePage() {
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const { profile } = useAppServices();
  const loader = useCallback(() => {
    if (auth.loading || !auth.isAuthenticated) {
      return Promise.resolve(null);
    }

    return profile.getSnapshot();
  }, [auth.isAuthenticated, auth.loading, profile]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  if (auth.loading) {
    return <AsyncPage loading error={null} />;
  }

  if (!auth.isAuthenticated) {
    return (
      <div className="page-stack">
        <section className="glass-panel profile-empty-state">
          <Result
            icon={<UserOutlined />}
            title="个人页面需要登录"
            subTitle="登录后可以查看你的学习计划、能力进度和最近反馈。"
            extra={
              <Button
                type="primary"
                icon={<LoginOutlined />}
                onClick={() => navigate("/login", { state: { from: location } })}
              >
                请先登录
              </Button>
            }
          />
        </section>
      </div>
    );
  }

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          {auth.isAuthenticated ? (
            <section className="glass-panel account-strip">
              <Flex justify="space-between" align="center" gap={16} wrap="wrap">
                <Flex align="center" gap={14}>
                  <Avatar size={46} className="profile-avatar" icon={<UserOutlined />} />
                  <div>
                    <Text className="eyebrow">Signed in</Text>
                    <Title level={5}>{auth.user.displayName || auth.user.username}</Title>
                    <Text type="secondary">{auth.user.email}</Text>
                  </div>
                </Flex>
                <Tag bordered={false} className="soft-tag soft-tag--dark">
                  {auth.user.role}
                </Tag>
              </Flex>
            </section>
          ) : null}

          <ProfileHero profile={data} />

          <section className="split-panel">
            <LearningPlanPanel plan={data.dailyPlan} />
            <ProgressPanel progress={data.dailyPlan.progress} />
          </section>

          <FeedbackPanel feedback={data.feedback} />
        </div>
      ) : null}
    </AsyncPage>
  );
}
