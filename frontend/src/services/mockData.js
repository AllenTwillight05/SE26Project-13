export const dashboardOverviewMock = {
  productTag: "A6 口语学习助手",
  stackTag: "Spring Boot + React",
  headline: "更像私人教练，而不是练习题仓库。",
  description:
    "把情境训练、实时反馈、词汇巩固和个性化计划放进一个统一界面，让用户从“知道该学什么”直接走到“今天就能开口说”。",
  primaryActionLabel: "开始今天训练",
  secondaryActionLabel: "切换场景库",
  quickStats: [
    { label: "本周开口时长", value: "148 min", icon: "microphone" },
    { label: "发音准确率", value: "92%", icon: "waveform" },
    { label: "连续学习", value: "12 days", icon: "streak" }
  ],
  focusIntensity: "中高",
  suggestedDuration: "32 分钟"
};

export const speakingCatalogMock = {
  scenarios: [
    {
      id: "business-opening",
      title: "商务会谈",
      level: "B2",
      accent: "美式表达",
      duration: "18 min",
      summary: "训练礼貌开场、项目进度同步等职场对话技能。",
      tone: "blue",
      goal: "掌握得体的会议开场白，并学会主动推进议程与确认交付时间。",
      keywords: ["agenda", "clarify", "timeline", "follow up"],
      prompts: [
        {
          role: "coach",
          text: "Good morning. Could you briefly introduce today's agenda?"
        },
        {
          role: "learner",
          text: "Sure. Today I'd like to start with the project update, then discuss the delivery timeline."
        },
        {
          role: "coach",
          text: "Great. How would you clarify a delayed deadline politely?"
        },
        {
          role: "learner",
          text: "I would say: Could we confirm whether Friday is still realistic for delivery?"
        }
      ],
      feedback: {
        totalScore: 88,
        pronunciation: 91,
        fluency: 84,
        speed: "136 WPM",
        issueSentences: [
          "Could we confirm whether Friday is still realistic for delivery?"
        ],
        suggestions: [
          "clarify 的重音可以更清楚，放在第一个音节。",
          "长句中可以在 whether 后稍作停顿，听感会更稳。"
        ]
      }
    },
    {
      id: "airport-checkin",
      title: "机场值机与改签",
      level: "A2",
      accent: "出行必备",
      duration: "12 min",
      summary: "训练值机、改签沟通等旅行技能。",
      tone: "gold",
      goal: "完成值机、座位选择和请求改签的基础沟通。",
      keywords: ["check in", "boarding pass", "aisle seat", "reschedule"],
      prompts: [
        {
          role: "coach",
          text: "Hello. May I see your passport and booking reference?"
        },
        {
          role: "learner",
          text: "Of course. Here is my passport, and this is my booking reference."
        },
        {
          role: "coach",
          text: "Would you prefer a window seat or an aisle seat?"
        },
        {
          role: "learner",
          text: "I'd prefer an aisle seat if one is available."
        }
      ],
      feedback: {
        totalScore: 82,
        pronunciation: 86,
        fluency: 78,
        speed: "121 WPM",
        issueSentences: ["I'd prefer an aisle seat if one is available."],
        suggestions: [
          "available 末尾不要吞音，保持清楚收尾。",
          "旅行场景可以多使用 if one is available 这样的礼貌条件句。"
        ]
      }
    },
    {
      id: "dinner-smalltalk",
      title: "晚餐社交破冰",
      level: "B1",
      accent: "自然对话",
      duration: "15 min",
      summary: "训练轻松破冰、自然接话和巧妙追问。",
      tone: "mint",
      goal: "掌握轻松得体的开场白，并学会用追问保持对话的流畅度。",
      keywords: ["small talk", "recommend", "by the way", "sounds great"],
      prompts: [
        {
          role: "coach",
          text: "This restaurant is popular. Have you been here before?"
        },
        {
          role: "learner",
          text: "No, it's my first time here. Do you have any recommendations?"
        },
        {
          role: "coach",
          text: "The pasta is great. What kind of food do you usually enjoy?"
        },
        {
          role: "learner",
          text: "I usually enjoy simple food with fresh ingredients."
        }
      ],
      feedback: {
        totalScore: 85,
        pronunciation: 87,
        fluency: 86,
        speed: "128 WPM",
        issueSentences: ["Do you have any recommendations?"],
        suggestions: [
          "recommendations 的音节较多，可以放慢一点读。",
          "回答后加一句反问，会更像真实社交对话。"
        ]
      }
    },
    {
      id: "clinic-visit",
      title: "医院就诊沟通",
      level: "B1",
      accent: "医疗常用语",
      duration: "16 min",
      summary: "训练挂号、描述症状、取药等就医场景的英语沟通。",
      tone: "rose",
      goal: "能够清晰描述症状，理解医生的简单问诊并配合完成就诊流程。",
      keywords: ["symptom", "prescription", "pharmacy", "appointment"],
      prompts: [
        {
          role: "coach",
          text: "Good morning. What seems to be the problem today?"
        },
        {
          role: "learner",
          text: "I've had a sore throat and a headache for the past three days."
        },
        {
          role: "coach",
          text: "Have you taken any medication for it so far?"
        },
        {
          role: "learner",
          text: "I took some painkillers yesterday, but the headache came back this morning."
        }
      ],
      feedback: {
        totalScore: 80,
        pronunciation: 83,
        fluency: 79,
        speed: "118 WPM",
        issueSentences: ["I've had a sore throat and a headache for the past three days."],
        suggestions: [
          "sore throat 的 /θ/ 音要咬舌，不要读成 /t/。",
          "描述症状时长句可以拆短，先说主要不适再说持续时间。"
        ]
      }
    },
    {
      id: "store-return",
      title: "商场退换货",
      level: "A2",
      accent: "购物技能",
      duration: "13 min",
      summary: "训练退换货沟通、保修咨询等表达。",
      tone: "violet",
      goal: "掌握在商场进行退换货、询问保修政策和申请退款的基础对话。",
      keywords: ["receipt", "refund", "exchange", "warranty"],
      prompts: [
        {
          role: "coach",
          text: "Welcome. How can I help you today?"
        },
        {
          role: "learner",
          text: "I'd like to return this jacket. It's too small for me."
        },
        {
          role: "coach",
          text: "Do you have the receipt with you?"
        },
        {
          role: "learner",
          text: "Yes, here it is. Can I exchange it for a larger size?"
        }
      ],
      feedback: {
        totalScore: 83,
        pronunciation: 85,
        fluency: 81,
        speed: "124 WPM",
        issueSentences: ["I'd like to return this jacket. It's too small for me."],
        suggestions: [
          "exchange 的 /ks/ 组合不要吞音，清晰发出两个辅音。",
          "退换货时用 I'd like to 开头会让语气更礼貌。"
        ]
      }
    },
    {
      id: "cafe-ordering",
      title: "咖啡厅点单与社交",
      level: "A2",
      accent: "生活口语",
      duration: "11 min",
      summary: "训练咖啡厅点单时的简短社交。",
      tone: "orange",
      goal: "能够自信地在咖啡厅点单、提出个性化需求，并进行简单的熟人寒暄。",
      keywords: ["order", "regular", "take away", "catch up"],
      prompts: [
        {
          role: "coach",
          text: "Hi there! What can I get for you today?"
        },
        {
          role: "learner",
          text: "Hi! Could I have a medium latte with oat milk, please?"
        },
        {
          role: "coach",
          text: "Sure. Is that for here or to go?"
        },
        {
          role: "learner",
          text: "To go, please. And could you add a little extra foam?"
        }
      ],
      feedback: {
        totalScore: 86,
        pronunciation: 88,
        fluency: 85,
        speed: "132 WPM",
        issueSentences: ["Could I have a medium latte with oat milk, please?"],
        suggestions: [
          "latte 的重音在第一个音节，不要读成 la-TTE。",
          "点单时用 Could I have 比 I want 更自然礼貌。"
        ]
      }
    }
  ]
};

export const feedbackSummaryMock = {
  statusLabel: "实时采样中",
  playbackActionLabel: "语音回放",
  metrics: [
    { key: "accuracy", label: "发音准确性", value: "91%" },
    { key: "speed", label: "语速控制", value: "136 WPM" },
    { key: "fluency", label: "流利度", value: "8.7 / 10" }
  ],
  notes: [
    "把 /r/ 的卷舌力度再收一点，整体会更自然。",
    "回应速度很好，建议在长句里增加停顿层次。",
    "旅行问路场景词汇掌握稳定，可以升级到陌生城市导航。"
  ]
};

export const vocabularySnapshotMock = {
  dailyGoal: "今日目标 12 词",
  retentionHint: "预计留存率 83%",
  cards: [
    {
      id: "clarify",
      word: "clarify",
      usage: "Could you clarify the deadline?",
      progress: 84,
      tag: "高频表达"
    },
    {
      id: "reschedule",
      word: "reschedule",
      usage: "I'd like to reschedule the meeting.",
      progress: 67,
      tag: "会议场景"
    },
    {
      id: "itinerary",
      word: "itinerary",
      usage: "Let me check the travel itinerary.",
      progress: 58,
      tag: "旅行场景"
    }
  ]
};

export const vocabularyMemoryMock = {
  retentionLabel: "当前记忆留存率",
  retentionRate: 78,
  stats: [
    { value: "156 词", label: "已掌握" },
    { value: "12 词", label: "可能遗忘" },
    { value: "20 词", label: "今日待练" }
  ]
};

export const vocabularyPracticeProgressMock = {
  completed: 8,
  total: 20
};

export const vocabularyPracticeWordsMock = [
  {
    id: "purple",
    word: "purple",
    phonetic: "'pә:pl",
    definition: "n. a purple color or pigment\nn. of imperial status\nv. become purple\nv. color purple",
    briefTranslation: "紫色",
    translation: "n. 紫色, 帝位\na. 紫色的, 帝王的, 华而不实的\nv. (使)成紫色",
    collins: "2",
    oxford: "1",
    tag: "zk gk cet4 cet6 ky toefl",
    bnc: "5326",
    frq: "3894",
    exchange: "s:purples/d:purpled/i:purpling/p:purpled/3:purples/r:purpler/t:purplest",
    uk_audio: "https://dictionary.cambridge.org/media/english/uk_pron/u/ukp/ukpur/ukpurit004.mp3",
    us_audio: "https://dictionary.cambridge.org/media/english/us_pron/p/pur/purpl/purple.mp3",
    chineseOptions: ["紫色", "恶名昭著的", "旅行计划", "重新安排时间"],
    englishOptions: ["purple", "flagrant", "itinerary", "reschedule"]
  },
  {
    id: "flagrant",
    word: "flagrant",
    phonetic: "'fleigrәnt",
    definition: "s conspicuously and outrageously bad or reprehensible",
    briefTranslation: "恶名昭著的",
    translation: "a. 极端明显的, 不能容忍的, 恶名昭著的\n[法] 公然的, 罪恶昭彰的, 现行的",
    collins: "1",
    oxford: "",
    tag: "gre",
    bnc: "19033",
    frq: "18623",
    exchange: "",
    uk_audio: "https://dictionary.cambridge.org/media/english/uk_pron/u/ukf/ukfla/ukflacc010.mp3",
    us_audio: "https://dictionary.cambridge.org/media/english/us_pron/f/fla/flagr/flagrant.mp3",
    chineseOptions: ["恶名昭著的", "紫色", "登机口", "确认"],
    englishOptions: ["flagrant", "purple", "gate", "confirm"]
  }
];

export const vocabularyWordbookWordsMock = [
  {
    id: "purple",
    word: "purple",
    phonetic: "'pә:pl",
    definition: "n. a purple color or pigment\nn. of imperial status",
    briefTranslation: "紫色",
    tag: "cet4 cet6 toefl",
    us_audio: "https://dictionary.cambridge.org/media/english/us_pron/p/pur/purpl/purple.mp3",
    favorited: true
  },
  {
    id: "flagrant",
    word: "flagrant",
    phonetic: "'fleigrәnt",
    definition: "s conspicuously and outrageously bad or reprehensible",
    briefTranslation: "恶名昭著的",
    tag: "gre",
    us_audio: "https://dictionary.cambridge.org/media/english/us_pron/f/fla/flagr/flagrant.mp3",
    favorited: false
  },
  {
    id: "itinerary",
    word: "itinerary",
    phonetic: "ai'tinәrәri",
    definition: "n. a proposed route of travel",
    briefTranslation: "旅行计划",
    tag: "cet6 toefl ielts",
    us_audio: "",
    favorited: true
  }
];

export const grammarSnapshotMock = {
  focus: "本周重点：时态、从句与口语连接词",
  topics: [
    {
      id: "tense-shift",
      title: "时态切换",
      summary: "训练过去经历、当前状态和未来计划之间的自然切换。",
      examples: [
        "I used to work on design, but now I focus on product research.",
        "I was planning to leave, but the meeting got extended."
      ],
      progress: 72,
      tag: "高频口语"
    },
    {
      id: "conditionals",
      title: "条件句表达",
      summary: "掌握建议、假设和委婉表达，适合商务与旅行场景。",
      examples: [
        "If possible, I'd like to reschedule the call.",
        "If I were you, I would clarify the agenda first."
      ],
      progress: 64,
      tag: "表达升级"
    },
    {
      id: "linking",
      title: "连接词与过渡",
      summary: "让长句更有层次，减少中文式断句。",
      examples: [
        "That being said, we still need to check the budget.",
        "In that case, we can move the deadline to Friday."
      ],
      progress: 81,
      tag: "流利度"
    }
  ]
};

export const dailyPlanMock = {
  autoPilotEnabled: true,
  weeklyImprovement: "已提升 14%",
  items: [
    { id: "morning", time: "08:30", task: "早间跟读", meta: "节奏与连读", done: true },
    { id: "meeting", time: "14:00", task: "情境对话", meta: "商务会议", done: false },
    { id: "review", time: "20:30", task: "晚间复盘", meta: "错词回顾", done: false }
  ],
  progress: [
    { id: "fluency", label: "口语流利度", value: 78, tone: "default" },
    { id: "coverage", label: "场景覆盖度", value: 64, tone: "teal" },
    { id: "retention", label: "词汇留存率", value: 83, tone: "gold" }
  ]
};

export const profileSnapshotMock = {
  learnerName: "English Learner",
  level: "B1 -> B2",
  streak: "12 days",
  feedback: feedbackSummaryMock,
  dailyPlan: dailyPlanMock
};
