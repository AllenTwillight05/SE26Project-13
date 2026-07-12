import { createHttpServices } from "./httpServices";
import { createMockServices } from "./mockServices";

// 数据源切换入口：
// - mock：前端独立开发时默认走本地 mock。
// - http：联调 Spring Boot 时切到真实接口。
// - mixed：按模块选择 mock/http，适合多人并行联调各自负责的后端模块。
const apiMode = import.meta.env.VITE_API_MODE ?? "mock";
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "";

const mixedModuleModes = {
  auth: import.meta.env.VITE_AUTH_API_MODE ?? "http",
  dashboard: import.meta.env.VITE_DASHBOARD_API_MODE ?? "mock",
  speaking: import.meta.env.VITE_SPEAKING_API_MODE ?? "mock",
  vocabulary: import.meta.env.VITE_VOCABULARY_API_MODE ?? "mock",
  grammar: import.meta.env.VITE_GRAMMAR_API_MODE ?? "mock",
  profile: import.meta.env.VITE_PROFILE_API_MODE ?? "mock"
};

const mockServices = createMockServices();
const httpServices = createHttpServices(apiBaseUrl);

function pickService(moduleName) {
  return mixedModuleModes[moduleName] === "http"
    ? httpServices[moduleName]
    : mockServices[moduleName];
}

export const appServices = (() => {
  if (apiMode === "http") {
    return httpServices;
  }

  if (apiMode === "mixed") {
    return {
      auth: pickService("auth"),
      dashboard: pickService("dashboard"),
      speaking: pickService("speaking"),
      vocabulary: pickService("vocabulary"),
      grammar: pickService("grammar"),
      profile: pickService("profile")
    };
  }

  return mockServices;
})();
