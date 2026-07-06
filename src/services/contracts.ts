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

export interface Scenario {
  id: string;
  title: string;
  level: string;
  accent: string;
  duration: string;
  summary: string;
  tone: "blue" | "gold" | "mint";
}

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
