// Spring Boot 接口路径统一放这里，页面和服务层不要散写 URL 字符串。
export const API_ENDPOINTS = {
  authRegister: "/api/auth/register",
  authLogin: "/api/auth/login",
  authMe: "/api/auth/me",
  authLogout: "/api/auth/logout",
  dashboardOverview: "/api/dashboard/overview",
  speakingCatalog: "/api/speaking/catalog",
  vocabularyMemory: "/api/vocabulary/memory",
  vocabularyPracticeProgress: "/api/vocabulary/practice-progress",
  vocabularyPracticeWords: "/api/vocabulary/practice-words",
  vocabularyWordbookWords: "/api/vocabulary/wordbook-words",
  vocabularySnapshot: "/api/vocabulary/snapshot",
  grammarNotebookQuestions: "/api/grammar/notebook-questions",
  grammarOverview: "/api/grammar/overview",
  grammarPracticeQuestions: "/api/grammar/practice-questions",
  grammarProgress: "/api/grammar/progress",
  grammarTopics: "/api/grammar/topics",
  grammarSnapshot: "/api/grammar/snapshot",
  profileSnapshot: "/api/profile/snapshot"
};
