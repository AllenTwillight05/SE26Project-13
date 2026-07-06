import { createHttpServices } from "./httpServices";
import { createMockServices } from "./mockServices";

const apiMode = import.meta.env.VITE_API_MODE ?? "mock";
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "";

export const appServices =
  apiMode === "http" ? createHttpServices(apiBaseUrl) : createMockServices();

