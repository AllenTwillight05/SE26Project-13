import { LoginOutlined, LogoutOutlined, RocketOutlined } from "@ant-design/icons";
import { App, Button, Space, Tag, Typography } from "antd";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import { navigationItems } from "../../router/navigation";

const { Text } = Typography;

export function AppHeader() {
  const { message } = App.useApp();
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await auth.logout();
    message.success("已退出登录");
    navigate("/", { replace: true });
  };

  return (
    <header className="topbar">
      <div className="topbar__brand">
        <div className="brand-mark">
          <RocketOutlined />
        </div>
        <div>
          <Text className="brand-title">English Learning Copilot</Text>
          <div className="brand-subtitle">口语学习中枢</div>
        </div>
      </div>
      <nav className="topbar__nav" aria-label="primary">
        <Space size={8} wrap>
          {navigationItems.map((item) => (
            <NavLink
              key={item.key}
              to={item.path}
              end={item.path === "/"}
              className={({ isActive }) =>
                isActive ? "nav-pill nav-pill--active" : "nav-pill"
              }
            >
              <span className="nav-pill__icon">{item.icon}</span>
              <span>{item.label}</span>
            </NavLink>
          ))}
        </Space>
      </nav>
      <div className="topbar__auth">
        {auth.isAuthenticated ? (
          <Space size={8} wrap>
            <div className="user-chip">
              <span className="user-chip__name">{auth.user.displayName || auth.user.username}</span>
              <Tag bordered={false} className="soft-tag">
                {auth.user.role}
              </Tag>
            </div>
            <Button icon={<LogoutOutlined />} onClick={handleLogout} loading={auth.loading}>
              退出
            </Button>
          </Space>
        ) : (
          <Space size={8} wrap>
            <Button
              icon={<LoginOutlined />}
              onClick={() => navigate("/login", { state: { from: location } })}
            >
              登录
            </Button>
          </Space>
        )}
      </div>
    </header>
  );
}
