import { useCallback, useMemo, useState } from "react";
import { Segmented } from "antd";
import { useNavigate } from "react-router-dom";
import { ScenarioGrid } from "../components/Speaking/ScenarioGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

export function SpeakingPage() {
  const navigate = useNavigate();
  const { speaking } = useAppServices();
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
              eyebrow="Scenario Grid"
              title="口语练习"
              description="选择一个真实情景模块，进入详情页后再开始会话练习。"
              extra={
                <Segmented
                  options={data.modes}
                  value={selectedMode}
                  onChange={(value) => setMode(String(value))}
                />
              }
            />
          </section>

          <ScenarioGrid
            scenarios={scenarios}
            selectedMode={selectedMode}
            onSelect={(scenario) => navigate(`/speaking/${scenario.id}`)}
          />
        </div>
      ) : null}
    </AsyncPage>
  );
}
