import { createHttpServices } from "./httpServices";
import { createMockServices } from "./mockServices";

// Data-source switch:
// - mock: local typed mock data for independent frontend development.
// - http: Spring Boot endpoints defined in endpoints.ts.
const apiMode = import.meta.env.VITE_API_MODE ?? "mock";
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "";

export const appServices =
  apiMode === "http" ? createHttpServices(apiBaseUrl) : createMockServices();
