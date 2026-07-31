import { LockOutlined, MailOutlined, SolutionOutlined, UserOutlined } from "@ant-design/icons";
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

export function RegisterPage() {
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
      await auth.register(values);
      message.success("注册成功");
      navigate(redirectTarget, { replace: true });
    } catch (error) {
      if (!applyFieldErrors(form, error)) {
        message.error(error.message || "注册失败，请稍后重试。");
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
            Register
          </Tag>
          {/* <Tag bordered={false} className="soft-tag">
            注册
          </Tag> */}
        </Space>
        <Title>创建你的学习身份</Title>
        <Paragraph>
          注册后会立即获得登录态，后续可以直接查看个人页、学习计划和最近反馈。
        </Paragraph>
      </div>

      <div className="auth-card glass-panel">
        <div className="section-head">
          <Text className="eyebrow">Register</Text>
          <Title level={3}>创建账号</Title>
          <Paragraph>字段规则与 Spring Boot 后端校验保持一致。</Paragraph>
        </div>

        <Form form={form} layout="vertical" requiredMark={false} onFinish={handleSubmit}>
          <Form.Item
            label="用户名"
            name="username"
            rules={[
              { required: true, message: "请输入用户名。" },
              { min: 3, max: 64, message: "用户名长度需在 3 到 64 个字符之间。" }
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder="3 到 64 个字符" />
          </Form.Item>
          <Form.Item
            label="邮箱"
            name="email"
            rules={[
              { required: true, message: "请输入邮箱。" },
              { type: "email", message: "请输入有效的邮箱地址。" }
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="you@example.com" />
          </Form.Item>
          <Form.Item
            label="展示名"
            name="displayName"
            rules={[
              { required: true, message: "请输入展示名。" },
              { max: 100, message: "展示名长度不能超过 100 个字符。" }
            ]}
          >
            <Input prefix={<SolutionOutlined />} placeholder="在个人页显示的名字" />
          </Form.Item>
          <Form.Item
            label="密码"
            name="password"
            rules={[
              { required: true, message: "请输入密码。" },
              { min: 8, max: 128, message: "密码长度需在 8 到 128 个字符之间。" }
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="至少 8 个字符" />
          </Form.Item>
          <Button type="primary" htmlType="submit" loading={submitting} className="auth-submit">
            注册并进入学习空间
          </Button>
        </Form>

        <div className="auth-switch">
          <Text type="secondary">已经有账号？</Text>
          <Link to="/login" state={location.state}>返回登录</Link>
        </div>
      </div>
    </section>
  );
}
