import { LockOutlined, LoginOutlined, MailOutlined } from "@ant-design/icons";
import { App, Button, Form, Input, Space, Tag, Typography } from "antd";
import { useState } from "react";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const { Title, Paragraph, Text } = Typography;

function getRedirectTarget(location) {
  const from = location.state?.from;

  if (from?.pathname) {
    return `${from.pathname}${from.search ?? ""}`;
  }

  return "/profile";
}

function applyFieldErrors(form, error) {
  if (!error?.fieldErrors) {
    return false;
  }

  form.setFields(
    Object.entries(error.fieldErrors).map(([name, message]) => ({
      name,
      errors: [message]
    }))
  );

  return true;
}

export function LoginPage() {
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const { message } = App.useApp();
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const redirectTarget = getRedirectTarget(location);

  if (!auth.loading && auth.isAuthenticated) {
    return <Navigate to="/profile" replace />;
  }

  const handleSubmit = async (values) => {
    setSubmitting(true);

    try {
      await auth.login(values);
      message.success("登录成功");
      navigate(redirectTarget, { replace: true });
    } catch (error) {
      if (!applyFieldErrors(form, error)) {
        message.error(error.message || "登录失败，请检查账号和密码。");
      }
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-copy glass-panel">
        <Space align="center" wrap>
          <Tag bordered={false} className="soft-tag soft-tag--dark">
            Account
          </Tag>
          {/* <Tag bordered={false} className="soft-tag">
            登录
          </Tag> */}
        </Space>
        <Title>欢迎回来</Title>
        <Paragraph>
          登录后可以让练习记录、学习计划和个人反馈都归到同一个学习身份下。
        </Paragraph>
      </div>

      <div className="auth-card glass-panel">
        <div className="section-head">
          <Text className="eyebrow">Login</Text>
          <Title level={3}>账号登录</Title>
          <Paragraph>使用用户名或邮箱继续学习。</Paragraph>
        </div>

        <Form form={form} layout="vertical" requiredMark={false} onFinish={handleSubmit}>
          <Form.Item
            label="账号"
            name="account"
            rules={[{ required: true, message: "请输入用户名或邮箱。" }]}
          >
            <Input prefix={<MailOutlined />} placeholder="learner 或 learner@example.com" />
          </Form.Item>
          <Form.Item
            label="密码"
            name="password"
            rules={[{ required: true, message: "请输入密码。" }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="请输入密码" />
          </Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            icon={<LoginOutlined />}
            loading={submitting}
            className="auth-submit"
          >
            登录
          </Button>
        </Form>

        <div className="auth-switch">
          <Text type="secondary">还没有账号？</Text>
          <Link to="/register" state={location.state}>创建账号</Link>
        </div>
      </div>
    </section>
  );
}
