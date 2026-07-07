import { useCallback } from "react";
import { Tag } from "antd";
import { GrammarTopicGrid } from "../components/Grammar/GrammarTopicGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

export function GrammarPage() {
  const { grammar } = useAppServices();
  const loader = useCallback(() => grammar.getSnapshot(), [grammar]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Grammar"
              title="语法练习"
              description={data.focus}
              extra={
                <Tag bordered={false} className="soft-tag">
                  句型理解 + 口语应用
                </Tag>
              }
            />
          </section>

          <GrammarTopicGrid topics={data.topics} />
        </div>
      ) : null}
    </AsyncPage>
  );
}
