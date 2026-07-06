/**
 * Frontend/backend data contracts.
 *
 * Keep page components dependent on these interfaces instead of concrete mock
 * data or raw fetch responses. When Spring Boot APIs are ready, make their JSON
 * responses match these shapes, then switch VITE_API_MODE to "http".
 */
export interface QuickStat {
  label: string;
  value: string;
  icon: "microphone" | "waveform" | "streak" | "trend" | "clock";
}

export interface DashboardOverview {
  productTag: string;
  stackTag: string;
  headline: string;
  description: string;
  primaryActionLabel: string;
  secondaryActionLabel: string;
  quickStats: QuickStat[];
  focusIntensity: string;
  suggestedDuration: string;
}

/** One speaking scenario card shown on the speaking page. */
export interface Scenario {
  id: string;
  title: string;
  level: string;
  accent: string;
  duration: string;
  summary: string;
  tone: "blue" | "gold" | "mint";
}

/** Payload for the speaking module. Extend this when adding recording sessions. */
export interface SpeakingCatalog {
  modes: string[];
  scriptPreviewTitle: string;
  scriptPreviewLines: string[];
  scenarios: Scenario[];
}

export interface FeedbackMetric {
  key: string;
  label: string;
  value: string;
}

export interface FeedbackSummary {
  statusLabel: string;
  playbackActionLabel: string;
  metrics: FeedbackMetric[];
  notes: string[];
}

export interface VocabularyCard {
  id: string;
  word: string;
  usage: string;
  progress: number;
  tag: string;
}

export interface VocabularySnapshot {
  dailyGoal: string;
  retentionHint: string;
  cards: VocabularyCard[];
}

/** Grammar page payload. Examples should be ready to render directly. */
export interface GrammarTopic {
  id: string;
  title: string;
  summary: string;
  examples: string[];
  progress: number;
  tag: string;
}

export interface GrammarSnapshot {
  focus: string;
  topics: GrammarTopic[];
}

export interface PlanItem {
  id: string;
  time: string;
  task: string;
  meta: string;
  done: boolean;
}

export interface ProgressMetric {
  id: string;
  label: string;
  value: number;
  tone: "default" | "teal" | "gold";
}

export interface DailyPlan {
  autoPilotEnabled: boolean;
  weeklyImprovement: string;
  items: PlanItem[];
  progress: ProgressMetric[];
}

export interface ProfileSnapshot {
  learnerName: string;
  level: string;
  streak: string;
  feedback: FeedbackSummary;
  dailyPlan: DailyPlan;
}

/**
 * Service boundary consumed by pages.
 * Each member can own one service area and replace the mock implementation with
 * HTTP-backed behavior without changing page imports.
 */
export interface DashboardService {
  getOverview(): Promise<DashboardOverview>;
}

export interface SpeakingService {
  getCatalog(): Promise<SpeakingCatalog>;
}

export interface VocabularyService {
  getSnapshot(): Promise<VocabularySnapshot>;
}

export interface GrammarService {
  getSnapshot(): Promise<GrammarSnapshot>;
}

export interface ProfileService {
  getSnapshot(): Promise<ProfileSnapshot>;
}

export interface AppServices {
  dashboard: DashboardService;
  speaking: SpeakingService;
  vocabulary: VocabularyService;
  grammar: GrammarService;
  profile: ProfileService;
}
