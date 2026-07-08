import { useCallback, useMemo, useState } from "react";
import {
  ArrowLeftOutlined,
  AudioOutlined,
  PauseCircleOutlined,
  SendOutlined,
  SoundOutlined
} from "@ant-design/icons";
import { Button, Flex, Progress, Space, Tag, Typography } from "antd";
import { useNavigate, useParams } from "react-router-dom";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

const { Text, Title } = Typography;

export function SpeakingConversationPage() {
  const navigate = useNavigate();
  const { scenarioId } = useParams();
  const { speaking } = useAppServices();
  const loader = useCallback(() => speaking.getCatalog(), [speaking]);
  const { data, loading, error } = useAsyncData(loader, [loader]);
  const [isRecording, setIsRecording] = useState(false);
  const [turnCount, setTurnCount] = useState(0);

  const scenario = useMemo(
    () => data?.scenarios.find((item) => item.id === scenarioId),
    [data, scenarioId]
  );

  const startRecording = useCallback(() => {
    setIsRecording(true);
    setTurnCount((current) => Math.min(current + 1, 3));
  }, []);

  return (
    <AsyncPage loading={loading} error={error}>
      {scenario ? (
        <div className="page-stack">
          <section className="glass-panel speaking-session-panel">
            <PageSectionHeader
              eyebrow="Conversation Mode"
              title="微信对话模式"
              description={scenario.title}
              extra={
                <Space wrap>
                  <Tag bordered={false} className="soft-tag">
                    已完成 {turnCount} / 3 轮
                  </Tag>
                  <Tag bordered={false} className="soft-tag soft-tag--dark">
                    {scenario.level}
                  </Tag>
                </Space>
              }
            />

            <div className="chat-window chat-window--page">
              {scenario.prompts.map((message, index) => (
                <div
                  className={`chat-bubble-row chat-bubble-row--${message.role}`}
                  key={`${message.role}-${index}`}
                >
                  <div className={`chat-bubble chat-bubble--${message.role}`}>{message.text}</div>
                </div>
              ))}
            </div>

            <div className={`recorder-strip ${isRecording ? "recorder-strip--recording" : ""}`}>
              <div className="recorder-strip__label">
                <SoundOutlined />
                <span>{isRecording ? "正在采集你的回答" : "点击开始录音，模拟完成一轮回答"}</span>
              </div>
              <div className="waveform-bars" aria-hidden="true">
                {Array.from({ length: 28 }).map((_, index) => (
                  <span key={index} style={{ "--bar": `${18 + ((index * 13) % 34)}px` }} />
                ))}
              </div>
              {isRecording ? <Progress percent={66} showInfo={false} status="active" /> : null}
            </div>

            <Flex justify="space-between" gap={12} wrap="wrap">
              <Space wrap>
                <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/speaking/${scenario.id}`)}>
                  返回详情
                </Button>
                <Button
                  type="primary"
                  icon={<AudioOutlined />}
                  onClick={startRecording}
                  disabled={isRecording}
                >
                  开始录音
                </Button>
                <Button
                  icon={<PauseCircleOutlined />}
                  onClick={() => setIsRecording(false)}
                  disabled={!isRecording}
                >
                  停止录音
                </Button>
              </Space>
              <Button
                type="primary"
                icon={<SendOutlined />}
                onClick={() => navigate(`/speaking/${scenario.id}/feedback`)}
              >
                交卷
              </Button>
            </Flex>
            <Text type="secondary">当前为前端模拟录音流程，不会调用真实麦克风或上传音频。</Text>
          </section>
        </div>
      ) : data ? (
        <section className="glass-panel">
          <PageSectionHeader
            eyebrow="Scenario Missing"
            title="没有找到这个情景"
            description="请返回口语页重新选择一个情景模块。"
          />
          <Button icon={<ArrowLeftOutlined />} onClick={() => navigate("/speaking")}>
            返回口语页
          </Button>
        </section>
      ) : null}
    </AsyncPage>
  );
}
