
DROP TABLE IF EXISTS grammar_questions;

CREATE TABLE grammar_questions (
    id INT PRIMARY KEY,
    question_text TEXT NOT NULL COMMENT '题目文本',
    option_a VARCHAR(500) NOT NULL COMMENT '选项A',
    option_b VARCHAR(500) NOT NULL COMMENT '选项B',
    option_c VARCHAR(500) NOT NULL COMMENT '选项C',
    option_d VARCHAR(500) NOT NULL COMMENT '选项D',
    option_e VARCHAR(500) DEFAULT NULL COMMENT '选项E（部分题有5选项）',
    answer CHAR(1) NOT NULL COMMENT '正确答案(A/B/C/D/E)',
    grammar_category VARCHAR(50) NOT NULL COMMENT '语法类别',
    explanation TEXT COMMENT '答案解析'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='语法选择题题库';


INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    1,
    'Mr Wang made up his mind to devote all he could ______ his oral English before going abroad.',
    'improve',
    'to improve',
    'improving',
    'to improving',
    NULL,
    'D',
    '介词与固定搭配',
    'devote...to 中 to 是介词，后接动名词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    2,
    'Everything he ______ away from him before he returned to his hometown.',
    'took',
    'had been taken',
    'had had been taken',
    'had taken',
    NULL,
    'C',
    '时态与语态',
    'before 引导的从句中，主句动作先发生时用过去完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    3,
    'Before he went abroad， he spent as much time as he _____ English.',
    'could learning',
    'learned',
    'to learn',
    'could learn',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    4,
    'You can never imagine what great difficuly I have ______ your house.',
    'found',
    'finding',
    'to find',
    'for finding',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    5,
    'The person we spoke to ______ no answer at first.',
    'making',
    'makes',
    'make',
    'made',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    6,
    'The person we referred to ______ us a report tomorrow.',
    'giving',
    'will give',
    'gave',
    'give',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    7,
    'The days we have been looking forward to _______ soon.',
    'coming',
    'will come',
    'came',
    'have come',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    8,
    'The person we talked about ______ our school last week.',
    'visiting',
    'will visit',
    'visited',
    'has visited',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    9,
    'The man whose songs we are fond of ______ in our city next week.',
    'singing',
    'to sing',
    'will sing',
    'sang',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    10,
    'Not only ______ the jewelry she _____ been sold for her son''s gambling debts but also her house.',
    'is； has',
    'has； had',
    'has； has',
    '不填； has',
    NULL,
    'C',
    '从句',
    'Not only 置于句首时，该分句需要部分倒装（助动词提前）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    11,
    '______ in thought， he almost ran into the car in front of him.',
    'Losing',
    'Having lost',
    'Lost',
    'To lose',
    NULL,
    'C',
    '非谓语动词',
    '过去分词作状语或定语，主语与分词之间是被动关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    12,
    'The research is so designed that once nothing can be'' done to change it.',
    'begins',
    'having begun',
    'beginning',
    'begun',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    13,
    '— What do you think made the woman so upset？ — _______ weight.',
    'As she put on',
    'Put on',
    'Putting on',
    'Because of putting on',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    14,
    'Time should be made good use of ______ our lessons well.',
    'learning',
    'learned',
    'to learn',
    'having learned',
    NULL,
    'C',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    15,
    'It was only with the help of the local guide ______.',
    'was the mountain climber rescued',
    'then the mountain climber was rescued',
    'when the mountain climber was rescued',
    'that the mountain climber was rescued',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    16,
    'Never ______ time come again.',
    'has lost',
    'will lose',
    'will lost',
    'lose',
    NULL,
    'C',
    '从句',
    '否定词 Never 置于句首时，句子需要部分倒装。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    17,
    '— ______ was it ______ they discovered the entrance to the underground palace？ — Totally by chance.',
    'What； that',
    'How； that',
    'When； how',
    'Where； that',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    18,
    'I have nothing to confess. ______ you want me to say？',
    'What is it that',
    'What it is what',
    'How is it that',
    'How it is that',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    19,
    'Is this factory ______ you visited the other day？',
    'the one',
    'that',
    'where',
    'when',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    20,
    'Was it _____ she heard with her ears really made her frightened？',
    'How foolishly',
    'How foolish',
    'What foolishly',
    'What foolish',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    21,
    'It was ______ the old clock that the old man spent the whole morning at home.',
    'repair',
    'repairing',
    'to repair',
    'in repair',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    22,
    'Is this hotel ______ you said we were to stay in your letter？',
    'that',
    'where',
    'the one',
    'in which',
    NULL,
    'B',
    '从句',
    '定语从句中缺少地点状语时用关系副词 where。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    23,
    'Please tell me the way you thought of ______ the garden.',
    'take care of',
    'to take care of',
    'takinq care of',
    'how to take care of',
    NULL,
    'B',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    24,
    'A fast-food restaurant is the place _______， just as the name suggests， eating is performed quickly.',
    'which',
    'where',
    'there',
    'what',
    NULL,
    'B',
    '从句',
    '定语从句中缺少地点状语时用关系副词 where。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    25,
    'The film brought the hours back to me ______ I was taken good care of in that faraway village.',
    'until',
    'that',
    'when',
    'where',
    NULL,
    'C',
    '从句',
    '定语从句中缺少时间状语时用关系副词 when。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    26,
    'The professor has written another book，________ of great importance to cornputer science.',
    'which I think it is',
    'and I think is',
    'which I think is',
    'when I think is',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    27,
    '— Where do you think ______ he ______ the computer？ — Sorry， I have no idea.',
    'had； bought',
    'has； bought',
    'did； buy',
    '不填； bought',
    NULL,
    'D',
    '从句',
    'do you think 作插入语时，特殊疑问句用陈述语序。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    28,
    'We should do more such exercises in the future， I think， _____ those we did yesterday.',
    'as',
    'like',
    'about',
    'than',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    29,
    'He will tell you _____ he expects will win such a match.',
    'why',
    'whom',
    'which',
    'who',
    NULL,
    'D',
    '从句',
    '定语从句修饰人时用 who，作主语；作宾语时用 whom。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    30,
    'In New Zealand， I made lots of friends _____ a very practical knowledge of the English language.',
    'get',
    'toget',
    'getting',
    'got',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    31,
    'I''m busy now. I''m sorry I can''t help _____ the flowers.',
    'watedng',
    'watered',
    'waters',
    'to water',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    32,
    'Who would you rather ______ the report instead of you？',
    'have write',
    'have to write',
    'write',
    'have written',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    33,
    'We must stop pollution ______ longer.',
    'living',
    'from living',
    'to live',
    'live',
    NULL,
    'C',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    34,
    '— Was it under the tree ______ you were away talking to a friend？ — Sure. But when I got back there， the bike was gone.',
    'that',
    'where',
    'which',
    'while',
    NULL,
    'D',
    '时态与语态',
    'when 引导的时间状语从句用一般过去时，主句根据需要选用时态。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    35,
    'Not far from the club there was a garden， ______ owner seated in it playing bridge with his children every afternoon.',
    'whose',
    'its',
    'which',
    'that',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    36,
    'Wang Ling was elected ______ all he is the tallest.',
    'because',
    'because of',
    'for',
    'as',
    NULL,
    'B',
    '介词与固定搭配',
    'because of + 名词/名词性成分，表示原因。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    37,
    'We''ll be free tomorrow， so I suggest ______ to the history museum.',
    'to visit',
    'visiting',
    'we should visit',
    'a visit',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    38,
    'I like swimming， while what my brother enjoys ______.',
    'cooking',
    'to cook',
    'is cooking',
    'cook',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    39,
    'Thank you for the trouble you have ______ to help me.',
    'paid',
    'taken',
    'had',
    'asked',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    40,
    'Who is it up _______ decide whether to goor not？',
    'to to',
    'for for',
    'to for',
    'for to',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    41,
    'We keep in touch ______ writing often.',
    'with',
    'of',
    'on',
    'by',
    NULL,
    'D',
    '介词与固定搭配',
    'keep in touch with sb 与某人保持联系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    42,
    '— How long have you been here？ — ______ the end of last month，',
    'In',
    'By',
    'At',
    'Since',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    43,
    'You should treat him (in) the way ______ suits him most.',
    'that',
    'in which',
    '不填',
    'why',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    44,
    'He insisted that the sky ______ clear up the following day.',
    'would',
    'should',
    '不填',
    'be',
    NULL,
    'A',
    '情态动词与虚拟语气',
    'insist 作''坚持认为''时用陈述语气，作''坚持要求''时用虚拟语气 (should do)。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    45,
    'He is a strict but kind-hearted father， ______ the children respect but are afraid of.',
    '不填',
    'that',
    'for whom',
    'one whom',
    NULL,
    'D',
    '从句',
    '定语从句中关系代词作宾语时用 whom（宾格）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    46,
    'Mr Smith is ______ a good teacher ______ we all respect.',
    'such；that',
    'such； as',
    'so；that',
    'so； as',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    47,
    '______ nice， the food was all eaten up soon.',
    'Tasting',
    'Taste',
    'Tasted',
    'To taste',
    NULL,
    'A',
    '非谓语动词',
    '此处需要动名词 (doing) 作宾语或介词宾语。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    48,
    '— You haven''t been to Beijing， have you？ —______. And how I wish to go there again.',
    'Yes， t have',
    'Yes， I haven''t',
    'No， I have',
    'No， I haven''t',
    NULL,
    'A',
    '情态动词与虚拟语气',
    'wish 后接虚拟语气，与现在相反用 did/were，与过去相反用 had done。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    49,
    'He was sentenced to death ______ what he has stolen from the bank.',
    'that',
    'since',
    'because',
    'because of',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    50,
    'Joseph _____Wharton, along with his many successful business ventures, has_____ helped shape the history of American industry.',
    '[NO CHANGE]',
    'Wharton, including his many successful business ventures, have',
    'Wharton, along with his many successful business ventures, have',
    'Wharton and his many successful business ventures has',
    NULL,
    'A',
    '主谓一致',
    '主语后有 with/together with/as well as 等插入语时，谓语与前面的主语保持一致。插入语不影响主语的数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    51,
    'Project SCORE was the first communications satellite ever put into Earth''s orbit, and _____it will be launched_____ on December 18, 1958.',
    '[NO CHANGE]',
    'it was launched',
    'they were launched',
    'it is launched',
    NULL,
    'B',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    52,
    'Despite the group''s many musical successes, _____they are struggling_____ to afford new instruments and gas money for the van.',
    '[NO CHANGE]',
    'they struggle',
    'it is struggling',
    'he and she are struggling',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    53,
    'An ocean mammal, such as the blue whale, _____behaves quite differently_____ than mammals that live on land.',
    '[NO CHANGE]',
    'behave quite differently',
    'behaves quite different',
    'has behaviors quite differently',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    54,
    'The "desert rally," an endurance race by dune buggies across hostile terrain, _____that only the fiercest competitors qualify for._____',
    '[NO CHANGE]',
    'which only the fiercest competitors can qualify for.',
    'and only the fiercest competitors qualify for it.',
    'is a contest that only the fiercest competitors qualify for.',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    55,
    '_____While it panted and strained at its leash, the dog''s owner attempted to restrain the puppy_____ when it saw a squirrel at the dog park.',
    '[NO CHANGE]',
    'The dog''s owner attempted to restrain the puppy while it panted and strained at its leash',
    'While it panted and strained at its leash, the puppy''s owner attempted to restrain it',
    'The dog''s owner, while it panted and strained at its leash, attempted to restrain the puppy',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    56,
    'The small house built in the field used the _____average_____ building materials that were available in the region: wood, stone, brick, and glass.',
    '[NO CHANGE]',
    'arbitrary',
    'stereotypical',
    'characteristic',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    57,
    'Beethoven''s music is known for being logical, yet emotive; challenging, yet occasionally simple; and unique, yet _____derived from previous styles._____',
    '[NO CHANGE]',
    'previous styles influenced it.',
    'was derived from previous styles.',
    'it was derived from previous styles.',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    58,
    'The intrepid explorers of the wilderness discovered a new _____river, they also found_____ a pair of mountains they named "Dipyramid."',
    '[NO CHANGE]',
    'river, found',
    'river; they also find',
    'river and found',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    59,
    'Cyclone Althea devastated portions of northern Australia in December 1971; the strong winds ripped roofs off houses, hail damaged vehicles and windows, _____and it cost_____ the Australian federal government close to a billion dollars in modern-day adjusted values.',
    '[NO CHANGE]',
    'and they cost',
    'and those cost',
    'and the storm cost',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    60,
    'There are several activities that I like to practice _____every day; motorcycling,_____ running, and songwriting.',
    '[NO CHANGE]',
    'every day: motorcycling,',
    'every day, motorcycling,',
    'every day. Motorcycling,',
    NULL,
    'B',
    '时态与语态',
    'every day 表示经常性动作，用一般现在时。被动语态结构：be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    61,
    'During his college interview, Jose was _____confused by_____ several questions, including one about his favorite types of animals, and another that asked, "if you were a flavor of ice cream, what flavor would you be?"',
    '[NO CHANGE]',
    'confused at',
    'confused with',
    'confused for',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    62,
    'Though the thought seems humorous now, we - both my friends and I - dreamed _____of becoming a garbage-truck driver_____ when we were much younger.',
    '[NO CHANGE]',
    'of becoming garbage-truck drivers',
    'to become a garbage-truck driver',
    'to become garbage-truck drivers',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    63,
    'Each swimmer should check that _____they have_____ goggles, a swimsuit, and a towel before boarding the team''s bus.',
    '[NO CHANGE]',
    'it has',
    'he or she has',
    'he or she have',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    64,
    'Whoever our next mayor might be, _____I believe that he or she_____ absolutely must protect our citizens from any further violence.',
    '[NO CHANGE]',
    'they',
    'I believe that it',
    'I believe that they',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    65,
    'If nobody in the house is going to eat that pasta, _____they ought to_____ refrigerate the leftovers so that the food stays fresh.',
    '[NO CHANGE]',
    'they should',
    'someone should',
    'he or she ought to',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    66,
    'Alone on the prairie, one often begins to feel that _____they have_____ a special connection to the desolate landscape.',
    '[NO CHANGE]',
    'it has',
    'one has',
    'we have',
    NULL,
    'C',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    67,
    'Many people, after touring the medieval fair, _____become an apprentice_____ to blacksmiths or jewelry-makers.',
    '[NO CHANGE]',
    'be an apprentice',
    'become apprentices',
    'choose to be an apprentice',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    68,
    'One of my classmates thinks that _____their parents are_____ the most productive and generous members of society who ever lived.',
    '[NO CHANGE]',
    'his parents are',
    'their parents is',
    'one''s parents are',
    NULL,
    'B',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    69,
    'What any particular antique violin _____is worth is_____ determined by its age and maker.',
    '[NO CHANGE]',
    'are worth is',
    'is worth are',
    'are worth are',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    70,
    'All of the watches in our collection, which is comprised of timepieces from distant _____centuries, is highly valued_____by connoisseurs of horology.',
    '[NO CHANGE]',
    'centuries, it is highly valued',
    'centuries, are highly valued',
    'centuries, being highly valued',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    71,
    'Someone is at the door, but I am unaware of what purpose _____they might be_____ visiting us for at this late hour.',
    '[NO CHANGE]',
    'they are',
    'they could be',
    'he or she might be',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    72,
    'Each runner will have a lane on the track, which has recently been resurfaced, to _____themselves_____ in order to minimize accidents.',
    '[NO CHANGE]',
    'oneself',
    'him or her',
    'himself or herself',
    NULL,
    'D',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    73,
    'As I have taught my little sister more and more about soccer, _____she and me spend more time_____ kicking the ball around outside.',
    '[NO CHANGE]',
    'she and I have spent more time',
    'she and I will spend more time',
    'her and me have spent more time',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    74,
    'On our travels through Germany, Raphael, Bill, and _____me met both weary_____ travelers and excited explorers.',
    '[NO CHANGE]',
    'me met both wearily',
    'I met both weary',
    'I met both wearily',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    75,
    '_____Us students are_____ leading a solidarity march in order to protest the unfair treatment of migrant farm workers.',
    '[NO CHANGE]',
    'Ourselves are',
    'Us students were',
    'We students are',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    76,
    'When I saw Kate jumping on the trampoline, I asked _____my friends and she to be careful_____, for the springs were old and worn-out.',
    '[NO CHANGE]',
    'them',
    'she and my friends to be careful',
    'my friends and her to be careful',
    NULL,
    'D',
    '时态与语态',
    '主语与动词之间是被动关系，用被动语态 be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    77,
    '_____My puppy and I go_____ to the dog park as often as possible; I love to watch his rambunctious antics as he chases other friendly dogs and plays in the river.',
    '[NO CHANGE]',
    'I go',
    'Me and my puppy go',
    'My puppy and me go',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    78,
    'Annabelle is an old acquaintance of our family; _____my brother and her may_____ even get married someday.',
    '[NO CHANGE]',
    'she and he',
    'her and my brother',
    'she and my brother',
    NULL,
    'D',
    '代词与限定词',
    'other 泛指''其他的''，others = other people。the other 指两者中的另一个。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    79,
    'When Debbie answers the phone, _____she always responds "this is her."_____',
    '[NO CHANGE]',
    'she always responds "this is she."',
    'her always responds "this is she."',
    'she always responding "this is she."',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    80,
    'To a great artist such _____as he, both_____ oil painting and charcoal sketching come quite naturally.',
    '[NO CHANGE]',
    'as his, both',
    'as him, both',
    'like him, both',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    81,
    'He throws the ball accurately; she catches it consistently; together, _____he and she make_____ a remarkably well-rounded team.',
    '[NO CHANGE]',
    'he and her make',
    'he and she makes',
    'he and her makes',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    82,
    'The bear chased Xander and _____me while Martha''s climbing skills allowed her to perch_____ safely in a tree.',
    '[NO CHANGE]',
    'me to allow Martha''s climbing skills to perch her',
    'I allowing Martha''s climbing skills to perch her',
    'I while Martha''s climbing skills allowed her to perch',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    83,
    'Playing _____the way us Wildcats do_____ is sure to guarantee victory in the tournament.',
    '[NO CHANGE]',
    'our way, us Wildcats',
    'in our own way, we Wildcats',
    'the way we Wildcats do',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    84,
    'Concert audiences in the 1800s did not have access to recording technology and might only hear a song once in their entire life; _____for they, an encore_____ was their only chance to hear a piece of music for a second time.',
    '[NO CHANGE]',
    'for these, an encore',
    'for them, an encore',
    'an encore, for these,',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    85,
    'The science institute was located in picturesque surroundings, _____but the students are able_____ to relax among the lakes and fields after classes.',
    '[NO CHANGE]',
    'so the students are able',
    'though the students are able',
    'this allows the students',
    NULL,
    'B',
    '时态与语态',
    '主语与动词之间是被动关系，用被动语态 be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    86,
    'Dangerous Dan was a fictional detective and the star of many popular mystery _____movies; however,_____ the actors that have played Dan are now worth millions of dollars.',
    '[NO CHANGE]',
    'Movies; despite that,',
    'movies; in fact,',
    'movies, and yet,',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    87,
    'An "Iltizam" was a 17th century Egyptian farmland that earned the wealthy landowner a great deal of _____profit; therefore, he left_____ only pennies to the peasants who actually cultivated the farm.',
    '[NO CHANGE]',
    'profit, to leave',
    'profit, but left',
    'profit, nor left',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    88,
    'The Federal Trade Commission was established years ago to protect the American economy, _____because since then it has_____ ended many unfair monopolies.',
    '[NO CHANGE]',
    'because in that time it has',
    'and since then they have',
    'and since that time it has',
    NULL,
    'D',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    89,
    'Ludwig van Beethoven may have been the greatest Viennese composer in _____history, and he_____ almost missed his destiny when deafness threatened to end his musical career.',
    '[NO CHANGE]',
    'history, he',
    'history, so he',
    'history, but he',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    90,
    'An excellent wrestler, David Tsinakuridze won the Olympic gold medal, _____although his coach_____ was concerned about the athlete''s recently- injured knee.',
    '[NO CHANGE]',
    'for his coach',
    'for he and his coach',
    'though he and his coach',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    91,
    'You may know this simple device only as a "crowbar"; _____furthermore,_____ in previous ages it was known as a "handspike."',
    '[NO CHANGE]',
    'indeed,',
    'however,',
    'in rebuttal,',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    92,
    'Because mountain-climbing is an arduous and exhausting activity with serious risk of _____injury_____, since many mild-mannered people are unwilling to even attempt it.',
    '[NO CHANGE]',
    'injury,',
    'injury, so',
    'injury, and',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    93,
    'Some motorcycles can appear to be quite intricate in their design, _____but they all share_____ the same basic concept of unification between frame, wheels, and engine.',
    '[NO CHANGE]',
    'but it all shares',
    'and they all share',
    'furthermore, they all share',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    94,
    'Charlie Daniels will be remembered as the songwriter of "Devil Went Down to Georgia," _____therefore, although he wrote_____ many other tunes, none of them have remained popular.',
    '[NO CHANGE]',
    'therefore, writing',
    'but, although he wrote',
    'and since he wrote',
    NULL,
    'C',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    95,
    '_____Although_____ these people run the risk of landing in unexpected places, aerial thrill-seekers, such as skydivers and hang-glider pilots, should carry a GPS locator and two-way radio.',
    '[NO CHANGE]',
    'Since',
    'Despite the fact that',
    'Indeed,',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    96,
    'Charlie often enjoyed conversing with Veruca, whose controversial opinions always fascinated _____him; however,_____ he was sometimes offended when she vehemently disagreed with his own views.',
    '[NO CHANGE]',
    'him; moreover,',
    'him, and',
    'him, being that',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    97,
    'Songbirds often have to defend their nests from intruders that might prey on their _____eggs, so sometimes_____ the birds will share nesting grounds with other woodland creatures such as squirrels or opossums.',
    '[NO CHANGE]',
    'eggs; in addition,',
    'eggs, while sometimes',
    'eggs; nevertheless, sometimes',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    98,
    'David was surprised to learn that frogs were primarily _____carnivorous, he had assumed_____ that they mostly ate plants and vegetables.',
    '[NO CHANGE]',
    'carnivorous, he had assumed,',
    'carnivorous (he had assumed',
    'carnivorous; he had assumed',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    99,
    'A synthesizer - an entirely electronic musical _____instrument: can_____ usually be categorized into either an "East Coast" or a "West Coast" design philosophy.',
    '[NO CHANGE]',
    'instrument, can',
    'instrument - can',
    'instrument; can',
    NULL,
    'C',
    '主谓一致',
    'either...or 连接并列主语时，谓语与 or 后面的主语保持就近一致。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    100,
    'There are a wide variety of modern dance forms and styles to _____enjoy. That''s_____ one reason my hometown has many different dance studios.',
    '[NO CHANGE]',
    'enjoy that''s',
    'enjoy, that''s',
    'enjoy (that''s',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    101,
    'A professional long-distance _____runners_____ life revolves around his or her workout schedule, sleep routine, and dietary needs.',
    '[NO CHANGE]',
    'runner',
    'runner''s',
    'runners''',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    102,
    'Though the words "vintage" and "classic" are often applied interchangeably, there is a subtle _____difference: "vintage"_____ has a primary reference to age, while "classic" can add an additional connotation of elegance.',
    '[NO CHANGE]',
    'difference, "vintage"',
    'difference ("vintage"',
    'difference, so "vintage"',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    103,
    'Musicians should carefully craft a strategy for their online _____streaming - the ability_____ for their music to be heard instantly via the internet through a computer or mobile device) to achieve maximum popularity.',
    '[NO CHANGE]',
    'streaming (the ability',
    'streaming. The ability',
    'streaming; the ability',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    104,
    'Alvin is struggling in math class _____this year; and there are_____ many topics in Precalculus that he finds both challenging and frustrating.',
    '[NO CHANGE]',
    'this year (there are',
    'this year, there are',
    'this year; there are',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    105,
    'Surfing (riding the waves on a wooden or plastic board) gained _____much of its'' popularity_____ in the 1950s and 60s in Hawaii, California, and Australia.',
    '[NO CHANGE]',
    'much of its popularity',
    'much of it''s popularity',
    'much of their popularity',
    NULL,
    'B',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    106,
    'The art exhibit''s brochure read like a "who''s who" of famous Italian _____artists;Michelangelo_____, Raphael, Leonardo da Vinci…',
    '[NO CHANGE]',
    'artists,Michelangelo',
    'artists:Michelangelo',
    'artists (Michelangelo',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    107,
    'Anyone who has had _____siblings - younger_____ or older, brother or sister) will know that they are, at various times, a source of both great joy and great frustration.',
    '[NO CHANGE]',
    'a sibling. Younger',
    'a sibling, younger',
    'a sibling (younger',
    NULL,
    'D',
    '时态与语态',
    'now that 引导原因状语从句，可用现在完成时表示刚完成的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    108,
    'When working at the kennel, Samantha was often astonished at the sheer variety of _____the dogs'' collars_____, including spiked ones, simple ones, and colorful ones.',
    '[NO CHANGE]',
    'the dogs'' collar',
    'the dog''s collars',
    'the dogs collars',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    109,
    'Brian Eno is the foremost composer of _____ambient music (a genre_____ based in slow, evolving soundscapes that can be either ignored in the background or attended to carefully, as the listener may determine for himself or herself.',
    '[NO CHANGE]',
    'ambient music; a genre',
    'ambient music - a genre',
    'ambient music. A genre',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    110,
    'Coffee, a beverage that many entrepreneurs depend on, _____it is not only stimulating and delicious, but also comforting._____',
    '[NO CHANGE]',
    'stimulating, delicious, and comforting.',
    'is not only stimulating and delicious, but also comforting.',
    'because it is not only stimulating and delicious, but also comforting.',
    NULL,
    'C',
    '从句',
    'Not only 置于句首时，该分句需要部分倒装（助动词提前）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    111,
    'The people of Qalif frequently follow their family _____trade, they work_____ mainly in fishing, oil, and education.',
    '[NO CHANGE]',
    'trade;',
    'trade working',
    'trade, working',
    NULL,
    'D',
    '非谓语动词',
    '此处需要动名词 (doing) 作宾语或介词宾语。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    112,
    '_____A worldwide authority on horticulture, Gerald Holmes, after training at the finest academies in France._____',
    '[NO CHANGE]',
    'A worldwide authority on horticulture, Gerald Holmes trained at the finest academies in France.',
    'A worldwide authority on horticulture; Gerald Holmes trained at the finest academies in France.',
    'Gerald Holmes, who trained at the finest academies in France, becoming a worldwide authority on horticulture.',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    113,
    'The Trenton Thunder is a Minor League _____baseball team, it is_____ from Trenton, New Jersey.',
    '[NO CHANGE]',
    'baseball team, they are',
    'baseball team',
    'baseball team; they are',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    114,
    'In 1686, at the age of only eight, Heinrich Gustav was appointed captain in the German _____army, he was_____ a prince and was therefore required to serve in the armed forces.',
    '[NO CHANGE',
    'army;',
    'army; he was',
    'army, who was',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    115,
    'I one met a Harvard math professor, considered preeminent in his _____field; and_____ at the forefront of economic analysis.',
    '[NO CHANGE]',
    'field, and was',
    'field, who was',
    'field, he was',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    116,
    'Carnegie-Mellon _____University, which has one of the finest computer science programs in the nation, and_____ its students are recruited by top companies every year.',
    '[NO CHANGE]',
    'University: which has one of the finest computer science programs in the nation, and',
    'University, because it has one of the finest computer science programs in the nation,',
    'University has one of the finest computer science programs in the nation, and',
    NULL,
    'D',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    117,
    'St.Michael''s Church, in Longstation, England, is an example of what is called a "redundant" _____church, it is_____ no longer used for regular church services.',
    '[NO CHANGE]',
    'church: it is',
    'church; they are',
    'church, the church is',
    NULL,
    'B',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    118,
    'Eddie Mottau is an American _____guitarist who has played_____ on albums written by innovators such as John Lennon and Yoko Ono.',
    '[NO CHANGE]',
    'guitarist, he played',
    'guitarist, he has played',
    'guitarist, played',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    119,
    'Llyn Conwy is a picturesque lake in north Wales, _____many tourists and families visit it_____ each year.',
    '[NO CHANGE]',
    'many tourists and families visiting it',
    'which many tourists and families visit',
    'where many tourists and families visit it',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    120,
    'Departing the port of Boston in 1913, the _____battleship cruised the U.S. coast, protected_____ merchant fleets.',
    '[NO CHANGE]',
    'battleship, cruised the U.S. coast, protected',
    'battleship cruised the U.S. coast and protecting',
    'battleship cruised the U.S. coast and protected',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    121,
    '_____The warm summer sun that radiated onto earth, and_____ the birds sang while dragonflies hovered all around us.',
    '[NO CHANGE]',
    'The warm summer sun radiated onto earth, and',
    'The warm summer sun radiated onto earth; and',
    'The summer sun, it radiated warmth onto the earth, and',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    122,
    'The modern-day tennis player, who has access to rackets enhanced by technological innovations, _____and who is able to_____ set new world records more easily than players of the past.',
    '[NO CHANGE]',
    'and can',
    'is able to',
    'they can',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    123,
    'Mt. Austin, which offers a beautiful view from the summit, is in _____Australia, and is_____ a popular tourist attraction.',
    '[NO CHANGE]',
    'Australia is',
    'Australia, is',
    'Australia, which is',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    124,
    'CAT scans are a medical procedure used to peer inside the human brain without _____surgery; they are commonly_____ used to diagnose concussions.',
    '[NO CHANGE]',
    'surgery; commonly',
    'surgery, they are commonly',
    'surgery and it is commonly',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    125,
    '_____Amidst the majestic hills of South Dakota, where iron ore was once mined, that young sparrows once frolicked._____',
    '[NO CHANGE]',
    'Amidst the majestic hills of South Dakota; iron ore was once mined and young sparrows once frolicked.',
    'Where iron ore was once mined amidst the majestic hills of South Dakota: now young sparrows frolic.',
    'Young sparrows once frolicked, and iron ore was once mined, amidst the majestic hills of South Dakota.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    126,
    'In an era of smartphones and text-messaging, one may find it difficult to find a moment of _____silence; at times our technology_____ can seem like a burden.',
    '[NO CHANGE]',
    'silence, it',
    'silence, our technology',
    'silence, at times our technology',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    127,
    'Though often misunderstood, SAT question- writers actually have the best of _____intentions, want to help you clarify your written and spoken communication._____',
    '[NO CHANGE]',
    'intentions; wanting to help with you clarifying your written and spoken communication.',
    'intentions, they want to help you clarify your communication, both written and spoken.',
    'intentions, and want to help you clarify your communication, both written and spoken.',
    NULL,
    'D',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    128,
    'Any great opera depends upon a well-written libretto, the text that will be _____sung, requires, in most cases, the_____ services of a second author who is not the composer of the music.',
    '[NO CHANGE]',
    'sung; requiring, in most cases, the',
    'sung, which, in most cases, requiring',
    'sung, requiring, in most cases, the',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    129,
    '_____Automated book-summary technology, programmers believe, may soon be available,_____ so some investors are buying stocks in related new companies.',
    '[NO CHANGE]',
    'Automated book-summary technology may soon be available,',
    'Programmers, believing that automated book-summary technology may soon be available, and',
    'Programmers, who believe that automated book-summary technology may soon be available, so',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    130,
    'A rural airstrip presents a pilot with two primary difficulties: _____to land on dirt, and avoiding_____ dangerous crosswinds.',
    '[NO CHANGE]',
    'landing on dirt and avoiding',
    'landing on dirt and the avoidance of',
    'the landing of the plane on dirt, and avoiding',
    NULL,
    'B',
    '非谓语动词',
    '此处需要动名词 (doing) 作宾语或介词宾语。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    131,
    'I prefer my Japanese history class to _____sitting and listening to American history_____.',
    '[NO CHANGE]',
    'American history',
    'my American history class',
    'sitting in American history class',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    132,
    'Getting an article published can be quite tedious; once written, it must still be _____edited and reviewed_____ before it is distributed.',
    '[NO CHANGE]',
    'edited and pass review',
    'reviewed and will be edited',
    'reviewed and they will edit it',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    133,
    'Though wax candles pose a risk of fire hazard, due to their open flames, they generate a light that is more relaxing than _____electric candles_____.',
    '[NO CHANGE]',
    'electric ones',
    'an electric candle',
    'that of electric candles',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    134,
    '_____To put berries in your breakfast cereal is one healthy idea;_____ putting bananas in your oatmeal is another.',
    '[NO CHANGE]',
    'To put berries in your breakfast cereal is one healthy idea;',
    'Putting berries in one''s breakfast cereal is one healthy idea;',
    'Putting berries in your breakfast cereal is one healthy idea;',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    135,
    'Unlike _____the_____ flu, people who suffer from the common cold may only have to deal with a headache and a congested nose for twenty-four hours.',
    '[NO CHANGE]',
    'having the',
    'people with the',
    'when you have the',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    136,
    'The website''s stated mission was "_____to preserve the internet, and we will record_____ illegal online censorship."',
    '[NO CHANGE]',
    'the preserving of the internet and to record',
    'to preserve the internet and to record',
    'to preserve the internet and keeping records of',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    137,
    'My friend makes electronic _____music by sampling classic tunes and combining them_____ into a remix.',
    '[NO CHANGE]',
    'music by sampling classic tunes and combines them',
    'music, samples classic tunes, combines them',
    'music with samples of classic tunes and combining them',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    138,
    'This omelet calls for more ingredients than any other recipe''s instructions._____omelet calls for more ingredients than any other recipe''s instructions._____',
    '[NO CHANGE]',
    'omelet''s recipe calls for more ingredients than any other recipe.',
    'omelet, calling for more ingredients than any other recipe''s instructions.',
    'omelet calls for more ingredients than the instructions of any other recipe.',
    NULL,
    'B',
    '代词与限定词',
    'other 泛指''其他的''，others = other people。the other 指两者中的另一个。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    139,
    '_____To keep her life private and protecting_____ her family, the pop star wore masks and costumes during her live shows.',
    '[NO CHANGE]',
    'To keep her life private, protect',
    'Keeping her life private and to protect',
    'To keep her life private and protect',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    140,
    'The hands and feet of chimpanzees are far more versatile and dexterous than _____humans_____.',
    '[NO CHANGE]',
    'humans are',
    'that of humans',
    'those of humans',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    141,
    'This calculator''s _____processing power is greater than that of_____ the computers that NASA used in the 1960s to land a man on the moon.',
    '[NO CHANGE]',
    'processes more great than',
    'processing power is greater than',
    'processing power is greater than those of',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    142,
    'Kishore Kumar was a multitalented man who was mainly known as a singer, actor, _____and wrote screenplays_____.',
    '[NO CHANGE]',
    'screenwriter',
    'for his screenplays',
    'he wrote screenplays',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    143,
    'Having read many of both authors'' novels, I prefer those of Ernest Hemingway to _____Victor Hugo_____.',
    '[NO CHANGE]',
    'those of Victor Hugo',
    'Victor Hugo''s writing',
    'what Victor Hugo wrote',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    144,
    'Teeth-grinding, fidgeting, and _____excessive energy_____ are a few negative side effects of overconsumption of energy drinks.',
    '[NO CHANGE]',
    'to have excess energy',
    'to be overly-energetic',
    'more than enough energy',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    145,
    'Hibernation, the act of resting and conserving energy when food is scarce, is _____a behavior more common to bears than_____ most other mammals.',
    '[NO CHANGE]',
    'more like bears than',
    'more common to bears than to',
    'more common to bears than to the behavior of',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    146,
    'There are many famous architects who began as furniture designers, interior decorators, or worked in construction._____worked in construction._____',
    '[NO CHANGE]',
    'in construction.',
    'construction workers.',
    'who worked in construction.',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    147,
    'Human infants, unlike _____the young_____ of other species, are born without yet knowing the basic instincts that are necessary for their survival.',
    '[NO CHANGE]',
    'with',
    'that of',
    '[DELETE the underlined portion]',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    148,
    'The length of this completed essay, almost eighteen pages long, is far more impressive than _____that_____ poorly-researched rough draft.',
    '[NO CHANGE]',
    'a',
    'this',
    'the length of that',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    149,
    'For our feature on your achievements, would you like to be interviewed online, in the newspaper, or _____captured on video_____?',
    '[NO CHANGE]',
    'on video',
    'a video interview',
    'interviewed on video',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    150,
    'Strict vegans monitor more than just their eating habits; they refuse to use any animal products for clothing or _____to decorate_____.',
    '[NO CHANGE]',
    'decoration',
    'decorate with them',
    'use them for decoration',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    151,
    'More citizens of New York City (with its affordable subway network) utilize public transportation than _____those of any_____ other city.',
    '[NO CHANGE]',
    'any',
    'that of any',
    'the public transportation of any',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    152,
    'One minor tragedy of life is that you must choose to follow only a single path rather than _____going down_____ all possible roads.',
    '[NO CHANGE]',
    'down',
    'go down',
    'following down',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    153,
    'If you are planning a solo wilderness adventure, some important necessities are a compass, a map, a supply of water, and to tell people where you are going._____a compass, a map, a supply of water, and to tell people where you are going._____',
    '[NO CHANGE]',
    'a compass, a map, water, and to tell people where you are going.',
    'having: a compass, a map, a supply of water, and telling people where you are going.',
    'to tell people where you are going and to bring a compass, a map, and a supply of water.',
    NULL,
    'D',
    '从句',
    '定语从句中缺少地点状语时用关系副词 where。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    154,
    'The newest models of seismographs, which are scientific devices designed to measure the movements of tectonic plates, are now more sophisticated than _____previous eras_____.',
    '[NO CHANGE]',
    'it used to be',
    'eras once were',
    'the seismographs of previous eras',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    155,
    'Ear-piercing has many social functions; it not only is a sign of rebellion, but also is an indicator of fashion sense or _____demonstrates belonging_____.',
    '[NO CHANGE]',
    'belonging',
    'demonstrated belonging',
    'to demonstrate belonging',
    NULL,
    'B',
    '从句',
    'Not only 置于句首时，该分句需要部分倒装（助动词提前）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    156,
    'The nighthawk is a bird that eats primarily insects, _____much like the diet of many other birds around the world_____.',
    '[NO CHANGE]',
    'as do many other birds around the world',
    'much like the diet of many other birds around the world',
    'much like what many other birds around the world eat',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    157,
    '_____Because they eat mosquitoes, many home owners have_____ considered building bird houses in their backyards.',
    '[NO CHANGE]',
    'Because birds eat mosquitoes, many home owners have',
    'Home owners, because they eat mosquitoes, have',
    'Because mosquitoes are eaten by birds, many home owners have',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    158,
    '_____Buffeted by strong, gusty winds, the skyscraper''s tenants felt the building_____ leaning to and fro.',
    '[NO CHANGE]',
    'The skyscraper''s tenants, buffeted by strong, gusty winds, felt the building',
    'As the building was buffeted by strong, gusty winds, the skyscraper''s tenants could feel it',
    'When buffeted by strong, gusty winds, the skyscraper''s tenants felt the building',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    159,
    '_____As the crowd''s favorite athlete, Janet Lynn''s figure-skating earned_____ cheers and applause that surely swayed the judge''s opinion.',
    '[NO CHANGE]',
    'As the crowd''s favorite athlete, Janet Lynn figure-skating earned',
    'As the crowd''s favorite athlete, the figure skating of Janet Lynn earned',
    'Janet Lynn''s figure-skating, as the crowd''s favorite athlete, earned',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    160,
    '_____Green workers, known for their focus on the environmental impact of large projects, are in high demand._____',
    '[NO CHANGE]',
    'Known for their focus on the environmental impact of large projects, demad is high for so-called "green" workers.',
    'Known for focusing on the environmental impacts of large projects, there is a high demand for so-called "green" workers.',
    'Green workers, known for their focus on large projects and its environmental impact, are in high demand.',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    161,
    '_____Chilly in the winter, a space heater would make this basement more comfortable._____',
    '[NO CHANGE]',
    'Chilly in the winter, space-heating would make this basement more comfortable.',
    'A space heater would make this basement, chilly in the winter, more comfortable.',
    'A space heater, chilly in the winter, would make this basement more comfortable.',
    NULL,
    'C',
    '词汇与逻辑',
    'win one''s trust = 赢得信任。catch/achieve/receive 不搭配 trust。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    162,
    '_____Formed while its members were in college, the four-piece band toured all over Europe._____',
    '[NO CHANGE]',
    'Formed while its members were in college, Europe was toured all over by the four- piece band.',
    'The four-piece band toured all over Europe, formed while its members were in college.',
    'All over Europe, formed while its members were in college, the four-piece band toured.',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    163,
    '_____Using GPS technology, getting lost was easily dealt with by the savvy traveler._____',
    '[NO CHANGE]',
    'Using GPS technology, the savvy traveler''s getting lost was easily dealt with.',
    'The savvy traveler using GPS technology, easily dealt with getting lost.',
    'Using GPS technology, the savvy traveler easily dealt with getting lost.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    164,
    '_____Standing in Northumberland County, invading armies were never able to breach the thick stone walls of Warkworth Castle._____',
    '[NO CHANGE]',
    'Invading armies were never able to breach the thick stone walls of Warkworth Castle, which stands in Northumberland County.',
    'Invading armies, never breached the thick stone walls of Warkworth Castle, standing in Northumberland County.',
    'Invading armies, standing in Northumberland County, were never able to breach the thick stone walls of Warkworth Castle.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    165,
    '_____Founded in 1968, attendees of the Clarion Workshop are taught to improve their science- fiction writing._____',
    '[NO CHANGE]',
    'Founded in 1968, improving their science- fiction writing is the goal of attendees of the Clarion Workshop.',
    'The Clarion Workshop is attended by those, founded in 1968, who are taught to improve their science-fiction writing.',
    'Attendees of the Clarion Workshop, which was founded in 1968, are taught to improve their science-fiction writing.',
    NULL,
    'D',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    166,
    '_____As the race car reached speeds of over 200 miles per hour, its engine noise became_____ nearly deafening to the spectators.',
    '[NO CHANGE]',
    'As it reached speeds of over 200 miles per hour, the race car''s engine noise became',
    'Reaching speeds of over 200 miles per hour, the race car''s engine noise became',
    'The race car''s engine noise, reaching speeds of over 200 miles per hour, became',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    167,
    '_____An author of passionately nationalistic novels, Carl Gustaf Verner''s writing depicts Swedish life and traditions._____',
    '[NO CHANGE]',
    'Authoring passionately nationalistic novels, Carl Gustaf Verner''s writing depicts Swedish life and traditions.',
    'Carl Gustaf Verner, an author of passionately nationalistic novels, depicts Swedish life and traditions in his writing.',
    'Authoring passionately nationalistic novels, Swedish life and traditions were depicted by Carl Gustaf Verner''s writing.',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    168,
    '_____Studying intensively, the Department of Public Administration was where Felix received an excellent education._____',
    '[NO CHANGE]',
    'The Department of Public Administration, studying intensively, was where Felix received an excellent education.',
    'Studying intensively, at the Department of Public Administration was where Felix received an excellent education.',
    'Studying intensively, Felix received an excellent education at the Department of Public Administration.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    169,
    '_____Agreeing after hours of debate, the decision of the jury was to let the citizen go without legal charges._____',
    '[NO CHANGE]',
    'The jury''s decision, agreeing after hours of debate, was to let the citizen go without legal charges.',
    'The citizen, agreeing after hours of debate, was let go without legal charges by the jury.',
    'The jury, agreeing after hours of debate, decided to let the citizen go without legal charges.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    170,
    'Finding contemporary classical music "arrogant," _____Lutz Glandien''s music embraces rock influences and older styles alike._____',
    '[NO CHANGE]',
    'Lutz Glandien''s music is influenced by both rock and older styles.',
    'Lutz Glandien embraces rock influences and older styles alike in his music.',
    'rock influences and older styles alike are embraced by Lutz Glandien''s music.',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    171,
    '_____There is nothing more annoying than trying to work while a fly buzzes behind your ear._____',
    '[NO CHANGE]',
    'There is nothing more annoying than a fly buzzing behind your ear, trying to work.',
    'There is nothing more annoying than a fly buzzing behind your ear while trying to work.',
    'A fly buzzing behind your ear: there is nothing more annoying while trying to work.',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    172,
    '_____The abilities of squirrels, running across power lines and racing up even the tallest trees, have always amazed me._____',
    '[NO CHANGE]',
    'Running across power lines and racing up even the tallest trees, the abilities of squirrels have always amazed me.',
    'Squirrels have always amazed me with their ability to run across power lines and race up even the tallest trees.',
    'Running across power lines and racing up even the tallest trees, squirrels'' abilities have always amazed me.',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    173,
    '_____Born in 1810, Robert Schumann''s life is a fascinating tale of_____ a composer driven to madness by schizophrenia.',
    '[NO CHANGE]',
    'Born in 1810, the life of Robert Schumann is a fascinating tale',
    'Born in 1810, the tale of Robert Schumann''s life is',
    'Born in 1810, Robert Schumann lived a life that makes for a fascinating tale',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    174,
    'The mythical painting was said to draw magical power from both its _____age and its master''s skills, dead for over 900 years._____',
    '[NO CHANGE]',
    'age and, dead for over 900 years, the skills of its master.',
    'age and the skills of its master, who had been dead for over 900 years.',
    'master''s skills, dead for over 900 years, and its age.',
    NULL,
    'C',
    '时态与语态',
    'for + 时间段常与现在完成时连用，表示从过去持续到现在的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    175,
    '_____The road racer was given a citation racing too close to other bicyclists in the race._____',
    '[NO CHANGE]',
    'The road racer was given a citation for racing too close to other bicyclists in the race.',
    'A citation, racing too close to other bicyclists in the race, was given to the road racer.',
    'Racing too close to other bicyclists in the race, a citation was given to the road racer.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    176,
    '_____Experimenting in a new kind of land partnership, the farmers turned the land into a cooperative cropland after purchasing it in 1804._____',
    '[NO CHANGE]',
    'After purchasing the land in 1804, the farmers turned it into a cooperative cropland experimenting in a new kind of land partnership.',
    'The farmers turned the land, experimenting in a new kind of land partnership, into a cooperative cropland after purchasing it in 1804.',
    'Experimenting in a new kind of land partnership, the land purchased in 1804 was turned into a cooperative cropland by the farmers.',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    177,
    '_____Though she is known as a murderer, the newspaper reports that the suspect may actually have had good intentions._____',
    '[NO CHANGE]',
    'The newspaper reports that, although she is known as a murderer, the suspect may actually have had good intentions.',
    'The newspaper, though she is known as a murderer, reports that the suspect may actually have had good intentions.',
    'Though she is known as a murderer, good intentions may actually have been had by the suspect, the newspaper reports.',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    178,
    '"The Cloisters" is a museum in New York City that is constructed of four covered walkways from Europe - _____named the Cuxa, Bonnefont, Trie, and Saint-Guilhem, respectively_____ - that were disassembled and transported to American soil between 1934 and 1939.Which of the following choices provides the most relevant and useful supporting details for the sentence above?',
    '[NO CHANGE]',
    'a continent located in the Northern Hemisphere of the globe',
    'cloisters is another word for "covered walkways"',
    'moved from their places of origin',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    179,
    'The use of robots on farms is growing rapidly, since it is often _____more cost-effective than_____ human farm workers.',
    '[NO CHANGE]',
    'more cost-effective than the use of',
    'more cost-effective than using',
    'less costly than',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    180,
    '[1] The Japanese battleship Nagato was built in 1920 as the most powerful capital ship in the Imperial Japanese Navy. [2] Her earliest noteworthy service was carrying supplies for the survivors of the Great Kanto earthquake in 1923. [3] Nagato then participated in the Second Sino-Japanese War of 1937. [4] Now, the wreck of the Nagato is considered one of the top ten shipwreck-diving spots in the world. [5] She remained in service during World War 2 and was involved in the surprise attack on Pearl Harbor as the flagship of Admiral Isoroku Yamamoto. [6] Impressively, Nagato was the only Japanese battleship to survive the entirety of World War 2. [7] In 1946, the massive vessel was destroyed (with no sailors on board) in the American nuclear weapons tests named "Operation Crossroads" held at Bikini Atoll. To make this paragraph most logical, sentence 4 should be placed',
    'where it is now.',
    'before sentence 1.',
    'after sentence 5.',
    'after sentence 7.',
    NULL,
    'D',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    181,
    'The soldier successfully pulled his ally from the _____helicopter wreck, and since the_____ flames engulfed their craft, he was unable to repair the vehicle.',
    '[NO CHANGE]',
    'helicopter wreck, the',
    'wreck of the helicopter, with the consequence that',
    'helicopter wreck, but because',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    182,
    'The Vauxhall _____automobile, being designed_____ to be economical, has recently regained popularity among car collectors.',
    '[NO CHANGE]',
    'automobile being designed',
    'automobile, designed',
    'automobile, a car designed',
    NULL,
    'C',
    '非谓语动词',
    '过去分词作状语或定语，主语与分词之间是被动关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    183,
    'Is it right that _____us workers must suffer due to_____ incompetent management?',
    '[NO CHANGE]',
    'we workers must suffer despite',
    'us workers must suffer despite',
    'we workers must suffer due to',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    184,
    'Of these, polyethylene is the most common type of plastic, accounting for 34% of the world''s total plastics market. It has a startlingly-wide range of applications; for example, ultra-high- density polyethylene is used in hip joint replacements, industrial shipping, and ice- skating rinks. [A] _____On the other end of the spectrum, low-density polyethylene is often used for plastic wraps, tubing, and food storage._____ In between these "high-density" and "low- density" uses, polyethylene can also be found in film applications, water plumbing, milk jugs, garbage containers, and children''s toys.The writer is considering adding the following sentence as an introduction to the paragraph above. Plastics are one of the most common and useful construction materials in modern global industry, and are produced in a wide range of varieties. Should the writer make this addition here?',
    'Yes, because the sentence provides an essential supporting example to the paragraph.',
    'Yes, because the sentence effectively introduces the main topic of the paragraph.',
    'No, because the sentence contradicts a central point made within the paragraph.',
    'No, because the following sentence ("Of these, polyethylene is…") already provides a more effective introduction to the paragraph.',
    NULL,
    'B',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    185,
    'Of these, polyethylene is the most common type of plastic, accounting for 34% of the world''s total plastics market. It has a startlingly-wide range of applications; for example, ultra-high- density polyethylene is used in hip joint replacements, industrial shipping, and ice- skating rinks. [A] _____On the other end of the spectrum, low-density polyethylene is often used for plastic wraps, tubing, and food storage._____ In between these "high-density" and "low- density" uses, polyethylene can also be found in film applications, water plumbing, milk jugs, garbage containers, and children''s toys.At Point [A], the writer is considering deleting the underlined sentence. Should the writer make this change?',
    'Yes, because it provides an excessive list of examples for a term that has already been defined.',
    'Yes, because it interrupts the overall flow of the paragraph.',
    'No, because it provides effective support for an essential counterargument within the paragraph.',
    'No, because it provides examples that are used as a key point for a comparison within the paragraph.',
    NULL,
    'D',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    186,
    'Our football team, currently in the running for the championship, and our school tennis star, who has dominated her recent _____matches, is putting us_____ in the running for a record season this year.',
    '[NO CHANGE]',
    'matches, was putting us',
    'matches, are putting us',
    'matches, were putting us',
    NULL,
    'C',
    '主谓一致',
    '集合名词作主语时，强调整体用单数，强调成员用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    187,
    '_____The pine tree, known for its majestic appearance and sweet scent, and its needles can be used to brew tea._____',
    '[NO CHANGE]',
    'The pine tree, known for its majestic appearance and sweet scent, has needles that can be used to brew tea.',
    'Known for its majestic appearance and sweet scent, the pine tree''s needles can be used to brew tea.',
    'The pine tree, its needles can be used to brew tea, is known for its majestic appearance and sweet scent.',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    188,
    'Neither the duck _____nor the goose is able to_____ enjoy long periods of time without a nearby body of water.',
    '[NO CHANGE]',
    'nor the goose',
    'or the goose is able to',
    'nor the goose are able to',
    NULL,
    'A',
    '主谓一致',
    'either...or 连接并列主语时，谓语与 or 后面的主语保持就近一致。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    189,
    'I think that most Japanese manufacturers'' cars get better mileage and are more reliable than _____American automakers_____.',
    '[NO CHANGE]',
    'making cars in America',
    'American automakers'' cars',
    'what they make in America',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    190,
    'Still undecided, the financial committee _____deliberated_____ who they would send to speak to the president of the company about the decreased profits this year.',
    '[NO CHANGE]',
    'authorized',
    'nominated',
    'delegated',
    NULL,
    'A',
    '主谓一致',
    '集合名词作主语时，强调整体用单数，强调成员用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    191,
    'Upon attempting to enter the military base, the reporter was _____tested_____ by the guard, who called out a simple request for identification.',
    '[NO CHANGE]',
    'dared',
    'challenged',
    'taxed',
    NULL,
    'C',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    192,
    'My grandparents fell deeply in love with each other from the moment they met; my grandpa, in particular, always described feeling _____attentive to_____ my grandma from the first time he saw her.',
    '[NO CHANGE]',
    'enamored with',
    'armored with',
    'concerned with',
    NULL,
    'B',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    193,
    'As a political science major writing a thesis paper on the behavior of crowds on voting day, I _____followed_____ the elections with excitement and interest.',
    '[NO CHANGE]',
    'stalked',
    'imitated',
    'came after',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    194,
    'Despite the best efforts of a federal team of business experts and politicians, the economic _____miniaturization_____ continued to affect the lives of millions of citizens.',
    '[NO CHANGE]',
    'procession',
    'lessening',
    'recession',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    195,
    'When in the grips of a major seizure, a patient''s body and limbs may _____trample_____ uncontrollably.',
    '[NO CHANGE]',
    'tremble',
    'wobble',
    'vibrate',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    196,
    'When traveling through an area with many pickpockets, regardless of police presence and other security measures, you must stay attentive - no one can _____warranty_____ the safety of your belongings better than yourself.',
    '[NO CHANGE]',
    'underwrite',
    'swear',
    'guarantee',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    197,
    'Only the most _____isolated_____ classical pianists will earn renown in the modern era, for the popularity of classical music has been waning as the audiences for rock and rap music have grown.',
    '[NO CHANGE]',
    'freakish',
    'exceptional',
    'abnormal',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    198,
    'Although the world traveler had seen many impressive sights, she could not decide whether the stone ruins of Machu Picchu were more _____confounding then_____ the colossal Great Pyramid at Giza.',
    '[NO CHANGE]',
    'confounding than',
    'astonishing then',
    'astonishing than',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    199,
    'To _____demolish_____ the volume of human trafficking across the border, the government implemented new security measures, but this did not complete eliminate the problem.',
    '[NO CHANGE]',
    'knock down',
    'diminish',
    'raze',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    200,
    'The warring tribes competed for land and resources throughout history, but neither tribe was ever able to completely _____conquer_____ the small island.',
    '[NO CHANGE]',
    'defeat',
    'trounce',
    'overcome',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    201,
    'When restoring a vintage car or motorcycle, it is wise to proceed carefully and slowly to avoid breaking the original parts that can no longer be _____superseded_____.',
    '[NO CHANGE]',
    'replaced',
    'returned',
    'swapped',
    NULL,
    'B',
    '非谓语动词',
    '过去分词作状语或定语，主语与分词之间是被动关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    202,
    'Generally stopping short of outright blackmail or violence, the mobster was famous for publicly making _____veiled_____ threats towards witnesses who planned to testify against him.',
    '[NO CHANGE]',
    'secretive',
    'concealed',
    'hidden',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    203,
    'When babysitting an infant, a sitter must be _____frugal with_____ the needs of the child, especially the need for food, security, and sleep.',
    '[NO CHANGE]',
    'shrewd about',
    'attentive to',
    'thrifty with',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    204,
    'The bank robbers are still at large, but the police are carefully tracking _____there_____ whereabouts.',
    '[NO CHANGE]',
    'they''re',
    'their',
    'its',
    NULL,
    'C',
    '主谓一致',
    'police/people/cattle 等集合名词通常视为复数，谓语用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    205,
    'Groups of elite actors working on a film together must control their _____egos_____ to prevent fights from breaking out among prima donnas.',
    '[NO CHANGE]',
    'characters',
    'self-images',
    'roles',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    206,
    'Ever since I accidentally offended my boss in the staff meeting yesterday, he has been treating me in a _____freezing_____ manner and ignoring all of my attempts to apologize.',
    '[NO CHANGE]',
    'frigid',
    'subzero',
    'rigid',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    207,
    '_____Although initial success can be rewarding, the feeling of satisfaction can promptly_____ wear off quickly.',
    '[NO CHANGE]',
    'the first initial success can be rewarding, the feeling of satisfaction can',
    'initial success can be rewarding, the feeling of satisfaction can rapidly',
    'initial success can be rewarding, the feeling of satisfaction can',
    NULL,
    'D',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    208,
    '_____The safety of bridges, keeping them from becoming dangerous, is overseen by government departments, but_____ unfortunately, the spans occasionally suffer abrupt structural failure and collapse.',
    '[NO CHANGE]',
    'Government departments oversee bridge safety, but',
    'Bridge safety, being overseen by government departments, is crucial, but',
    'The safety of bridges is being overseen by government departments, but',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    209,
    '_____This fine wool sweater was once being knitted by my grandmother, and it_____ has always been among my favorite articles of clothing.',
    '[NO CHANGE]',
    'My grandmother knitted this fine wool sweater, and it',
    'This fine wool sweater was knitted by my grandmother, and she',
    'This fine wool sweater was knitted and made by my grandmother, and it',
    NULL,
    'B',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    210,
    'There was an immediate and _____massive and tremendous public outcry against the dictator''s proclamation._____',
    '[NO CHANGE]',
    'massive public outcry and popular complaint against the dictator''s proclamation.',
    'ferocious public outcry against the dictator''s proclamation.',
    'tremendous public outcry against the dictator''s proclamation when he declared it.',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    211,
    '_____The teacher taught a lesson that_____ cleared up any misconceptions about the nature of space travel.',
    '[NO CHANGE]',
    'Being taught by the teacher, a lesson',
    'A lesson was taught by the teacher that',
    'The teacher taught and instructed the class in a lesson that',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    212,
    'When _____a budget is stuck to by you, you_____ will find yourself with more money left in your pocket at the end of each month.',
    '[NO CHANGE]',
    'a budget is being stuck to, you',
    'you stick to a budget, you',
    'you stick to a financial budget, you',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    213,
    'Sangita Santosham, a respected vocalist, _____has multiple genres of singing that are known by her._____',
    '[NO CHANGE]',
    'can sing in multiple genres.',
    'has the ability to sing in multiple different genres.',
    'knows how to sing in multiple genres and categories of singing.',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    214,
    'Perhaps some superstitions arose from the belief that after _____a disaster first occurred, it would repeat itself_____ under similar circumstances.',
    '[NO CHANGE]',
    'a disaster first occurred, it would repeat itself again',
    'an initial disaster first occurred, it would repeat itself',
    'a troublesome disaster first occurred, it would repeat itself',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    215,
    '_____The bicycle was pedaled by him furiously as he tried to catch up to the bus that had left without him._____',
    '[NO CHANGE]',
    'Being pedaled furiously, the bicycle he rode was trying to catch up to the bus that had left without him.',
    'The bus, leaving without him, was trying to be caught up to by him as he pedaled the bicycle furiously.',
    'He pedaled his bike furiously as he tried to catch up to the bus that had left without him.',
    NULL,
    'D',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    216,
    'The _____World Cup is a famous and well-known_____ soccer (or football) championship with fans around the world.',
    '[NO CHANGE]',
    'World Cup is a famous and commonly- known',
    'well-known World Cup is a famous',
    'World Cup is a famous',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    217,
    '_____The art and artifacts of ancient American cultures were intended to be represented by the artist._____',
    '[NO CHANGE]',
    'The artist intended to represent the art and artifacts of ancient American cultures.',
    'The artist''s intention was to represent the art and artifacts of early ancient American cultures.',
    'The artist intended to represent and portray the art and artifacts of ancient early American cultures.',
    NULL,
    'B',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    218,
    '_____A thrifty investor may buy diverse assets in order to make a fortune._____',
    '[NO CHANGE]',
    'To make a fortune, diverse assets may be bought by a thrifty investor.',
    'A thrifty investor may buy many different diverse assets in order to make a fortune.',
    'Many different diverse assets may be bought by a thrifty investor in order to make a fortune.',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    219,
    'Gustav Holst, though known mainly for his orchestral suites, _____also has several operas composed by him._____',
    '[NO CHANGE]',
    'also composed and wrote several operas.',
    'also composed several operas.',
    'also composed several operas that he wrote.',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    220,
    'The Olympic runner was legendary for winning the marathon _____and also_____ the 100- meter dash in the same week.',
    '[NO CHANGE]',
    'and the',
    'and additionally the',
    'and, furthermore, the',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    221,
    'The unique structural-support _____beams, specifically built and intended for this particular use, were_____ both artistic and technical marvels.',
    '[NO CHANGE]',
    'beams, built for this function, were',
    'beams were',
    'beams, constructed and intended for this particular use, were',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    222,
    'Some love songs _____are written for a specific romantic interest; others are composed about more general ideals._____',
    '[NO CHANGE]',
    'are written and created for a specific romantic interest; others are composed about more general ideals or principles.',
    'are written for a specific romantic or love interest; others are composed about more general ideals.',
    'or tunes are written for a particular and specific romantic interest; others are composed about more general ideals or principles.',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    223,
    'The masterful fresco _____painting, unmatched by any other artwork in its genre, was extremely unique._____',
    '[NO CHANGE]',
    'painting was extremely unique.',
    'painting, unmatched by any other artwork in its genre, was one-of-a-kind.',
    'painting was unique.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    224,
    'Which of the following choices provides the most relevant introduction to the sentence that follows?_____Considered one of the most popular Western songs of all time,_____ "Bury Me Not on the Lone Prairie" is actually an adaptation of an old sea song called "The Sailor''s Grave."',
    '[NO CHANGE]',
    'A favorite song of mine,',
    'Often recorded by radio artists,',
    'Although a watery origin may seem strange for aWestern song,',
    NULL,
    'D',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    225,
    'Which of the following choices most effectively combines the two underlined sentences below?_____Alan Dinehart was an American actor and director who initially studied to be a priest. He entered the film industry and eventually appeared in over eighty-eight films._____',
    'Alan Dinehart was an American actor and director who initially studied to be a priest, who entered the film industry, and eventually appeared in over eight-eight films.',
    'Alan Dinehart was an American actor and director who initially studied to be a priest, but entered the film industry instead, eventually appearing in over eighty-eight films.',
    'Entering the film industry, Alan Dinehart was an American actor and director who initially studied to be a priest, and eventually appeared in over eighty-eight films.',
    'Alan Dinehart, entering the film industry, an American actor and director, initially studied to be a priest, and eventually appeared in over eight-eight films.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    226,
    'Which of the following choices provides the most relevant and specific supporting examples for the following sentence?A "rake angle" is a measurement used to describe the sharpness or dullness of edges or points when making tools _____that have cutting edges or chiseling points_____.',
    '[NO CHANGE]',
    'to use in various applications',
    'such as chisels, blades, and saws',
    'for industrial purposes',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    227,
    'Which of the following choices provides the most relevant and vivid sensory details for the following sentence?The mountains of northern Scotland, while often cold and dangerous for inexperienced hikers, are also filled with _____sweeping sunlit glens and the sweet smell of native wildflowers._____',
    '[NO CHANGE]',
    'interesting areas, charming weather, and beautiful sights.',
    'many native plants and animals to observe.',
    'excellent places to hike alone or in a group.',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    228,
    'Which of the following choices most effectively transitions between the two underlined sentences below?_____Debra Sina is a monastery located in the highlands of Eritrea, a country in the Horn of Africa. This monastery is the site of an annual pilgrimage in June of each year._____',
    'Debra Sina, located in Eritrea, a country in the Horn of Africa, in the highlands, is the site of an annual pilgrimage in June of each year, and is a monastery.',
    'Located in the highlands of Eritrea, a country in the Horn of Africa, Debra Sina is a monastery and the site of an annual pilgrimage in June of each year.',
    'The monastery Debra Sina, the site of an annual pilgrimage each June, is located in the highlands of a country in the Horn of Africa, which is Eritrea.',
    'Debra Sina is a monastery located in the highlands of Eritrea, a country in the Horn of Africa, and is the site of an annual pilgrimage each June.',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    229,
    'Which of the following choices provides the most relevant and effective conclusion to the following sentence?Romania is perhaps the only former Soviet country that is attempting to remedy the illegal appropriation of factories, homes, and business through the foundation of a national joint-stock company that _____helps reimburse the original owners for their stolen property._____',
    '[NO CHANGE]',
    'was founded in 2005 and is called "Fondul Proprietetea."',
    'has more than 150,000 individual shareholders across the nation.',
    'is unprecedented among nations that were once held under Communist governments.',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    230,
    'Which of the following choices provides the most specific and vivid description of the colors of the Hawaiian sunset in the following sentence?The Hawaiian sunset is famed for its colors, _____which are generally quite vivid_____, and has been commemorated in song by no less a luminary than Elvis Presley himself.',
    '[NO CHANGE]',
    'which range from azure blue to ripe-lemon yellow',
    'which can be spectacularly amazing',
    'which contain a variety of hues due to rare trace elements in the atmosphere',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    231,
    'Which of the following choices provides the most effective and specific supporting details for the following sentence?So-called "Grid computing" is a computer network in which each computer shares its _____resources, rather than keeping each computer isolated from_____ every other computer in the system.',
    '[NO CHANGE]',
    'resources and links with',
    'resources - such as processing power, data storage, and memory - with',
    'resources - any physical or virtual component of limited availability - with',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    232,
    'The nature reserve, owned and operated by the Royal Society, is located in Minsmere, Suffolk. Intended primarily to aid in bird conservation, the environment is favorable for bird nesting and breeding, and includes a diverse variety of habitats such as marshes, grasslands, and forest areas. [1] Guests of the reserve can access a visitor center and spot many species of birds on an extensive network of footpaths and trails. [2]At Point [1], the writer is considering adding the following sentence:Over 15,000 curious bird-lovers travel to the reserve each month during birding season.Should the writer make this addition here?',
    'Yes, because it provides both relevant information and a transition to the following sentence.',
    'Yes, because the sentence reinforces the paragraph''s main point.',
    'No, because it blurs the paragraph''s focus by adding irrelevant information.',
    'No, because it includes information that has already been stated elsewhere in the paragraph.',
    NULL,
    'A',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    233,
    'The nature reserve, owned and operated by the Royal Society, is located in Minsmere, Suffolk. Intended primarily to aid in bird conservation, the environment is favorable for bird nesting and breeding, and includes a diverse variety of habitats such as marshes, grasslands, and forest areas. [1] Guests of the reserve can access a visitor center and spot many species of birds on an extensive network of footpaths and trails. [2]The writer is considering concluding this paragraph with the following sentence:Many bird conservation sites around the world are experiencing funding problems due to lack of public support and private donations.Should the writer make this addition here?',
    'Yes, because the sentence provides a satisfactory conclusion to the preceding paragraph.',
    'Yes, because the sentence provides evidence to support a relevant claim.',
    'No, because the sentence provides a level of detail that is inconsistent with the rest of the paragraph.',
    'No, because the sentence introduces irrelevant information to the paragraph.',
    NULL,
    'D',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    234,
    'Edgar Rice Burroughs, who lived from 1875 to 1950, was an American writer known for his works in the adventure and science-fiction genres.While many of his stories and characters have faded into obscurity over the past century, he will probably always remain in the public eye as the creator of Tarzan, the famous "man raised by apes" who became a cultural icon. [3] _____Though many literary critics of the past and present have not exactly respected Burroughs'' work for its stylistic merits_____, there can be no doubt that his adventures have had a great impact upon popular culture at large, and on a smaller scale, upon the imaginations of his readers, both young and old. [4]The writer is considering deleting the underlined portion of Sentence 3. Should the writer make this change?',
    'Yes, because it interrupts the flow of the paragraph.',
    'Yes, because it contains a negative tone that is not consistent with the rest of the paragraph.',
    'No, because it offers an effective and informative counterpoint to the rest of the sentence.',
    'No, because introduces a relevant supporting example.',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    235,
    'Edgar Rice Burroughs, who lived from 1875 to 1950, was an American writer known for his works in the adventure and science-fiction genres.While many of his stories and characters have faded into obscurity over the past century, he will probably always remain in the public eye as the creator of Tarzan, the famous "man raised by apes" who became a cultural icon. [3] _____Though many literary critics of the past and present have not exactly respected Burroughs'' work for its stylistic merits_____, there can be no doubt that his adventures have had a great impact upon popular culture at large, and on a smaller scale, upon the imaginations of his readers, both young and old. [4]The writer is considering concluding this paragraph with the following sentence:After a long and successful life, Burroughs'' final resting place was his ranch in California, fittingly named "Tarzana,"which has since become a residential neighborhood in the city of Los Angeles.Should the writer make this addition here?',
    'Yes, because it provides evidence that supports a relevant claim within the paragraph.',
    'Yes, because it provides an interesting conclusion to Burroughs'' life story that ties back to information presented earlier in the paragraph.',
    'No, because it fails to provide evidence for the paragraph''s central claim.',
    'No, because it does not fit with the style and tone of the preceding paragraph.',
    NULL,
    'B',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    236,
    '[5] _____The International Space Station ("ISS"), launched into orbit in 1998, has been continuously inhabited since November 2000_____. This astonishing technological achievement represents one of humanity''s largest and most ambitious leaps into outer space. Largely powered by massive solar arrays, the ISS provides a completely self-contained living environment, although its continued functioning depends upon regular supply runs and crew replacements. [6] If all goes well, the ISS is expected to serve until at least the year 2028.The writer is considering deleting the underlined sentence. Should this sentence be kept or deleted?',
    'Kept, because it offers an effective introduction to the paragraph and defines a key acronym.',
    'Kept, because provides a relevant example that supports the paragraph''s main point.',
    'Deleted, because it distracts from the primary focus of the paragraph.',
    'Deleted, because it does not provide an effective introduction to the paragraph.',
    NULL,
    'A',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    237,
    '[5] _____The International Space Station ("ISS"), launched into orbit in 1998, has been continuously inhabited since November 2000_____. This astonishing technological achievement represents one of humanity''s largest and most ambitious leaps into outer space. Largely powered by massive solar arrays, the ISS provides a completely self-contained living environment, although its continued functioning depends upon regular supply runs and crew replacements. [6] If all goes well, the ISS is expected to serve until at least the year 2028.At Point [6], the writer is considering adding the following sentence to the paragraph:The station''s research laboratories provide an invaluable environment for experiments in micro-gravity in fields that range from biology and physics to astronomy and meteorology.Should the writer add this sentence here?',
    'Yes, because this sentence maintains the informal and conversational tone of the paragraph.',
    'Yes, because this sentence adds relevant information about the purpose of the ISS.',
    'No, because this sentence does not provide an effective counterargument to the previous sentence.',
    'No, because this sentence blurs the main focus of the paragraph with irrelevant information.',
    NULL,
    'B',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    238,
    '[7] In the United States, warrantless searches are restricted by the Fourth Amendment. This was partially a product of the British Empire''s treatment of colonists, whose homes soldiers could search without warning for evidence of revolutionary activities. [8] _____The First Amendment also protects an important freedom of citizens by guaranteeing freedom of religion, speech, and press._____ During certain periods in history (for example, during wartime) or under specific circumstances (such as fears of domestic terrorism or foreign spying), there have been exceptions to this restriction. The topic of warrants and related search-and-seizure laws in the United States continues to be hotly debated in a new era of communications technology and social media.The writer is considering adding the following sentence as an introduction to the paragraph:A "warrantless search" is a search or seizure of personal property conducted without a documented court order to justify the intrusion on a citizen''s privacy.Should the writer add this sentence here?',
    'Yes, because this sentence provides a useful example that will be explored in the paragraph.',
    'Yes, because this sentence provides a useful definition of a key term explored in the paragraph.',
    'No, because this sentence provides an unnecessary amount of detail about a single term.',
    'No, because this sentence fails to introduce the main topic of the paragraph.',
    NULL,
    'B',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    239,
    '[7] In the United States, warrantless searches are restricted by the Fourth Amendment. This was partially a product of the British Empire''s treatment of colonists, whose homes soldiers could search without warning for evidence of revolutionary activities. [8] _____The First Amendment also protects an important freedom of citizens by guaranteeing freedom of religion, speech, and press._____ During certain periods in history (for example, during wartime) or under specific circumstances (such as fears of domestic terrorism or foreign spying), there have been exceptions to this restriction. The topic of warrants and related search-and-seizure laws in the United States continues to be hotly debated in a new era of communications technology and social media.The writer is considering deleting the underlined sentence. Should this sentence be kept or deleted?',
    'Kept, because the sentence expands upon a relevant topic.',
    'Kept, because the sentence provides an additional supporting example for the paragraph.',
    'Deleted, because the sentence distracts from the main topic of the paragraph.',
    'Deleted, because the sentence undermines the primary claims of the paragraph.',
    NULL,
    'C',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    240,
    '[1] Laurence Olivier, born in 1907, was a respected English actor and director who rose to become one of the most famous men in cinema and stagecraft. [2] Although his family had no connections to the theater, Olivier studied at a drama school in London as a boy and achieved his first success in the theater in his early 20s. [3] By the 1940s, he was an established star, and co-directed at the Old Vic, one of the most important theaters in London. [4] His final major role was as King Lear, and critics glowed over his realistic, natural portrayal of the grieving old king. [5] Some of his most famous roles were as Shakespearean leads, but he displayed a great versatility in portraying a wide variety of characters over the course of his career. [6] For his vast critical and popular success, Olivier was honored with a knighthood and other national decorations during his lifetime. [7] He died at the age of 82 after a prolonged period of ill health, although he continued to work throughout his illnesses.To make this paragraph most logical, sentence 4 should be placed',
    'where it is now.',
    'after sentence 1.',
    'after sentence 5.',
    'after sentence 7.',
    NULL,
    'C',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    241,
    '[1] "Techno" is a form of electronic dance music that was pioneered in Detroit, Michigan in the mid- 1980s. [2] The style and sounds of Techno emerged as a response to a variety of stimuli and pressures. [3] For one thing, the young musicians who created this style often had little disposable income with which to purchase expensive music gear, which is part of the reason for the stripped-down, minimal style of early techno. [4] Furthermore, the gear itself had many limitations. [5] For example, many of the drum machines and synthesizers only had enough "memory" to program short, repeating sections of music called "loops." [6] Most early techno tracks were built out of several electronic loops played over each other; traditionally, a drum machine, bass synthesizer, and lead synthesizer would form the core of the sound. [7] A third major influence was the sound of the city of Detroit itself. [8] Known for its industrial character and filled with machinery, Detroit can be seen in many ways as a physical embodiment of the "Techno sound."To make this paragraph most logical, sentence 5 should be placed',
    'where it is now.',
    'after sentence 1.',
    'after sentence 3.',
    'after sentence 6.',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    242,
    '[1] The comics simply would not be the same without Stan Lee. [2] Born in 1922, Lee was a comicbook author, editor, and publisher who rose to lead industry juggernaut Marvel Comics for twentyyears. [3] His cultural impact was enormous: he co-created famous superheroes such as Spider-Manand the X-Men, forced needed change upon the outdated regulations of the Comics Code Authority,and helped revolutionize the very essence and style of modern comic books. [4] Known as a generous,creative soul and revered by comic fans around the world, Stan Lee never failed to receive thunderousapplause at all of his public appearances. [5] Although the world lost Stan Lee in 2018, he will neverbe forgotten as long as his colorful heroes still spring off the silver screen and the printed page.The writer wants to add the following sentence to the paragraph:In his later years, blockbuster movies based on his characters hit the theaters, andLee was ever-popular for the cameo roles he made in many of these films.The best placement for this sentence is',
    'after sentence 1.',
    'after sentence 2.',
    'after sentence 3.',
    'after sentence 5.',
    NULL,
    'C',
    '时态与语态',
    'for + 时间段常与现在完成时连用，表示从过去持续到现在的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    243,
    '[1] The act of bending one knee to the ground is properly called "genuflecting," though in conversation it may be called simply "kneeling." [2] Traditionally considered a sign of deepest respect, this gesture is common in many cultures, religions, and ceremonies around the world and throughout history. [3] Alexander the Great was known for requiring genuflection in his court etiquette, and in medieval Europe, commoners were typically required to genuflect before kings and other high- ranking members of the noble class. [4] However, elderly people, pregnant women, or people in poor physical condition are often exempted from mandatory genuflection. [5] Instances of genuflecting in the modern era are seen in the traditional marriage proposal, in the Roman Catholic Church, and in presentations of a folded flag to the family members of a fallen veteran. [6] A failure to genuflect at the required moment may be interpreted as a sign of disrespect and dishonor.To make this paragraph most logical, sentence 4 should be placed',
    'where it is now.',
    'after sentence 1.',
    'after sentence 5.',
    'after sentence 6.',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    244,
    'A list of the richest Americans in history will be filled largely with self-made entrepreneurs, althoughthere will also be some appearances from heirs and heiresses to family fortunes. And, because ofAmerica''s preeminence among the world''s economy, the richest members of American society arefrequently among the richest men and women in the entire world. [A] John D. Rockefeller may havebeen the wealthiest American in history by percentage of the nation''s total wealth (or "GDP") in histime. [B] His staggering fortune may have been worth as much as 2% of the entire country''s nationalproduction at the time. [C] In 2018, Jeff Bezos, founder of Amazon.com, became the richest man inmodern history, with an eye-popping net worth estimated at over 150 billion dollars. If there''s onemajor lesson the list can teach, it''s that if you want to reach the pinnacle of financial fortunes, it paysto either create an industry yourself, or to dominate one that already exists. [D]The writer wants to add the following sentence to the paragraph:Other noteworthy "richest Americans" include Andrew Carnegie, CorneliusVanderbilt, Bill Gates, and Henry Ford.The sentence would most logically be placed at:',
    'Point A.',
    'Point B.',
    'Point C.',
    'Point D.',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    245,
    '[1] The role of a navel architect is to handle the engineering and design of marine vessels and structures. [2] This can include working on a range of projects, from deep-sea oil platforms to the smallest of sailboats. [3] A seagoing vessel may be described as having a "fair" shape, which, in essence, means a shape that is "right for the job." [4] Required knowledge for a professional naval architect includes hydrodynamics, materials properties, and construction techniques. [5] Somewhere between an art and a science, the field of naval architecture still finds it difficult to quantify its principles into simple rules. [6] Given the continued importance of well-built vessels for both industrial and pleasure purposes, and the lack of well-quantified design principles, it seems likely that job prospects for talented young naval architects will remain relatively strong.For the sake of logic and cohesion, the best placement for Sentence 3 would be',
    'after sentence 1.',
    'after sentence 5.',
    'after sentence 6.',
    'where is is now.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    246,
    'India - a diverse country containing a mix of cultures and customs within its borders - is _____famous in_____ its warm hospitality, its spicy rice dishes, and its embrace of many different spiritualities.',
    '[NO CHANGE]',
    'famous to',
    'famous for',
    'famous on',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    247,
    'James decide to become a mechanic, for he had always been _____curious on_____ the inner workings of machinery and engines.',
    '[NO CHANGE]',
    'curious for',
    'curious into',
    'curious about',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    248,
    'Though most domesticated dogs are not _____capable of_____ caring for themselves for extended periods of time, in some cities there are packs of roaming feral dogs who are able to scavenge and survive on their own without any human support whatsoever.',
    '[NO CHANGE]',
    'capable to',
    'capable in',
    'capable with',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    249,
    'Galileo Galilei was a profound thinker, influential in many fields, but particularly known for his theories and research in astronomy, and for his _____rebellion from_____ religious authority through his insightful assertion that the Earth revolved around the Sun.',
    '[NO CHANGE]',
    'rebellion against',
    'rebellion under',
    'rebellion from',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    250,
    'Although visiting relatives in another city can be fun, you must be careful not to _____impose on_____ their hospitality for too long, lest you become a burden.',
    '[NO CHANGE]',
    'impose to',
    'impose for',
    'impose around',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    251,
    '"Asymmetrical warfare" is another name for guerrilla-style tactics, when a smaller force must engage a much larger _____force to_____ battle.',
    '[NO CHANGE]',
    'force for',
    'force in',
    'force with',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    252,
    'Before a recording artist _____enters through_____ a contract with a record label, he or she should have the deal examined by at least one independent lawyer with expertise in the fields of entertainment and contract law.',
    '[NO CHANGE]',
    'enters by',
    'enters towards',
    'enters into',
    NULL,
    'D',
    '情态动词与虚拟语气',
    'should have done 表示本应做而未做（虚拟），暗含责备或遗憾。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    253,
    'Austin, Texas is _____regarded in_____ some residents and music journalists as "the Live Music Capital of theWorld," although one must admit that there are many other cities that could contend for the honor.',
    '[NO CHANGE]',
    'regarded by',
    'regarded between',
    'regarded upon',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    254,
    '_____In keeping with_____ the era of its design, the old hotel maintains a complete set of antique furnishings, and all the art on its walls are original works.',
    '[NO CHANGE]',
    'As keeping to',
    'By keeping with',
    'In keeping to',
    NULL,
    'A',
    '主谓一致',
    '"the + 形容词" 表示一类人时，谓语用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    255,
    'Leonardo da Vinci is _____celebrated for_____ one of humanity''s foremost geniuses: his creative insights into painting, anatomy, and mechanical design can stun a contemporary audience even five centuries after his death.',
    '[NO CHANGE]',
    'celebrated within',
    'celebrated as',
    'celebrated upon',
    NULL,
    'C',
    '主谓一致',
    'each of / one of + 复数名词作主语时，核心词是 each/one（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    256,
    'The group of people, walking through the prestigious museum _____collection, were talking loudly_____, while others concealed their irritation.',
    '[NO CHANGE]',
    'collection, was talking loudly',
    'collection and talking loudly',
    'collection, are talking loudly',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    257,
    '_____To build city bridges and roads and laying pipes is the job_____ of some construction workers, but competent workers can be found building other industrial projects as well.',
    '[NO CHANGE]',
    'To build city bridges and roads and to lay pipes are the jobs',
    'Building city bridges and roads and to lay pipes are the jobs',
    'To build city bridges and roads and to lay pipes is the job',
    NULL,
    'B',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    258,
    'The study of _____law, which may lead to a well- respected career, but you_____ must also consider the extremely high cost of attending law school.',
    '[NO CHANGE]',
    'law may lead to a well-respected career, but you',
    'law, leading to a well-respected career, but you',
    'law, may lead to a well-respected career, but you',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    259,
    'Though _____it is comprehended by_____ only a few, the theories postulated by the famous physicist could be of the utmost importance to society.',
    '[NO CHANGE]',
    'they comprehend',
    'it was comprehended by',
    'they are comprehended by',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    260,
    'In the Swedish town of Gavle, a giant wood- and-straw statue of a traditional Christmas goat _____will be damaged_____ by deliberate arson a total of 37 times since 1966.',
    '[NO CHANGE]',
    'is damaged',
    'has been damaged',
    'once was damaged',
    NULL,
    'C',
    '时态与语态',
    'since + 过去时间点，主句用现在完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    261,
    '_____Chewing loudly and talking endlessly at the table_____ are frowned upon within many social circles.',
    '[NO CHANGE]',
    'Chewing loud and endless talking',
    'Chewing loudly and talking endless',
    'Chewing loud and talking endlessly',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    262,
    'The executives at the record company agreed that _____the new band must be listened to by them before the company considered promoting_____ its self-recorded album.',
    '[NO CHANGE]',
    'only after they listened to the new band would the company consider promoting',
    'only after the new band was listened to by them would they consider promoting',
    'they must listen to the new band before they considered promoting the album the band made on its own,',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    263,
    'Although life is short, passing briefly before our eyes, _____yet_____ we may choose to make the most of it, since we are aware of our mortality.',
    '[NO CHANGE]',
    'so',
    'and',
    '[DELETE the underlined portion]',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    264,
    'A Pinkernes was an official cup-bearer - always male - for the Byzantine emperor, who had an extensive staff that responded to _____his_____ every whim and command.',
    '[NO CHANGE]',
    'him at',
    'the <em>pinkernes</em>''s',
    'that man''s',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    265,
    '[1] The 1980 Summer Olympics were held in Moscow, in the Soviet Union (now Russia). [2] These were the first Olympic Games ever held in a socialist country, which may have been part of what caused these Games to be so controversial on an international scale. [3] Only 80 countries were represented at the 1980 Olympics; over sixty countries boycotted the entire competition because of the Soviet invasion of Afghanistan. [4] These 1980 Olympics were also controversial for another reason: steroids, blood doping, and other illegal performance-enhancing drugs were widespread among the athletes. [5] Surprisingly, there was a greater variety of events held than at any previous Olympics, a total of 203 different fields of competition. [6] It''s also of note that some athletes from boycotting countries still participated in the Games under the Olympic Flag, since they were prevented from representing their home countries by the international tensions. [7] Nearly 10 years later, a report from the Australian Senate would claim that "the Moscow Games might well have been called the Chemists'' Games." [8] The ramifications of these troubled Olympic Games - from political tensions to drug usage - have continued to reverberate through the modern era.For the sake of logic and cohesion, the best placement for sentence 4 would be',
    'where it is now.',
    'before sentence 3.',
    'after sentence 5.',
    'before sentence 7.',
    NULL,
    'D',
    '时态与语态',
    'for + 时间段常与现在完成时连用，表示从过去持续到现在的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    266,
    'Albert Einstein is considered by many to be the _____preeminent_____ theoretical physicist of all time for his astonishing insights into the nature of the universe.',
    '[NO CHANGE]',
    'preceding',
    'premature',
    'permanent',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    267,
    '_____As it shook violently and spun in circles, Jimmy watched the carnival ride go_____ from an amusing diversion to a dangerous piece of rogue machinery.',
    '[NO CHANGE]',
    'Shaking violently and spinning in circles, Jimmy watched the carnival ride go',
    'Jimmy, as it shook violently and spun in circles, watched the carnival ride go',
    'As it shook violently and spun in circles, the carnival ride that Jimmy watched went',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    268,
    'The Lotus 25 was a revolutionary design for a Formula One race car, and within a year of its debut, driver Jim Clark _____won the World Championship with it._____Which choice provides the most relevant and vivid sensory details to the previous sentence?',
    '[NO CHANGE]',
    'sped to a World Championship victory in the roar of its engine.',
    'proved that its design was exceptional with a World Championship victory.',
    'demonstrated its speed and power with a World Championship victory.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    269,
    'I prefer _____the way that Frank Gehry designs buildings to_____ Frank Lloyd Wright''s buildings.',
    '[NO CHANGE]',
    'Frank Gehry''s designs to',
    'Frank Gehry to',
    'Frank Gehry''s designs more than',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    270,
    'The trial lawyer found herself increasingly troubled by the number of convicted felons who had never _____admitted of_____ their crimes, despite the evidence being strongly in favor of their guilt.',
    '[NO CHANGE]',
    'admitted for',
    'admitted to',
    'admitted that',
    NULL,
    'C',
    '主谓一致',
    'the number of + 复数名词作主语时，核心词是 number（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    271,
    '_____John was debating the finer merits of interplanetary exploration, but Sarah was rebutting_____ his argument with excellent points about world hunger.',
    '[NO CHANGE]',
    'John, debating the finer merits of interplanetary exploration, but Sarah was rebutting',
    'John debated the finer merits of interplanetary exploration, Sarah rebutted',
    'John was debating the finer merits of interplanetary exploration; but Sarah was rebutting',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    272,
    '_____Her trainer and her, surprisingly, both_____ preferred to practice in the pouring rain, and agreed that if one could ride a horse in a storm, one could ride a horse in any conditions.',
    '[NO CHANGE]',
    'Surprisingly, both her and her trainer',
    'She and her trainer, surprisingly, both',
    'Both her and her trainer, surprisingly,',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    273,
    '[1] Just outside of the major metropolis of Chengdu, China, lies the world''s foremost Giant Panda breeding facility. [2] The facility, founded in 1987 and known officially as the "Chengdu Research Base of Giant Panda Breeding," is a non-profit research center and internationally- recognized tourism destination. [3] The research base spreads over dozens of acres and is home to over 80 pandas of all ages - from tiny newborn infants to rambunctious, playful adolescents to fully-grown giant bears who seem calm and contemplative as they munch bamboo shoots. [4] Here, the pandas are kept safe and happy in large enclosures that mimic their natural environments and provide plenty of space to climb, play, sleep, and eat. [5] Although the Giant Pandas are the main attraction, there is also a large pen dedicated to the fox-like Red Pandas. [6] Both Giant Pandas and Red Pandas are endangered after years of poaching, pollution, and habitat loss. [7] Thus, the Chengdu Panda Base is not only an extraordinarily interesting place for nature-lovers to visit, but also a crucial force in the battle to keep these magnificent animals from going extinct forever.The author is considering deleting sentence 6. Should the author make this change?',
    'Yes, because the sentence distracts from the primary topic of the paragraph.',
    'Yes, because the negative tone of this sentence is not consistent with the rest of the paragraph.',
    'No, because the sentence includes useful information that relates to the conclusion of the paragraph.',
    'No, because the sentence provides a relevant example to the paragraph.',
    NULL,
    'C',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    274,
    '[1] Just outside of the major metropolis of Chengdu, China, lies the world''s foremost Giant Panda breeding facility. [2] The facility, founded in 1987 and known officially as the "Chengdu Research Base of Giant Panda Breeding," is a non-profit research center and internationally- recognized tourism destination. [3] The research base spreads over dozens of acres and is home to over 80 pandas of all ages - from tiny newborn infants to rambunctious, playful adolescents to fully-grown giant bears who seem calm and contemplative as they munch bamboo shoots. [4] Here, the pandas are kept safe and happy in large enclosures that mimic their natural environments and provide plenty of space to climb, play, sleep, and eat. [5] Although the Giant Pandas are the main attraction, there is also a large pen dedicated to the fox-like Red Pandas. [6] Both Giant Pandas and Red Pandas are endangered after years of poaching, pollution, and habitat loss. [7] Thus, the Chengdu Panda Base is not only an extraordinarily interesting place for nature-lovers to visit, but also a crucial force in the battle to keep these magnificent animals from going extinct forever.At Point [5], the author is considering adding the following sentence to the paragraph: Tourists enjoy watching the bears'' daily activities so much that the Giant Pandas even have their own live video stream, which is broadcast around the world via the internet. Should the writer add this sentence here?',
    'Yes, because this sentence provides additional relevant information to the paragraph.',
    'Yes, because this sentence provides an effective supporting argument to the paragraph.',
    'No, because this sentence introduces unrelated information to the paragraph.',
    'No, because this sentence repeats information that has already been provided elsewhere.',
    NULL,
    'A',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    275,
    'In contrast to the purpose of typical audio equipment, which is to provide a pleasant sound, _____"reference" speakers are designed to reproduce_____ sound exactly as it was recorded.',
    '[NO CHANGE]',
    'reference speakers are designed for reproducing',
    'the purpose of "reference" speakers is to reproduce',
    'the purpose of "reference" speakers is reproducing',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    276,
    '_____Grateful for avoiding the perils of war, my thoughts were that_____ a military draft should never again be reinstated in a civilized country.',
    '[NO CHANGE]',
    'Grateful for avoiding the perils of war, I thought that',
    'My thoughts were, grateful for avoiding the perils of war, that',
    'My thoughts, grateful for avoiding the perils of war, were that',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    277,
    'The Baron Tyrawley was born _____Charles O''Hara: who_____ rose through the ranks to become the Commander-in-Chief of the Royal Irish Army in the early 1700s.',
    '[NO CHANGE]',
    'Charles O''Hara who',
    'Charles O''Hara; who',
    'Charles O''Hara, who',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    278,
    '_____Hanging on the wall in my room are one acoustic guitar, which I won_____ in a music competition, and two electric bass guitars.',
    '[NO CHANGE]',
    'Hanging on the wall in my room is one acoustic guitar, which were won',
    'On the wall in my room is hanging one acoustic guitar, which I won',
    'Hanging on the wall in my room are one acoustic guitar, which were won',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    279,
    'The Sixpenny Library was a complete set of reference books (almost two hundred in total) that was published in the late 1920s and earned praise for its _____high quality and affordable price._____',
    '[NO CHANGE]',
    'high quality, level of excellence, and its affordable price.',
    'high quality, low cost, and affordable price.',
    'affordability, high quality, and inexpensive price.',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    280,
    'Darla''s apartment was often difficult to fall asleep in, because it was _____adjacent with_____ a room of rowdy party animals who loved to stay up late and blast their stereo system at maximum volume.',
    '[NO CHANGE]',
    'adjacent to',
    'adjacent at',
    'adjacent into',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    281,
    'Authors selected for the philosophy journal must be respected in _____their field, and_____ should hold advanced degrees and have a great deal of practical experience.',
    '[NO CHANGE]',
    'his or her field, and',
    'their field, they',
    'their field, such authors',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    282,
    '_____Bursting with color, the pale hospital patient appreciated her friend''s gift of flowers._____',
    '[NO CHANGE]',
    'Bursting with color, the pale hospital patient''s friend''s gift of flowers was appreciated.',
    'The pale hospital patient, bursting with color, appreciated her friend''s gift of flowers.',
    'The pale hospital patient appreciated her friend''s gift of flowers, which was bursting with color.',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    283,
    'Plymouth was once a brand of automobiles produced in the United States, but it was dissolved and absorbed into other brands in 2001; however, some investors _____would like to revive_____ the marque, leaving the future of the Plymouth name in question.',
    '[NO CHANGE]',
    'will revive',
    'revived',
    'reviving',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    284,
    'Although deforestation is extremely detrimental to the planet''s delicate ecology, _____yet_____ many timber companies continue to harvest lumber for large corporate profits.',
    '[NO CHANGE]',
    'and',
    'but',
    '[DELETE the underlined portion]',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    285,
    'The bass guitar differs from standard guitars in that the bass _____has fewer strings, a longer neck, and the ability to produce lower notes._____',
    '[NO CHANGE]',
    'has fewer strings, a longer neck, and can produce lower notes.',
    'having fewer strings, a longer neck, and can produce notes more low.',
    'with lower strings, a longer neck, and lower notes.',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    286,
    '_____Charles and me love_____ to play dodgeball, and we have spent many an hour engaged in this exciting pastime.',
    '[NO CHANGE]',
    'Charles and I love',
    'Charles and me loves',
    'Charles and I loves',
    NULL,
    'B',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    287,
    'ChristinaWahlberg _____can handle a foil quite deft_____; in fact, she has competed in Olympic fencing.',
    '[NO CHANGE]',
    'can deft handle a foil',
    'can handle a foil quite deftly',
    'being a deft foil-handler',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    288,
    '_____Unethical political decisions are able to be protested by concerned American citizens._____',
    '[NO CHANGE]',
    'Unethical political decisions have the ability to be protested by concerned American citizens.',
    'Concerned American citizens are able to protest unethical political decisions made by politicians.',
    'Concerned American citizens are able to protest unethical political decisions.',
    NULL,
    'D',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    289,
    'Historical wolf hunts would include noblemen, conscripted peasant farmers, and trained wolfhounds, though _____they were_____ often injured as the wolves attempted to defend themselves with teeth and claws.',
    '[NO CHANGE]',
    'it was',
    'the wolfhounds were',
    'these were',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    290,
    'James Scawen was a British politician in the mid-1700s who was once pressured by a large mob to reveal how he had voted in an important election, _____and his father inherited a large estate near London_____.Which of the following choices provides the most relevant conclusion to the preceding sentence?',
    '[NO CHANGE]',
    'but he declined to share this information, stating that he must keep his vote secret in order to remain "an independent man."',
    'and in his later years, he developed Carshalton Park with improvements like canals, mills, and grottoes.',
    'and he lived from 1734 to 1801.',
    NULL,
    'B',
    '时态与语态',
    '被动语态：be + 过去分词。by 引出动作的执行者。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    291,
    'There are many kinds of dogs, but mine - a golden _____retriever, is my favorite, he_____ is very loyal and friendly.',
    '[NO CHANGE]',
    'retriever - is my favorite; he',
    'retriever - is my favorite, he',
    'retriever) is my favorite; he',
    NULL,
    'B',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    292,
    'I prefer modern boat designs, and although my cousins enjoyed the interior styling of the old steamship, I considered the decor fairly _____primordial_____.',
    '[NO CHANGE]',
    'elderly',
    'old-fashioned',
    'ancient',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    293,
    'Tara and Philippe liked their new patio, which _____they constructed themselves, it was built out of recycled plastic and reclaimed timber._____',
    '[NO CHANGE]',
    'they constructed themselves out of recycled plastic and reclaimed timber.',
    'out of recycled plastic and reclaimed timber, they had constructed it.',
    'they constructed themselves, the patio was built out of recycled plastic and reclaimed timber.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    294,
    'With little time left to spare before the plane''s arrival, _____my parents and me rushed_____ to the international airport to pick up my little brother.',
    '[NO CHANGE]',
    'me and my parents are rushing',
    'my parents and I rushed',
    'my parents and myself rushed',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    295,
    'The citizens and government of the costal city of Alicante, Spain, have ensured their port is integrated with the coastline through local planning _____laws; they prohibit_____ the construction of high buildings, with the exception of specialized situations such as construction cranes.',
    '[NO CHANGE]',
    'laws, prohibit',
    'laws; it prohibits',
    'laws; these laws prohibit',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    296,
    'The beans of the petai _____tree - known_____ for their peculiar smell and distinctive, powerful flavor - are used in the cuisine of many Southeast Asian countries such as Indonesia, Malaysia, and Thailand.',
    '[NO CHANGE]',
    'tree (known for',
    'tree, known for',
    'tree; known for',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    297,
    'When attending a stand-up comedy show, remember that the comedian''s jokes aren''t _____meant to_____ hurt your feelings, although he or she may make fun of specific people in the audience - including you!',
    '[NO CHANGE]',
    'meant for',
    'meant by',
    'meant that',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    298,
    'A "luge" is a tiny, minimalist sled for one or two people often used in high-speed downhill competitions; luging _____originates_____ as a winter pastime in the town of St.Moritz, Switzerland in the 1800s, but has since evolved into a thriving Olympic-level sport.',
    '[NO CHANGE]',
    'originated',
    'originating',
    'has originated',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    299,
    'Whether _____studying weeks in advance or you cram at the last second_____ on the way to school, the final exam will inevitably be challenging.',
    '[NO CHANGE]',
    'you study weeks in advance or cramming at the last second',
    'you study weeks in advance or cram at the last second',
    'you studied weeks in advance or it''s a last-second cram',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    300,
    'Anyone hoping to become a professional athlete must make sure that _____they are constantly practicing_____ the fundamental skills of the sport.',
    '[NO CHANGE]',
    'they constantly practice',
    'these constantly practice',
    'he or she is constantly practicing',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    301,
    'Ground squirrels are adept at hiding many _____nuts, but_____ frequently not so talented at remembering where they place their caches.',
    '[NO CHANGE]',
    'nuts; yet',
    'nuts; consequently, they are',
    'nuts, for they are',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    302,
    'The eruption of Mount St. Helens in 1980 was a _____misadventure_____ of epic proportions that destroyed 250 homes and killed fifty-seven people.',
    '[NO CHANGE]',
    'catalyst',
    'affliction',
    'calamity',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    303,
    'I was too embarrassed to tell my little brother, who is a professional picture-hanger, that _____the picture hung crooked on the wall._____',
    '[NO CHANGE]',
    'on the wall, the picture hung crooked.',
    'the picture on the wall hung crookedly.',
    'the picture, hung crookedly on the wall.',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    304,
    '_____A large bridge in France,_____ the Millau Viaduct is currently the tallest bridge in the world - 23 meters taller than the Eiffel Tower - and it saves tens of thousands of hours of driving time for French citizens and tourists each year.Which choice provides the most effective introduction to the preceding sentence?',
    '[NO CHANGE]',
    'Although it was constructed at great expense,',
    'Consistently ranked as one of the greatest engineering successes of all time,',
    'When compared to other bridges,',
    NULL,
    'C',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    305,
    '_____Bad drivers, always a source of mystery and amusement to me, though they_____ are dangerous to others on the road.',
    '[NO CHANGE]',
    'Bad drivers, though amusing and mystifying to me, they',
    'To my mystification and amusement, bad drivers, they',
    'Bad drivers are always a source of mystery and amusement to me, though they',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    306,
    'The number of new bands from my hometown of Austin _____are astonishing_____ when you consider that the city used to be much smaller than it is today.',
    '[NO CHANGE]',
    'were astonishing',
    'is astonishing',
    'astonish',
    NULL,
    'C',
    '主谓一致',
    'the number of + 复数名词作主语时，核心词是 number（单数），谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    307,
    'Both the egg and the eggplant, which have little in common with one another, _____is very_____ versatile when it comes to preparing a home-cooked meal for your family.',
    '[NO CHANGE]',
    'are very',
    'they are very',
    'was very',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    308,
    '_____Sitting on the couch and watching TV was_____ the best ways that Eric knew how to pass the time; unfortunately, he couldn''t pass 11th grade.',
    'NO CHANGE',
    'Sitting on the couch, watching TV, were',
    'Sitting on the couch and watching TV were',
    'To sit on the couch and watching TV were',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    309,
    'Arching their backs and _____hissing is a behavior_____ common to many felines when they feel that their safety is threatened.',
    '[NO CHANGE]',
    'hissing are a behavior',
    'hissing, behaviors',
    'hissing are behaviors',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    310,
    'According to my friend, a coin collector, _____a number of people are seeking Liberty Head nickels_____ minted in 1883.',
    '[NO CHANGE]',
    'a number of people is seeking Liberty Head nickels',
    'Liberty Head nickels are being sought by a number of people, they were',
    'Liberty Head nickels are sought by a number of people',
    NULL,
    'A',
    '主谓一致',
    'a number of + 复数名词作主语时，意为''许多''，谓语用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    311,
    '_____There is_____ more than two decades left until the next major earthquake is expected to shake California.',
    '[NO CHANGE]',
    'There have been',
    'There are',
    'It has',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    312,
    'Two writers in the USA, with unbelievable audacity, _____were claiming_____ to write a thousand books a year by working as a team and barely sleeping.',
    '[NO CHANGE]',
    'is claiming',
    'was claiming',
    'has been claiming',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    313,
    'Since news of the bankruptcy came suddenly, _____the entire company are clearing out their desks_____ immediately.',
    '[NO CHANGE]',
    'everyone in the company are clearing out their desks',
    'everyone in the company is clearing out his or her desk',
    'the entire company is clearing out their desk',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    314,
    'The school of fish, hiding among the jagged coral, _____were easily seen_____ by the hammerhead shark, despite the gloomy camouflage of the surroundings.',
    '[NO CHANGE]',
    'seen',
    'are easily seen',
    'was easily seen',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    315,
    'Philip Ferdinand''s debt, which grew rapidly as he spent frivolously on extravagant luxuries, _____were what ruined him in the end._____',
    '[NO CHANGE]',
    'was what ruined him in the end.',
    'and ruined him in the end.',
    'ruining him in the end.',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    316,
    'The vote of the American people, who turned out in record _____numbers, demonstrate_____ that our nation longs for more honest politicians to take office.',
    '[NO CHANGE]',
    'numbers, demonstrates',
    'numbers demonstrates',
    'numbers which demonstrate',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    317,
    'Pham Tuyen''s compositions, many of which praise Vietnamese _____Communism, is proof that music can simultaneously be_____ art and propaganda.',
    '[NO CHANGE]',
    'Communism, are proof that music can at the same time simultaneously be',
    'Communism, and music can simultaneously be',
    'Communism, are proof that music can be both',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    318,
    'The field of ceramics, which some laypeople simply call "pottery," _____are wide and varied_____ in application.',
    '[NO CHANGE]',
    'is wide and varied',
    'were wide and varied',
    'and is wide and varied',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    319,
    'The use of various drinking vessels for different _____dining situations are_____ a commonly accepted practice across many world cultures.',
    '[NO CHANGE]',
    'dining situations were',
    'dining situations is',
    'dining situations being',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    320,
    'My parents always told me that going to the horse races _____are something to experience at least once in your life._____',
    '[NO CHANGE]',
    'are something that one should experience at least once in your life.',
    'is something to experience at least once in your life.',
    'is something one''s life should experience at least once.',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    321,
    'Neither _____smoking nor drinking alcoholic beverages are_____ good for your long-term health.',
    '[NO CHANGE]',
    'to smoke nor drinking alcoholic beverages is',
    'smoking nor to drink alcoholic beverages are',
    'smoking nor drinking alcoholic beverages is',
    NULL,
    'D',
    '主谓一致',
    'either...or 连接并列主语时，谓语与 or 后面的主语保持就近一致。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    322,
    'Down the wildflower-covered _____mountain runs the trickle_____ of snowmelt and the roaring river that ends in a foaming waterfall.',
    '[NO CHANGE]',
    'mountain run the trickle',
    'mountain, the trickle',
    'mountain, runs the trickle',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    323,
    'The condensation that forms on cold _____glasses has_____ long been of interest to young scientists.',
    '[NO CHANGE]',
    'glasses have',
    'glasses having',
    'glasses are',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    324,
    'Although both of the campers saw the mountain _____lion, neither are going_____ to report the sighting to their counselors, since leaving the main trail is against camp rules.',
    '[NO CHANGE]',
    'lion but neither is going',
    'lion but neither are going',
    'lion, neither is going',
    NULL,
    'D',
    '主谓一致',
    'either...or 连接并列主语时，谓语与 or 后面的主语保持就近一致。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    325,
    'Only one in _____five doctors agrees_____ that health is more a matter of good genetics than of a balanced approach to eating and exercise.',
    '[NO CHANGE]',
    'five doctors agree',
    'five doctors have agreed',
    'five doctors are in agreement',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    326,
    '_____The collection of note shapes in written music from the 1400s are significantly different in appearance from the collection of note shapes in modern notation._____',
    '[NO CHANGE]',
    'The collection of note shapes in written music from the 1400s are, in appearance, significantly different from modern notation.',
    'The collection from the 1400s of note shapes in written music appearing significantly different from modern notation.',
    'Significantly different in appearance from the collection of note shapes in modern notation is the collection of note shapes in written music from the 1400s.',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    327,
    'While _____you were_____ at the hotel eating lunch, I will be at the gym for my daily workout.',
    '[NO CHANGE]',
    'you are',
    'you were being',
    'you have been',
    NULL,
    'B',
    '时态与语态',
    '主语与动词之间是被动关系，用被动语态 be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    328,
    'It''s a bad _____idea to go to_____ an international airport without having your passport ready!',
    '[NO CHANGE]',
    'idea going to',
    'idea will go to',
    'idea went to',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    329,
    'When your grandfather came to America two generations _____ago, he runs a_____ small factory in New York.',
    '[NO CHANGE]',
    'ago, running a',
    'ago, he will run a',
    'ago, he ran a',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    330,
    'This mountain began as a resort that _____will attract_____ famous international visitors to its spa and hotel.',
    '[NO CHANGE]',
    'attracts',
    'is attracting',
    'once attracted',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    331,
    'This wolf has not only learned to howl on cue, but also _____will jump_____ over fences.',
    '[NO CHANGE]',
    'jumping',
    'has jumped',
    'to jump',
    NULL,
    'D',
    '从句',
    'Not only 置于句首时，该分句需要部分倒装（助动词提前）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    332,
    '_____Lizards rustled_____ through the grass as we hiked up the hills of the wild island.',
    '[NO CHANGE]',
    'Lizards rustling',
    'Lizards are rustling',
    'Lizards will rustle',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    333,
    'Someday _____we went to the_____ North Pole to be like our favorite adventurers of olden days.',
    '[NO CHANGE]',
    'we will go to',
    'we had gone to',
    'we have gone',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    334,
    'Having a computer _____allowed you_____ to view more free entertainment than even a king and queen of a hundred years ago.',
    '[NO CHANGE]',
    'allows you',
    'allowing you',
    'is allowing you',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    335,
    'Since I''ve studied hard for medical school for years, I _____believed I would become_____ a doctor.',
    '[NO CHANGE]',
    'believed I will become',
    'believe I can become',
    'would become',
    NULL,
    'C',
    '时态与语态',
    'for + 时间段常与现在完成时连用，表示从过去持续到现在的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    336,
    'The oceans _____will have rose from_____ their ancient levels to their present-day levels because of powerful natural forces.',
    '[NO CHANGE]',
    'will be rising',
    'rose from',
    'will rise',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    337,
    'A well-trained team must practice _____regular and diligently_____, so that the members trust one another.',
    '[NO CHANGE]',
    'regular and diligent',
    'regularly and diligent',
    'regularly and diligently',
    NULL,
    'D',
    '从句',
    'so that 引导目的状语从句。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    338,
    'After training for many years in the company of other well- disciplined soldiers, a sniper _____shoots more accurately_____than other marksmen.',
    '[NO CHANGE]',
    'shoots more accurate',
    'can shoot more accurate',
    'shoots accurately more',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    339,
    'Though you may have training as a radiological technician, you can still _____make a career change easy_____ to an executive role.',
    '[NO CHANGE]',
    'make a career change easier',
    'make easy a career change',
    'easily make a career change',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    340,
    'You _____must run quickly_____ to get first or second place in the highly competitive Olympic sprints.',
    '[NO CHANGE]',
    'must run quick',
    'must run pretty quick',
    'might run quickly',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    341,
    'Kei Tari was a Japanese comedian and musician _____who melded seamless jazz and humor_____.',
    '[NO CHANGE]',
    'who seamlessly melded jazz and humor',
    'who was known for his melding of seamless jazz and humor',
    'who, with jazz and humor, seamlessly melded the two',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    342,
    'Annual "Clean Your Computer Day" _____is a keen- awaited holiday for_____ programmers around the world.',
    '[NO CHANGE]',
    'is keenly awaited by',
    'is keenly awaiting for',
    'is awaited with keenness by',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    343,
    'Surprisingly, my anthropology professor says she _____can speak easier_____ to a large crowd of people than to a small group of experts in her field.',
    '[NO CHANGE]',
    'can easily speak',
    'can speak more easy',
    'can speak more easily',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    344,
    'The "Feaster Five" road race is known for _____ending dramatically._____',
    '[NO CHANGE]',
    'ending dramatic.',
    'being dramatic and for ending.',
    'its having a dramatic ending.',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    345,
    'Sir Erec is an Arthurian Knight of the Round Table, and as such, _____he is required to fight valiant._____',
    '[NO CHANGE]',
    'he is valiantly required to fight.',
    'he is required to fight valiantly.',
    'requirements are that he must valiantly fight.',
    NULL,
    'C',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    346,
    'The champion lost all of our respect when she snatched the microphone and _____gave a haughty_____ victory speech.',
    '[NO CHANGE]',
    'gave a haughtily',
    'gives a haughty',
    'sounding haughtily, gave a',
    NULL,
    'A',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    347,
    'The tree branch banged and rattled against the window all night, and in the morning, _____it was_____ in pieces.',
    '[NO CHANGE]',
    'it is',
    'it will be',
    'the window was',
    NULL,
    'D',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    348,
    'Today in our class of thirty students, two policemen burst in the door and _____arrested her in_____ the middle of the classroom.',
    '[NO CHANGE]',
    'arrested him in',
    'arrested a female student in',
    'arrested a female student on',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    349,
    'The Russian ambassador spoke to the President of France and the delegate from _____Spain, promising him_____ that their governments could work together in the future.',
    '[NO CHANGE]',
    'Spain promising',
    'Spain, promising it',
    'Spain, promising both',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    350,
    'Strawberry jam from the chefs at my local French restaurant is my favorite treat; _____they make it_____ fresh each day in their kitchen.',
    '[NO CHANGE]',
    'it makes it',
    'they make them',
    'the chefs make them',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    351,
    'Cats, mice, and dogs don''t always get along together - the dogs tend to chase the cats, and _____they tend to_____ chase the mice.',
    '[NO CHANGE]',
    'they',
    'it tends to',
    'the cats tend to',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    352,
    'You may want to take an extra science, math, or language arts class, _____but it is_____ technically the only official requirement for graduation.',
    '[NO CHANGE]',
    'but math is',
    'but they are',
    'and they are',
    NULL,
    'B',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    353,
    'Today I saw two calico cats, a monarch butterfly, and three fluffy dogs on the way to work, but _____they were_____ my favorite animals of the morning.',
    '[NO CHANGE]',
    'it was',
    'the eagle I saw was',
    'the turtles I saw were',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    354,
    'The small group of soldiers could see the prince well-protected on one side of the battlefield and King Richard under vicious attack on the other, _____so they rallied to him_____.',
    '[NO CHANGE]',
    'so he rallied to them',
    'so they rallied to their king',
    'so the soldiers rallied to them.',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    355,
    'My friends want to go to the party, but _____they are not comfortable going to it_____ without getting their parents'' permission.',
    '[NO CHANGE]',
    'these friends are not comfortable going to it',
    'my friends are not comfortable going to this party',
    'these friends of mine are not comfortable going to this party',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    356,
    'In China and Korea, the chefs are known for their spicy food in certain regions and for more delicate fare in other parts _____of it._____',
    '[NO CHANGE]',
    'of them.',
    'of the countries.',
    'of China and Korea.',
    NULL,
    'C',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    357,
    'The ocean can be easily distinguished from a lake by two things: the presence of kelp and _____its distinctive smell_____.',
    '[NO CHANGE]',
    'their smell',
    'smelling distinct',
    'a distinctive smell',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    358,
    'The piano is known for its lovely and expressive sound; naturally, _____a trained pianist will find it_____ especially beautiful.',
    '[NO CHANGE]',
    'these will find it',
    'a trained pianist will find this',
    'a trained pianist will find the sound',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    359,
    'In a shocking turn of events, these illegal downloads are from protected government sources; _____it cannot be accurately traced._____',
    '[NO CHANGE]',
    'they cannot be accurately traced.',
    'tracing them is not possible.',
    'as a result, the downloads cannot be traced.',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    360,
    'No one, including the professors, _____were ready for_____ the probing questions asked by the members of the peer-reviewed scientific journal.',
    '[NO CHANGE]',
    'was ready for',
    'are ready for',
    'was ready for the answering of',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    361,
    'The company is about to run out of funding for research, so _____they are going_____ to turn to investors in hopes of securing a loan.',
    '[NO CHANGE]',
    'they will turn',
    'it is going',
    'they have gone',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    362,
    'One style of teacher prefers to lecture, rather than interact with students; _____they can often be found_____ at the front of the classroom.',
    '[NO CHANGE]',
    'find them',
    'it can often be found',
    'such an instructor can often be found',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    363,
    'Someone can be both brilliant in one field and far below average in many others; _____they are called "savants."_____',
    '[NO CHANGE]',
    'they can be called "savants."',
    'the term for these is "savants."',
    'he or she can be called a "savant."',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    364,
    'Everyone I know, including all my friends and _____relatives, are going_____ to be celebrating the holidays with good cheer.',
    '[NO CHANGE]',
    'relatives, is going',
    'my relatives, are going',
    'relatives is going',
    NULL,
    'B',
    '主谓一致',
    'each/every + 单名 and each/every + 单名作主语时，谓语用单数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    365,
    'As _____it ran_____ from the poachers, the pack of wolves raced across the barren tundra.',
    '[NO CHANGE]',
    'they ran',
    'they run',
    'it will run',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    366,
    'My young nephew reports that he knows some well-educated people who think _____he or she deserves_____ a scholarship or grant.',
    '[NO CHANGE]',
    'it deserves',
    'one deserves',
    'they deserve',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    367,
    '_____Until it can be_____ exchanged for a more durable and inexpensive material, these alloys will continue to be used in bridge-building.',
    '[NO CHANGE]',
    'Until it is',
    'Until they can be',
    'Once they can be',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    368,
    'Carlos predicted that anyone who attends the Bach and Mozart _____performances are_____ open-minded about the idea of listening to classical music.',
    '[NO CHANGE]',
    'performances is',
    'performance are',
    'performances were',
    NULL,
    'B',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    369,
    'Only the gifted few with unique _____talents are_____ likely to be remembered favorably by history in two hundred years.',
    '[NO CHANGE]',
    'talent is',
    'talents is',
    'talent was',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    370,
    '_____"Is," he asked_____ with curiosity, "both of your sisters attending the dance?"',
    '[NO CHANGE]',
    'Are, he asked',
    'Was, he asked',
    'Will, he asked',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    371,
    'I have seen businesses moving out of the country ---- avoid paying taxes.',
    'in case',
    'in view of',
    'so as to',
    'although',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    372,
    'We shall take an umbrella with us ---- we don''t get wet.',
    'in case',
    'in order to',
    'so that',
    'despite',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    373,
    'I couldn''t drive there ---- so I told my dad to come along.',
    'me',
    'each other',
    'myself',
    'one another''s',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    374,
    'Before asking ---- people to share their secrets with you, share some of ---- with them.',
    'other / yours',
    'the other / you',
    'some others / yours',
    'others / yours',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    375,
    'By the time you ---- to the party, we ---- for several hours.',
    'have come / had been drinking',
    'come / will be drinking',
    'come / have been drinking',
    'came / had been drinking',
    NULL,
    'D',
    '时态与语态',
    'by the time + 过去时，主句用过去完成时；by the time + 现在时，主句用将来完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    376,
    'Alan ---- all his money when we got to the casino, he didn''t even say hi to us.',
    'ought to lose',
    'should lose',
    'needn''t have lost',
    'can''t have lost',
    NULL,
    'E',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    377,
    'The store ---- by hundreds of people everyday, I don''t understand why my favorite book ---- yet.',
    'visited / hasn''t been bought',
    'is visited / hasn''t been bought',
    'was visited / hadn''t bought',
    'is visited / hadn''t been bought',
    NULL,
    'B',
    '时态与语态',
    'every day 表示经常性动作，用一般现在时。被动语态结构：be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    378,
    'The new movie performed very poorly ---- the low ratings it has received by the viewers.',
    'due to',
    'since',
    'despite',
    'however',
    NULL,
    'A',
    '介词与固定搭配',
    'due to + 名词，表示原因（= because of）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    379,
    'I can see the rainbow clearly now that the rain ----.',
    'stopped',
    'had stopped',
    'stop',
    'has stopped',
    NULL,
    'D',
    '时态与语态',
    'now that 引导原因状语从句，可用现在完成时表示刚完成的动作。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    380,
    'Several guests complained about ---- cold food last night.',
    'to be served',
    'being served',
    'served',
    'having served',
    NULL,
    'B',
    '非谓语动词',
    'complain about / insist on 等短语中，介词后接动名词 (doing)。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    381,
    'I cannot think of anything ---- I would love more than a car right now.',
    'where',
    'who',
    'when',
    'whom',
    NULL,
    'E',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    382,
    'I am excited to see all the historical structures ---- by the Roman Empire.',
    'building',
    'to build',
    'built',
    'to have built',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    383,
    'She would never ask anyone ---- her with her homework, she likes to work alone.',
    'to helping',
    'to help',
    'for helping',
    'help',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    384,
    'The dinosaurs ---- for millions of years before humans appeared on Earth.',
    'were extinct',
    'would be extinct',
    'had been extinct',
    'have been extinct',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    385,
    'The first game ---- by a large margin so the coach said that the preparation for the next game ---- crucial.',
    'lost / had been',
    'was lost / has been',
    'would be lost / would be',
    'was losing / was',
    NULL,
    'E',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    386,
    'Finally after a long meeting, the republican party is set ---- now.',
    'to have been centralized',
    'to be centralized',
    'having been centralized',
    'being centralized',
    NULL,
    'B',
    '非谓语动词',
    '此处需要不定式 to do 表示目的或补充说明。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    387,
    'I think ---- both short term and long term goals in life is important ---- one''s motivation high.',
    'To have / to keep',
    'Having / to keep',
    'To be having / to keep',
    'Having / to keeping',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    388,
    'Rush hour traffic has been a big issue for commuters ever since they closed the second bridge.',
    'will be',
    'was being',
    'is',
    'was',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    389,
    'Sponge Bob is a cartoon character ---- job is a fry cook in the show.',
    'who',
    'whose',
    'which',
    'what',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    390,
    'Jimmy ---- watching football at work, I won''t be surprised if he is fired soon.',
    'must not have been caught',
    'might not have caught',
    'shouldn''t have been caught',
    'needn''t be caught',
    NULL,
    'C',
    '情态动词与虚拟语气',
    '虚拟语气——注意主从句时态配合：与现在相反用 did/were，与过去相反用 had done，主句用 would/could/might have done。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    391,
    'I really don''t want to say anything in that meeting ---- I say something silly.',
    'whether',
    'if',
    'in case',
    'lest',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    392,
    'One day you will understand ---- fortunate you are for having me.',
    'what',
    'when',
    'how',
    'which',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    393,
    'We always need ---- where they came from and how they did it.',
    'to remembering',
    'to remember',
    'remembering',
    'for remembering',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    394,
    'I would have understood you if you ---- why you wanted to move out.',
    'had explained',
    'explaining',
    'have explained',
    'will explain',
    NULL,
    'A',
    '情态动词与虚拟语气',
    'would have done 用于与过去事实相反的虚拟条件句主句。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    395,
    'I have ---- friends in town, so I feel lonely from time to time.',
    'little',
    'a little',
    'a few',
    'few',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    396,
    'I have ---- patience left, so you need to stop complaining.',
    'a little',
    'little',
    'a few',
    'few',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    397,
    'I got into a cab quickly because I ---- by two strange men. As soon as I got into the cab, I ---- a little safer.',
    'was following / felt',
    'have followed / have felt',
    'was being followed / felt',
    'had been following / felt',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    398,
    'My wife always complains about what I buy so I just let her ---- the shopping.',
    'to have done',
    'do',
    'to do',
    'doing',
    NULL,
    'B',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    399,
    'I don''t want to have my tooth ----, I hate dentists.',
    'to be extracted',
    'having extracted',
    'being extracted',
    'extracted',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    400,
    'We didn''t know that a second bridge ----, otherwise we would have gone through the new one.',
    'built',
    'is built',
    'had been built',
    'would be built',
    NULL,
    'C',
    '情态动词与虚拟语气',
    'would have done 用于与过去事实相反的虚拟条件句主句。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    401,
    'I can''t find my house keys, I ---- it at the office.',
    'should have left',
    'have to leave',
    'could leave',
    'must have left ,',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    402,
    'Last time he paid for food, so now I ---- for it.',
    'have had to pay',
    'will have to pay',
    'should have paid',
    'would be paying',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    403,
    'The horrific tragedy ---- if the architect ---- more careful with the way buildings were constructed.',
    'ought to be avoided / was going to be',
    'must have avoided / used to be',
    'may have avoided / has been',
    'will be avoided / will have been',
    NULL,
    'E',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    404,
    'I failed all my finals, I ---- the whole weekend instead of partying.',
    'should have been studying',
    'could have be studying',
    'must have been studying',
    'had been studying',
    NULL,
    'A',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    405,
    'One ---- as hard as one can at a young age before it becomes too late.',
    'might have worked',
    'would work',
    'will have worked',
    'must have worked',
    NULL,
    'E',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    406,
    'Children now have a lot less play time with their friends than they ---- because of all the smart devices developed.',
    'should have',
    'must',
    'ought to',
    'could have',
    NULL,
    'E',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    407,
    'The show ---- longer so that we ---- more time together.',
    'should have lasted / could spend',
    'had better last / were able to spend',
    'might be lasting / can spend',
    'would rather last / spend',
    NULL,
    'A',
    '从句',
    'so that 引导目的状语从句。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    408,
    'Natalia hasn''t responded to any of our messages, she ---- to be with us.',
    'couldn''t have wanted',
    'shouldn''t want',
    'must not want',
    'ought not to have wanted',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    409,
    'The boat ---- but the waves weren''t that big.',
    'must have capsized',
    'will capsize',
    'had to capsize',
    'would have capsized',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    410,
    'Ever since I stopped working, I ---- to save money by ---- at home.',
    'have tried / having being cooked',
    'am trying / cooking',
    'have tried / cook',
    'tried / cooking',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    411,
    'My girlfriend and I ---- to find the right agent who ---- us find the perfect home.',
    'had tried / has help',
    'were trying / will help',
    'will have tried / will help',
    'have been trying / will help',
    NULL,
    'D',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    412,
    'He ---- the one who crashed your car yesterday, he was with me the whole time.',
    'may have been',
    'must have been',
    'shouldn''t have been',
    'can''t have been',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    413,
    'Computer records ---- that they ---- us before the product was shipped.',
    'showed / had paid',
    'has shown / have paid',
    'had shown / will pay',
    'showed / would be paid',
    NULL,
    'A',
    '主谓一致',
    '分析句子结构，找到真正的主语和谓语。定语从句或插入语中的名词可能干扰判断，注意主语核心词的单复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    414,
    'Every time we go there, we take a lot of pictures ---- we take a drive along the beach to see the surfers.',
    'so that',
    'as',
    'whereas',
    'even if',
    NULL,
    'B',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    415,
    'All inclusive resorts are best for families with kids, ---- unmarried couples.',
    'as well as',
    'so as to',
    'consequently',
    'thereby',
    NULL,
    'A',
    '介词与固定搭配',
    'as well as 表示并列（''以及''），类似 and。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    416,
    'Andriy Shevchenko, ---- goals I have watched over and over, was a legendary soccer player.',
    'which',
    'whose',
    'where',
    'what',
    NULL,
    'B',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    417,
    'This is the place ---- James inadvertently had his first kiss.',
    'which',
    'what',
    'where',
    'that',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    418,
    'It is very distracting if the students chatter around while the teacher ----.',
    'would lecture',
    'were lecturing',
    'drove',
    'is lecturing',
    NULL,
    'D',
    '情态动词与虚拟语气',
    '虚拟语气——注意主从句时态配合：与现在相反用 did/were，与过去相反用 had done，主句用 would/could/might have done。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    419,
    'Aristotle was among those ---- tried to prove the Earth was actually spherical and not flat.',
    'whose',
    'to whom',
    'where',
    'who',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    420,
    'Those are the kind of movies ---- many Americans would rate as mature.',
    'which',
    'where',
    'in which',
    'of which',
    NULL,
    'A',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    421,
    'There are known to be total of eight planets in the Solar System ---- is the Earth.',
    'which',
    'that',
    'through which',
    'each of them',
    NULL,
    'E',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    422,
    'Eclipse is the event ---- tonight in North America.',
    'that observed',
    'which are observing',
    'being observed',
    'having observed',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    423,
    'I don''t like ---- by a cop car ---- I am driving alone because it makes me nervous.',
    'being followed / while',
    'being followed / where',
    'to follow / while',
    'to be followed / that',
    NULL,
    'A',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    424,
    '---- I decided not to have a carrier in English, I still want to learn it.',
    'Despite',
    'Although',
    'Due to',
    'Therefore',
    NULL,
    'B',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    425,
    'She suddenly wants to get married, ---- moving out and finding a job in Michigan.',
    'while',
    'moreover',
    'in addition to',
    'including',
    NULL,
    'C',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    426,
    '---- we all know how poor his campaign was, he still won the election.',
    'Because',
    'In case',
    'As long as',
    'Ever since',
    NULL,
    'E',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    427,
    'The teacher advised us to go to every class and turn in our homework assignments timely ---- happens.',
    'due to the fact that',
    'accordingly',
    'however',
    'no matter what',
    NULL,
    'D',
    '代词与限定词',
    '冠词：the 表特指；a/an 表泛指。元音音素前用 an。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    428,
    'Don''t worry, by the time you ---- home I ---- dinner.',
    'came / will make',
    'have come / would have made',
    'come / will have made',
    'had come / made',
    NULL,
    'C',
    '时态与语态',
    'by the time + 过去时，主句用过去完成时；by the time + 现在时，主句用将来完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    429,
    'My father ---- to help out in the house since my mother started working.',
    'had tried',
    'tried',
    'was trying',
    'has been trying',
    NULL,
    'D',
    '时态与语态',
    'since + 过去时间点，主句用现在完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    430,
    '---- having the best player in the league, we lost 3 games in a row.',
    'due to',
    'in spite of the fact that',
    'as',
    'since',
    NULL,
    'E',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    431,
    'I can''t remember very well but I ---- your friend at the bazaar.',
    'may have met',
    'shouldn''t have met',
    'should have met',
    'should meet',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    432,
    'The company hired so many new employees last week, ---- they laid off two workers whose performance hadn''t been satisfactory.',
    'in addition to',
    'as a result of this',
    'on the other hand',
    'consequently',
    NULL,
    'C',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    433,
    'Public schools in the city ---- be free, now half of the people cannot afford them.',
    'are used to',
    'used to',
    'use to',
    'using to',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    434,
    'The dealership has very fancy cars in their inventory, most ---- are very expensive.',
    'that',
    'which',
    'of which',
    'of whose',
    NULL,
    'C',
    '代词与限定词',
    '代词/限定词：注意所指代的名词的单复数和所指关系。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    435,
    'The stand up show we went to see yesterday, ---- made my day, lasted only one hour.',
    'that',
    'who',
    'whom',
    'which',
    NULL,
    'D',
    '从句',
    '非限制性定语从句用 which 引导，不能用 that。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    436,
    'Daniel was exhausted ---- all day playing in the backyard.',
    'spending',
    'having been spending',
    'spent',
    'being spent',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    437,
    'All of these houses, ---- are falling apart already, will be put down.',
    'what',
    'that',
    'for which',
    'which',
    NULL,
    'D',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    438,
    'My cousin shared his lunch with me, ---- was very kind of him.',
    'where',
    'that',
    'who',
    'what',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    439,
    'My English teacher is going to join us for lunch, ---- father is the principle of the school.',
    'which',
    'that',
    'whose',
    'what',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    440,
    'He drove instead of flying ---- he could stop by his uncle on the way there.',
    'so that',
    'in case',
    'hence',
    'however',
    NULL,
    'A',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    441,
    'It doesn''t mean she loves you just ---- she accepted to go to the movies with you.',
    'due to',
    'despite',
    'yet',
    'because',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    442,
    'Angelina has been the girl of your dreams, ----?',
    'hasn''t she',
    'isn''t it',
    'doesn''t she',
    'did she',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    443,
    'When he ---- from the company he ---- for 20 years, he felt devastated.',
    'has been fired / has worked',
    'fired / had worked',
    'fired / worked',
    'was fired / had been working',
    NULL,
    'D',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    444,
    'By the time I ---- enough money, it ---- too late to turn things around.',
    'save / has been',
    'had saved / has been',
    'had saved / was',
    'would save / had been',
    NULL,
    'C',
    '时态与语态',
    'by the time + 过去时，主句用过去完成时；by the time + 现在时，主句用将来完成时。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    445,
    'I have been contemplating ---- or not to take summer classes.',
    'what',
    'why',
    'how',
    'that',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    446,
    'Harry ---- his friends that night so he ---- his parents he was going to be late.',
    'was meeting / told',
    'will meet / will tell',
    'meets / had told',
    'would meet / is going to tell',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    447,
    'When Mary arrived to the party she ---- that we ---- dinner without her.',
    'was disappointed / had been eating',
    'is disappointed / has eaten',
    'has been disappointed / would eat',
    'was disappointed / had eaten',
    NULL,
    'D',
    '从句',
    '分析从句类型（定语/名词性/状语），选择正确的关系词或连接词。注意倒装句的语序要求。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    448,
    'Students ---- online classes at all until the internet has become so widely used.',
    'must not have',
    'didn''t use to have',
    'needn''t have',
    'shouldn''t have',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    449,
    'He still doesn''t believe I won the track and field yesterday, ----?',
    'is he',
    'does he',
    'didn''t I',
    'did I',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    450,
    'Had I ---- my parents advice I wouldn''t be looking into my piggyback for money now.',
    'listen to',
    'have listened to',
    'listened to',
    'would listen to',
    NULL,
    'C',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    451,
    'George is lucky ---- to the conference as he doesn''t even work in our division.',
    'inviting',
    'to be invited',
    'having invited',
    'to have been invited',
    NULL,
    'D',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    452,
    'The accident ---- I told you about occurred in a very busy intersection.',
    'of which',
    'that',
    'about which',
    'where',
    NULL,
    'B',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    453,
    'I don''t want to go to Jessica''s birthday tomorrow ---- I run into bob there.',
    'so that',
    'in case of',
    'although',
    'lest',
    NULL,
    'D',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    454,
    'I wish we had an extra bedroom ---- the guests who want to sleep over.',
    'like',
    'in case',
    'the fact that',
    'whether',
    NULL,
    'E',
    '情态动词与虚拟语气',
    'wish 后接虚拟语气，与现在相反用 did/were，与过去相反用 had done。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    455,
    'Dr. Douglas hates ---- by his first name.',
    'calling',
    'call',
    'having called',
    'be calling',
    NULL,
    'E',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    456,
    'Everyone likes ---- but sometimes we need to work hard ---- one''s trust.',
    'to trust / to earn',
    'trusting / earning',
    'to be trusted / to earn',
    'having trusted / being earned',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    457,
    'The audience ---- left the theater early complained that their money had been wasted, ---- wasn''t enough to get a refund.',
    'which / that',
    'that / where',
    'of whom / who',
    'whom / when',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    458,
    '--- guilty, he had no choice but ---- the fine.',
    'To find / to be paid',
    'Having found / to have paid',
    'To be found / being paid',
    'Found / to pay',
    NULL,
    'E',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    459,
    'He wouldn''t be able to ---- that high even if he ---- which window was her room.',
    'climb / knew',
    'climbing / knew',
    'have climbed / had known',
    'climbing / had known',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    460,
    'The cartoon, ---- she watches everyday, is not very appropriate for her.',
    'that',
    'who',
    'which',
    'where',
    NULL,
    'C',
    '时态与语态',
    'every day 表示经常性动作，用一般现在时。被动语态结构：be + 过去分词。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    461,
    'We will find out soon enough ---- he lied to us about ---- from college.',
    'whether or not / having graduated',
    'in case / having graduated',
    'in case / being graduating',
    'if / having being graduated',
    NULL,
    'A',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    462,
    'The police ---- still interrogating the man who is furious about ---- unlawfully.',
    'are / to detain',
    'is / to have been detained',
    'are / being detained',
    'is / having been detained',
    NULL,
    'D',
    '主谓一致',
    'police/people/cattle 等集合名词通常视为复数，谓语用复数。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    463,
    'After driving for twelve hours straight, I was ---- that I could barely keep my eyes open.',
    'so tired',
    'such a tired',
    'as tired as',
    'the most tired',
    NULL,
    'A',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    464,
    'If you ---- French for 2 years, you ---- enough to have a simple conversion with someone French.',
    'have studied / should know',
    'will study / have known',
    'would study / had known',
    'studied / used to know',
    NULL,
    'A',
    '情态动词与虚拟语气',
    '虚拟语气——注意主从句时态配合：与现在相反用 did/were，与过去相反用 had done，主句用 would/could/might have done。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    465,
    'United States has only a few neighboring countries ---- Portugal or Brazil.',
    'the most',
    'such',
    'like',
    'such as',
    NULL,
    'C',
    '词汇与逻辑',
    '词义辨析——注意语境搭配和近义词的细微差别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    466,
    'Children ---- more time outdoors with their friends before the smartphones ----.',
    'were spending / have been invented',
    'would spend / were invented',
    'would be spending / had invented',
    'have spent / are invented',
    NULL,
    'B',
    '介词与固定搭配',
    '固定搭配或介词用法——注意短语动词中的介词与不定式 to 的区别。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    467,
    'She was surprised to see ---- money I had saved when she came back from her vacation.',
    'so',
    'such',
    'much more',
    'a little',
    NULL,
    'E',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    468,
    '---- earthquake can be devastating if you don''t take ---- necessary precautions.',
    'The / —',
    'Some / any',
    '— / the',
    'Any / a',
    NULL,
    'E',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    469,
    '---- team was compensated generously after winning the championship last year.',
    'Some',
    'Several',
    'The whole',
    'Enough of',
    NULL,
    'C',
    '时态与语态',
    '根据时间状语和上下文判断正确时态，注意主谓一致和语态（主动/被动）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    470,
    'Even though she might be a little annoying at times, she has never yelled at you ----?',
    'is she',
    'has she',
    'hasn''t she',
    'did she',
    NULL,
    'B',
    '从句',
    '状语从句，注意连接词的逻辑关系（因果/让步/条件等）。'
);

INSERT INTO grammar_questions (id, question_text, option_a, option_b, option_c, option_d, option_e, answer, grammar_category, explanation) VALUES (
    471,
    '---- being the last question in the test, this isn''t the easiest one to answer.',
    'Despite the fact that',
    'In spite of the fact that',
    'However',
    'Because',
    NULL,
    'E',
    '非谓语动词',
    '判断非谓语动词形式：主动用 doing，被动用 done，目的用 to do。注意介词后的动词用 doing。'
);