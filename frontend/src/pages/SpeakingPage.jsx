import { useCallback, useMemo, useState } from "react";
import { Segmented } from "antd";
import { ScenarioGrid } from "../components/Speaking/ScenarioGrid";
import { ScriptPreview } from "../components/Speaking/ScriptPreview";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

export function SpeakingPage() {
  const { speaking } = useAppServices();
  // 页面只负责取数和组合，具体卡片渲染交给 Speaking 私有组件目录。
  const loader = useCallback(() => speaking.getCatalog(), [speaking]);
  const { data, loading, error } = useAsyncData(loader, [loader]);
  const [mode, setMode] = useState();

  const selectedMode = mode ?? data?.modes[0];
  const scenarios = useMemo(() => data?.scenarios ?? [], [data]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Practice Route"
              title="口语练习"
              description="面向餐厅、商务、旅行等真实场景，支持后续接入录音、评估和对话会话接口。"
              extra={
                <Segmented
                  options={data.modes}
                  value={selectedMode}
                  onChange={(value) => setMode(String(value))}
                />
              }
            />
          </section>

          <ScenarioGrid scenarios={scenarios} selectedMode={selectedMode} />

          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Script Preview"
              title={data.scriptPreviewTitle}
              description="这里后续可以接入剧本详情接口、语音合成接口和会话创建接口。"
            />
            <ScriptPreview lines={data.scriptPreviewLines} />
          </section>
        </div>
      ) : null}
    </AsyncPage>
  );
}
