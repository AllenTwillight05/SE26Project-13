# API Contracts

本文件说明前端已经预留的 Spring Boot 接口。后端返回 JSON 需要对齐 `frontend/src/services/contracts.ts`。

默认前端使用 mock 数据。接入真实接口时，创建 `.env`：

```bash
VITE_API_MODE=http
VITE_API_BASE_URL=http://localhost:8080
```

## 1. 首页概览

`GET /api/dashboard/overview`

用途：渲染首页简介、行动按钮和今日概览。

核心结构：

```ts
interface DashboardOverview {
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
```

## 2. 口语模块

`GET /api/speaking/catalog`

用途：渲染口语页的练习模式、场景卡片和脚本预览。

核心结构：

```ts
interface SpeakingCatalog {
  modes: string[];
  scriptPreviewTitle: string;
  scriptPreviewLines: string[];
  scenarios: Scenario[];
}
```

后续可扩展接口：

- `POST /api/speaking/sessions`：创建口语练习会话。
- `POST /api/speaking/recordings`：上传录音。
- `GET /api/speaking/sessions/{id}/feedback`：获取口语反馈。

## 3. 词汇模块

`GET /api/vocabulary/snapshot`

用途：渲染词汇页卡片、复习目标和掌握度。

核心结构：

```ts
interface VocabularySnapshot {
  dailyGoal: string;
  retentionHint: string;
  cards: VocabularyCard[];
}
```

后续可扩展接口：

- `POST /api/vocabulary/review-queue`：加入复习队列。
- `PATCH /api/vocabulary/cards/{id}/progress`：更新掌握度。

## 4. 语法模块

`GET /api/grammar/snapshot`

用途：渲染语法页主题、例句和掌握度。

核心结构：

```ts
interface GrammarSnapshot {
  focus: string;
  topics: GrammarTopic[];
}
```

后续可扩展接口：

- `GET /api/grammar/topics/{id}`：查看语法主题详情。
- `POST /api/grammar/exercises/{id}/submit`：提交语法练习结果。

## 5. 个人模块

`GET /api/profile/snapshot`

用途：渲染个人页资料、学习计划、能力进度和最近反馈。

核心结构：

```ts
interface ProfileSnapshot {
  learnerName: string;
  level: string;
  streak: string;
  feedback: FeedbackSummary;
  dailyPlan: DailyPlan;
}
```

后续可扩展接口：

- `PATCH /api/profile`：更新用户资料。
- `POST /api/profile/plan/regenerate`：重新生成学习计划。
- `GET /api/profile/progress`：获取详细进度曲线。

## 6. 对接约定

- 前端页面只通过 `useAppServices()` 访问数据。
- 新增接口时，先更新 `contracts.ts`，再更新 `mockData.ts` 和 `httpServices.ts`。
- 后端字段命名建议使用 camelCase，与 TypeScript 契约保持一致。
- 错误处理后续集中放在 `frontend/src/services/httpClient.ts`。
- 鉴权、token、CSRF 等请求头也集中放在 `frontend/src/services/httpClient.ts`。
