import { useCallback, useEffect, useMemo, useState } from "react";
import {
  ArrowRightOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  StopOutlined,
  SwapOutlined
} from "@ant-design/icons";
import { Button, Flex, Space, Tag, Typography } from "antd";
import { useNavigate, useParams } from "react-router-dom";
import { AudioToChineseQuestion } from "../components/Vocabulary/AudioToChineseQuestion";
import { ChineseToEnglishQuestion } from "../components/Vocabulary/ChineseToEnglishQuestion";
import { EnglishToChineseQuestion } from "../components/Vocabulary/EnglishToChineseQuestion";
import { VocabularyWordCard } from "../components/Vocabulary/VocabularyWordCard";
import { AsyncPage } from "../components/common/AsyncPage";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Title, Text } = Typography;

const questionTypes = ["englishToChinese", "chineseToEnglish", "audioToChinese"];

const questionTypeLabels = {
  audioToChinese: "语音选中文",
  chineseToEnglish: "中文选英文",
  englishToChinese: "英文选中文"
};

const defaultRating = "良好";

const ratingShortcuts = {
  1: "重来",
  2: "困难",
  3: "良好",
  4: "简单"
};

function getCorrectAnswer(question, questionType) {
  if (questionType === "chineseToEnglish") {
    return question.word;
  }

  return question.briefTranslation;
}

function renderQuestion({ question, questionType, selectedAnswer, answered, onAnswer, onPlayAudio }) {
  const props = {
    answered,
    onAnswer,
    question,
    selectedAnswer
  };

  if (questionType === "chineseToEnglish") {
    return <ChineseToEnglishQuestion {...props} />;
  }

  if (questionType === "audioToChinese") {
    return <AudioToChineseQuestion {...props} onPlayAudio={onPlayAudio} />;
  }

  return <EnglishToChineseQuestion {...props} />;
}

export function VocabularyPracticePage() {
  const navigate = useNavigate();
  const { level = "starter" } = useParams();
  const { vocabulary } = useAppServices();
  const loader = useCallback(() => vocabulary.getVocabularyPracticeWords({ level }), [level, vocabulary]);
  const { data: practiceWords, loading, error } = useAsyncData(loader, [loader]);
  const [questionIndex, setQuestionIndex] = useState(0);
  const [questionTypeIndex, setQuestionTypeIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [selectedRating, setSelectedRating] = useState(defaultRating);

  const currentQuestion = practiceWords?.[questionIndex];
  const questionType = questionTypes[questionTypeIndex];
  const correctAnswer = useMemo(
    () => (currentQuestion ? getCorrectAnswer(currentQuestion, questionType) : ""),
    [currentQuestion, questionType]
  );
  const answered = selectedAnswer !== null;
  const isCorrect = answered && selectedAnswer === correctAnswer;

  useEffect(() => {
    setQuestionIndex(0);
    resetAnswerState();
  }, [level]);

  useEffect(() => {
    function handleRatingShortcut(event) {
      if (!answered || !ratingShortcuts[event.key]) {
        return;
      }

      setSelectedRating(ratingShortcuts[event.key]);
    }

    window.addEventListener("keydown", handleRatingShortcut);

    return () => {
      window.removeEventListener("keydown", handleRatingShortcut);
    };
  }, [answered]);

  function resetAnswerState() {
    setSelectedAnswer(null);
    setSelectedRating(defaultRating);
  }

  function handleSwitchQuestionType() {
    setQuestionTypeIndex((current) => (current + 1) % questionTypes.length);
    resetAnswerState();
  }

  function handleNextQuestion() {
    setQuestionIndex((current) => (current + 1) % practiceWords.length);
    resetAnswerState();
  }

  function handlePlayAudio() {
    if (!window.speechSynthesis) {
      return;
    }

    if (currentQuestion.us_audio) {
      new Audio(currentQuestion.us_audio).play();
      return;
    }

    const utterance = new SpeechSynthesisUtterance(currentQuestion.word);
    utterance.lang = "en-US";
    window.speechSynthesis.cancel();
    window.speechSynthesis.speak(utterance);
  }

  return (
    <AsyncPage loading={loading} error={error}>
      {practiceWords?.length ? (
        <div className="page-stack">
          <section className="practice-shell glass-panel">
            <Flex justify="space-between" align="start" gap={16} wrap>
              <div>
                <Space align="center" wrap>
                  <Tag bordered={false} className="soft-tag soft-tag--dark">
                    {level}
                  </Tag>
                  <Tag bordered={false} className="soft-tag">
                    {questionIndex + 1} / {practiceWords.length}
                  </Tag>
                </Space>
                <Title level={2}>词汇练习</Title>
              </div>
              <Button htmlType="button" icon={<SwapOutlined />} onClick={handleSwitchQuestionType}>
                {questionTypeLabels[questionType]}
              </Button>
            </Flex>

            {renderQuestion({
              answered,
              onAnswer: setSelectedAnswer,
              onPlayAudio: handlePlayAudio,
              question: currentQuestion,
              questionType,
              selectedAnswer
            })}

            {answered ? (
              <div className={isCorrect ? "answer-feedback answer-feedback--correct" : "answer-feedback answer-feedback--wrong"}>
                {isCorrect ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
                <Text strong>{isCorrect ? "回答正确" : `回答错误，正确答案是：${correctAnswer}`}</Text>
              </div>
            ) : null}
          </section>

          {answered ? (
            <VocabularyWordCard
              onRate={setSelectedRating}
              selectedRating={selectedRating}
              word={currentQuestion}
            />
          ) : null}

          <Flex justify="end">
            {answered ? (
              <Button
                htmlType="button"
                icon={<ArrowRightOutlined />}
                onClick={handleNextQuestion}
                size="large"
                type="primary"
              >
                下一题
              </Button>
            ) : (
              <Button
                htmlType="button"
                icon={<StopOutlined />}
                onClick={() => navigate("/vocabulary")}
                size="large"
              >
                结束练习
              </Button>
            )}
          </Flex>
        </div>
      ) : (
        <div className="page-stack">
          <section className="glass-panel">
            <Title level={3}>暂无可练习词汇</Title>
            <Button htmlType="button" onClick={() => navigate("/vocabulary")}>
              返回词汇主页
            </Button>
          </section>
        </div>
      )}
    </AsyncPage>
  );
}
