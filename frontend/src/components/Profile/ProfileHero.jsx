import { Avatar, Flex, Space, Statistic, Typography } from "antd";

const { Title, Text } = Typography;

export function ProfileHero({ profile }) {
  return (
    <section className="profile-hero glass-panel">
      <Flex justify="space-between" align="center" gap={20} wrap="wrap">
        <Space size={16}>
          <Avatar size={64} className="profile-avatar">
            EL
          </Avatar>
          <div>
            <Text className="eyebrow">Profile</Text>
            <Title level={3}>{profile.learnerName}</Title>
            <Text type="secondary">当前水平：{profile.level}</Text>
          </div>
        </Space>
        <Space size={16} wrap>
          <Statistic title="连续学习" value={profile.streak} />
          <Statistic title="本周提升" value={profile.dailyPlan.weeklyImprovement} />
        </Space>
      </Flex>
    </section>
  );
}
