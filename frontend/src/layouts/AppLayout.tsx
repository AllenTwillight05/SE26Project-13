import { Avatar, Layout, Space, Tag, Typography } from "antd";
import { Outlet } from "react-router-dom";
import { AppHeader } from "../components/navigation/AppHeader";

const { Content } = Layout;
const { Text } = Typography;

export function AppLayout() {
  return (
    <Layout className="app-shell">
      <AppHeader />
      <Content className="page-content">
        <section className="page-banner">
          <div>
            <Tag bordered={false} className="soft-tag soft-tag--dark">
              A6 口语学习助手
            </Tag>
          </div>
          <Space size={12}>
            <Text type="secondary">前端接口已按模块预留</Text>
            <Avatar className="profile-avatar">EL</Avatar>
          </Space>
        </section>
        <Outlet />
      </Content>
    </Layout>
  );
}

