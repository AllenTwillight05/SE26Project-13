import { createHttpServices } from "./httpServices";
import { createMockServices } from "./mockServices";

// 数据源切换入口：
// - mock：前端独立开发时默认走本地 mock。
// - http：联调 Spring Boot 时切到真实接口。
const apiMode = import.meta.env.VITE_API_MODE ?? "mock";
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "";

export const appServices =
  apiMode === "http" ? createHttpServices(apiBaseUrl) : createMockServices();
