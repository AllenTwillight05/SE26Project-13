import type { PropsWithChildren } from "react";
import { App as AntApp, ConfigProvider } from "antd";
import zhCN from "antd/locale/zh_CN";
import { appTheme } from "./theme";
import { appServices } from "../services";
import { AppServicesProvider } from "../services/ServiceContext";

export function AppProviders({ children }: PropsWithChildren) {
  return (
    <ConfigProvider locale={zhCN} theme={appTheme}>
      <AntApp>
        <AppServicesProvider services={appServices}>{children}</AppServicesProvider>
      </AntApp>
    </ConfigProvider>
  );
}

