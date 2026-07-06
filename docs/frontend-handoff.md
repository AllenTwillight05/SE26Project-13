# 前端交付说明

## 1. 当前交付范围

当前仓库已经具备可并行开发的前端基础框架：

- React + TypeScript + Vite 工程已搭建。
- Ant Design 已接入，并统一了基础主题。
- 页面已按产品入口拆分为：首页、口语、词汇、语法、个人。
- 每个页面都有独立路由和独立页面文件。
- 数据访问通过 `services` 层注入，页面不直接依赖 mock 或真实 HTTP。
- 已预留 Spring Boot 后端接口路径。
- 默认使用 mock 数据，便于前端成员独立开发。

这份交付适合作为团队前端分工起点，不代表完整业务功能已经完成。

## 2. 启动方式

```bash
npm install
npm run dev
```

默认访问地址以终端输出为准，通常是：

```text
http://127.0.0.1:4173/
```

构建验证：

```bash
npm run build
```

## 3. 页面与路由

| 页面 | 路由 | 文件 | 适合负责的成员 |
| --- | --- | --- | --- |
| 首页 | `/` | `src/pages/DashboardPage.tsx` | 框架/集成负责人 |
| 口语 | `/speaking` | `src/pages/SpeakingPage.tsx` | 口语练习负责人 |
| 词汇 | `/vocabulary` | `src/pages/VocabularyPage.tsx` | 词汇练习负责人 |
| 语法 | `/grammar` | `src/pages/GrammarPage.tsx` | 语法练习负责人 |
| 个人 | `/profile` | `src/pages/ProfilePage.tsx` | 个人中心/学习计划负责人 |

兼容旧路由：

- `/practice` 会重定向到 `/speaking`
- `/feedback` 会重定向到 `/profile`
- `/plan` 会重定向到 `/profile`

## 4. 目录结构

```text
src
├── app                  # 应用入口、Provider、Ant Design 主题
├── components           # 公共组件和导航组件
├── hooks                # 通用 Hook
├── layouts              # 全局布局
├── pages                # 五个产品页面
├── router               # 路由配置和导航配置
├── services             # 接口类型、mock 服务、HTTP 服务
└── styles.css           # 全局布局和业务视觉样式
```

重点文件：

- `src/router/routes.tsx`：页面路由。
- `src/router/navigation.tsx`：顶部导航。
- `src/services/contracts.ts`：前后端数据契约。
- `src/services/mockData.ts`：前端开发用 mock 数据。
- `src/services/httpServices.ts`：真实 HTTP 接口实现。
- `src/services/endpoints.ts`：后端接口路径。
- `src/app/theme.ts`：Ant Design 主题 token。
- `src/styles.css`：页面布局、卡片、响应式和 Apple 风格视觉。

## 5. 服务接口边界

页面通过 `useAppServices()` 获取服务，不直接写 `fetch`。

当前服务模块：

- `dashboard.getOverview()`：首页概览。
- `speaking.getCatalog()`：口语场景、练习模式、脚本预览。
- `vocabulary.getSnapshot()`：词汇卡片、掌握度、复习目标。
- `grammar.getSnapshot()`：语法主题、例句、掌握度。
- `profile.getSnapshot()`：个人信息、学习计划、能力进度、最近反馈。

成员开发页面时，优先改自己负责的页面和对应 service contract，不要把其他模块逻辑塞进首页。

## 6. 后端接口预留

接口路径集中在 `src/services/endpoints.ts`：

```text
/api/dashboard/overview
/api/speaking/catalog
/api/vocabulary/snapshot
/api/grammar/snapshot
/api/profile/snapshot
```

接 Spring Boot 时，后端返回 JSON 结构需要对齐 `src/services/contracts.ts`。

切换真实接口：

```bash
VITE_API_MODE=http
VITE_API_BASE_URL=http://localhost:8080
```

可以参考 `.env.example`。

## 7. 成员开发建议

口语负责人：

- 页面：`src/pages/SpeakingPage.tsx`
- 数据：`speaking.getCatalog()`
- 后续接口：场景列表、会话创建、录音上传、口语评分。

词汇负责人：

- 页面：`src/pages/VocabularyPage.tsx`
- 数据：`vocabulary.getSnapshot()`
- 后续接口：词卡列表、复习队列、掌握度更新。

语法负责人：

- 页面：`src/pages/GrammarPage.tsx`
- 数据：`grammar.getSnapshot()`
- 后续接口：语法主题、例句、题目、练习结果。

个人负责人：

- 页面：`src/pages/ProfilePage.tsx`
- 数据：`profile.getSnapshot()`
- 后续接口：学习计划、进度统计、最近反馈、用户资料。

框架负责人：

- 路由：`src/router/routes.tsx`
- 导航：`src/router/navigation.tsx`
- 主题：`src/app/theme.ts`
- 全局样式：`src/styles.css`

## 8. 交付检查清单

交付前至少确认：

- `npm install` 能成功安装依赖。
- `npm run dev` 能启动。
- `npm run build` 能通过。
- 首页只作为基础入口，不承载复杂业务。
- 五个页面路由都能打开。
- mock 数据能支持成员独立开发。
- 接口契约集中在 `contracts.ts`，后续便于和 Spring Boot 对齐。

## 9. 后续建议

- 接入登录后，再补全用户状态管理。
- 后端接口稳定后，可以用 OpenAPI 或接口表固化契约。
- 真实接口变多后，可以引入 TanStack Query 管理请求缓存。
- 口语实时反馈后续可扩展录音上传、波形展示和 WebSocket 流式反馈。
