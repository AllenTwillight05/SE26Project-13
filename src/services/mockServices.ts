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
