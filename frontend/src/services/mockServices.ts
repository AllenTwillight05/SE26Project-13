import type { AppServices } from "./contracts";
import {
  dashboardOverviewMock,
  grammarSnapshotMock,
  profileSnapshotMock,
  speakingCatalogMock,
  vocabularySnapshotMock
} from "./mockData";

function simulateLatency<T>(value: T, delay = 120): Promise<T> {
  return new Promise((resolve) => {
    window.setTimeout(() => {
      resolve(structuredClone(value));
    }, delay);
  });
}

// Mock implementation used by default. Keep mock payloads close to contracts so
// frontend members can develop pages before backend endpoints are ready.
export function createMockServices(): AppServices {
  return {
    dashboard: {
      getOverview: () => simulateLatency(dashboardOverviewMock)
    },
    speaking: {
      getCatalog: () => simulateLatency(speakingCatalogMock)
    },
    vocabulary: {
      getSnapshot: () => simulateLatency(vocabularySnapshotMock)
    },
    grammar: {
      getSnapshot: () => simulateLatency(grammarSnapshotMock)
    },
    profile: {
      getSnapshot: () => simulateLatency(profileSnapshotMock)
    }
  };
}
