import { App as AntApp, ConfigProvider } from "antd";
import zhCN from "antd/locale/zh_CN";
import { appTheme } from "./theme";
import { appServices } from "../services";
import { AppServicesProvider } from "../services/ServiceContext";

export function AppProviders({ children }) {
  return (
    <ConfigProvider locale={zhCN} theme={appTheme}>
      <AntApp>
        {/* 在应用入口统一注入 services，页面层只拿服务，不直接依赖 mock 或 HTTP 实现。 */}
        <AppServicesProvider services={appServices}>{children}</AppServicesProvider>
      </AntApp>
    </ConfigProvider>
  );
}
