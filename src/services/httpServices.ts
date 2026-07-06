import type { AppServices } from "./contracts";
import { API_ENDPOINTS } from "./endpoints";
import { getJson } from "./httpClient";

function withBaseUrl(baseUrl: string, path: string) {
  return `${baseUrl}${path}`;
}

export function createHttpServices(baseUrl = ""): AppServices {
  return {
    dashboard: {
      getOverview: () => getJson(withBaseUrl(baseUrl, API_ENDPOINTS.dashboardOverview))
    },
    speaking: {
      getCatalog: () => getJson(withBaseUrl(baseUrl, API_ENDPOINTS.speakingCatalog))
    },
    vocabulary: {
      getSnapshot: () => getJson(withBaseUrl(baseUrl, API_ENDPOINTS.vocabularySnapshot))
    },
    grammar: {
      getSnapshot: () => getJson(withBaseUrl(baseUrl, API_ENDPOINTS.grammarSnapshot))
    },
    profile: {
      getSnapshot: () => getJson(withBaseUrl(baseUrl, API_ENDPOINTS.profileSnapshot))
    }
  };
}
