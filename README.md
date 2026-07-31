# English Learning Copilot

English Learning Copilot（英语学习协同助手）是一个面向英语学习者的 Web 应用。系统将场景化口语对话、词汇与语法练习、FSRS-6 间隔复习、每日学习计划和学习统计组织为一个可持续的学习闭环。

## 当前功能

- 场景化口语：旅行、日常、职场、自由对话和 IELTS Speaking Part 1/2/3/完整模拟等 22 个已配置场景；支持会话创建、浏览器录音、转写、逐轮 Agent 回复、历史回顾和综合反馈。
- 语音与智能能力：后端支持讯飞 ASR、ISE、TTS 与 OpenAI-compatible 对话模型的配置化接入；在没有外部服务凭证时，可使用 Mock 实现演示基础流程。
- 词汇学习：分级词卡、主动回忆题、收藏与个人单词本；用户以 Again、Hard、Good、Easy 四级自评后由 FSRS-6 计算后续复习时间。
- 语法学习：按主题练习、即时解析、错题本、收藏、FSRS-6 复习和基于可信题目上下文的语法 Tutor 问答。
- 学习计划与统计：每日词汇/语法目标、连续学习、推荐任务、周度概览和本地检索式学习内容推荐。

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 前端 | React 18、JavaScript、Vite、React Router、Ant Design |
| 后端 | Java 21、Spring Boot 3.4.5、Spring Data JPA、Spring Security、JWT |
| 数据 | MySQL（运行环境）、H2（自动化测试）、词汇/语法数据集 |
| 语音与 Agent | 可配置的讯飞 ASR/ISE/TTS 与 OpenAI-compatible 模型服务；支持 Mock 模式 |
| 测试 | JUnit/Spring Boot Test、Vitest、Testing Library、Playwright、k6 |

## 目录

```text
SE26Project-13/
├── frontend/           # React 前端、单元测试和 Playwright 测试
├── backend/            # Spring Boot 后端、JUnit 集成/单元测试
├── data/               # 词汇、语法与词典导入数据
├── docs/               # 设计、接口、FSRS、部署和性能说明
├── llm-prompt-lab/     # 口语场景与 Agent 提示词实验
├── performance/        # k6 性能测试脚本
├── scripts/            # 构建、启动与部署脚本
├── PPT/                # 评审答辩材料
├── TechPrototype/      # 架构、迭代和 UML 相关已有文档
└── FinalRelease/       # 最终提交物整理目录
```

## 快速启动

### 1. 启动数据库

运行环境默认使用 MySQL。创建数据库后，通过环境变量或未提交的本地配置设置连接信息：

```sql
CREATE DATABASE english_learning_copilot
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

```bash
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD='your-local-password'
export APP_JWT_SECRET='replace-with-a-local-random-secret-of-at-least-32-characters'
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

项目使用 Java 21。也可使用已有脚本：

```bash
bash scripts/start-backend.sh
```

后端的数据库、认证、管理员种子和测试说明见 [backend/README.md](backend/README.md)。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

默认访问地址由 Vite 输出。联调时，前端可通过 `VITE_API_MODE` 选择数据源：

```text
mock   # 全部使用前端本地 mock 数据
mixed  # 按模块混用 mock 与 Spring Boot 接口
http   # 全部使用 Spring Boot 接口
```

`frontend/.env.example` 给出了 mixed 模式的模块开关示例。

## 无外部 Key 的演示模式

完整语音/模型能力依赖外部服务，但项目可以在没有真实 API Key 时完成基础演示。提交物中的 [FinalRelease/SourceCode/backend/.env.example](FinalRelease/SourceCode/backend/.env.example) 默认配置为：

```text
SPEAKING_AGENT_PROVIDER=mock
XFYUN_ASR_ENABLED=false
XFYUN_ISE_ENABLED=false
SUPERSMART_TTS_ENABLED=false
```

真实服务仅应在本机或部署服务器的私有环境配置中设置，例如 `SJTU_AI_API_KEY`、`XFYUN_APP_ID`、`XFYUN_API_KEY`、`XFYUN_API_SECRET`。不要将有效凭证写入 `.env.example`、前端代码或 Git 提交。

## 测试

后端测试：

```bash
cd backend
mvn test
```

前端单元测试：

```bash
cd frontend
npm run test:run
```

口语端到端测试：

```bash
cd frontend
npm run test:e2e
```

性能测试脚本位于 `performance/k6/`，使用方式见 [performance/README.md](performance/README.md)。

## 主要文档

- [接口契约](docs/api-contracts.md)
- [口语 Agent 与语音能力设计](docs/oral-agent-design.md)
- [FSRS-6 算法说明](docs/FSRS-algorithm-view.md)
- [词汇 FSRS 实现说明](docs/FSRS-vocabulary-review-implementation.md)
- [语法 FSRS 实现说明](docs/FSRS-grammar-review-implementation.md)
- [部署说明](docs/server-deploy.md)
- [FinalRelease 源码副本](FinalRelease/SourceCode/)
