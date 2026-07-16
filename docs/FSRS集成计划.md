# FSRS 集成计划

## 目标

用 FSRS-6 替换"个性化学习计划"中死板的"每天推 N 题"逻辑，让每道词汇/语法题的复习间隔根据用户的实际记忆状态动态调整。

---

## 第一步：定义数据库表

### 1.1 现有词汇表不动

`vocabulary` 表的字段保持不变（id, word, phonetic, definition, chineseOptions, englishOptions 等）。

### 1.2 新增 user_word_progress

每个用户 × 每道题的 FSRS 记忆状态独立存储：

```sql
CREATE TABLE user_word_progress (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    question_id VARCHAR(64) NOT NULL,   -- 对应词汇表的 id 或语法题的 id
    question_type ENUM('vocabulary','grammar') NOT NULL,
    difficulty  DOUBLE DEFAULT 2.5,     -- D，范围 [1, 10]
    stability   DOUBLE DEFAULT 2.5,     -- S，天数
    interval    INT DEFAULT 0,          -- 距下次复习的天数
    reps        INT DEFAULT 0,          -- 总复习次数
    lapses      INT DEFAULT 0,          -- 遗忘次数
    state       TINYINT DEFAULT 0,      -- 0=New, 1=Review
    due         DATETIME NOT NULL,      -- 下次复习时间
    last_review DATETIME,               -- 上次复习时间
    UNIQUE KEY uk_user_question (user_id, question_id, question_type)
);
```

**加载今日任务：** `SELECT * FROM user_word_progress WHERE user_id=? AND due <= NOW()`

---

## 第二步：集成 Java 版 FSRS

### 2.1 文件位置

已写好的文件：`backend/src/main/java/com/se26project/fsrs/FSRS.java`

包含三个类：
- `FSRS` — 调度器，核心入口 `review(CardState, Rating)`
- `CardState` — 对应 `user_word_progress` 的字段
- `Rating` — Again(1) / Hard(2) / Good(3) / Easy(4)

### 2.2 对接方式

在 Service 层创建 `FSRS` 实例（单例），每次用户答题后调用：

```java
@Autowired
private UserWordProgressRepository progressRepo;

private final FSRS fsrs = new FSRS(0.9, FSRS.defaultParams());

public void submitAnswer(Long userId, String questionId, Rating rating) {
    CardState card = progressRepo.findByUserAndQuestion(userId, questionId)
        .map(this::toCardState)          // DB → CardState
        .orElse(new CardState());        // 新题，初始化

    CardState newState = fsrs.review(card, rating);

    // CardState → DB，更新 difficulty, stability, due 等字段
    progressRepo.save(toEntity(userId, questionId, newState));
}
```

---

## 第三步：前端适配

### 3.1 答题页加评分按钮

现在每一题答完后只有"下一题"按钮。改为四个按钮：

```
[再来一次] [有点难] [良好] [太简单]
   Again     Hard    Good    Easy
     1        2       3       4
```

点击任一按钮 → 调 POST `/api/vocabulary/review` 或 POST `/api/grammar/review`，传 rating=1~4。

### 3.2 学习计划页改为"今日待复习"

不再展示固定的"每日 N 题"，而是展示 FSRS 筛选出的待复习列表：

```
GET /api/learning-plan/today
-> { tasks: [...], today_total: 20, today_done: 8, retention_rate: 0.78 }
```

---

## 第四步：新 API 接口

### 4.1 提交评分

```
POST /api/vocabulary/review
Body: { "question_id": "purple", "rating": 3 }
Response: { "ok": true, "next_due": "2026-07-14T08:30:00" }
```

### 4.2 获取今日任务

```
GET /api/vocabulary/today
Response: { "cards": [ { "id": "purple", "word": "purple", ... }, ... ] }
```

### 4.3 获取统计

```
GET /api/stats/retention
Response: { "retention_rate": 0.78, "mastered": 156, "at_risk": 12, "today_total": 20 }
```

---

## 第五步：顺序

| 步骤 | 内容 | 依赖 |
|------|------|------|
| 1 | 创建 `user_word_progress` 表 | MySQL |
| 2 | 写 `UserWordProgress` Entity + Repository | Spring Boot JPA |
| 3 | 写 `ReviewService.submitAnswer()` | Entity |
| 4 | 写 `GET /api/vocabulary/today` | Entity |
| 5 | 前端改答题页：加四个评分按钮 | 后端 API |
| 6 | 前端改 Dashboard：显示今日待复习 | 后端 API |
| 7 | 前端改 Profile：显示记忆留存率 | 后端 API |

---

## 不需要做的事

- 不修改现有词汇表结构
- 不修改口语练习模块（口语题没有"对错"，不适合 FSRS）
- 不需要前端理解 FSRS 算法
