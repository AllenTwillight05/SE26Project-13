import { RocketOutlined } from "@ant-design/icons";
import { Space, Typography } from "antd";
import { NavLink } from "react-router-dom";
import { navigationItems } from "../../router/navigation";

const { Text } = Typography;

export function AppHeader() {
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
    </header>
  );
}

