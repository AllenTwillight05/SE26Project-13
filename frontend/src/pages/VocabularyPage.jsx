import { useCallback } from "react";
import { Tag } from "antd";
import { VocabularyDeckGrid } from "../components/Vocabulary/VocabularyDeckGrid";
import { AsyncPage } from "../components/common/AsyncPage";
import { PageSectionHeader } from "../components/common/PageSectionHeader";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

export function VocabularyPage() {
  const { vocabulary } = useAppServices();
  const loader = useCallback(() => vocabulary.getSnapshot(), [vocabulary]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <section className="glass-panel">
            <PageSectionHeader
              eyebrow="Vocabulary Route"
              title="词汇强化卡组"
              description="适合后续接入词库、错词本、复习队列和 TTS 发音服务。"
              extra={
                <Tag bordered={false} className="soft-tag">
                  {data.retentionHint}
                </Tag>
              }
            />
          </section>

          <VocabularyDeckGrid cards={data.cards} />
        </div>
      ) : null}
    </AsyncPage>
  );
}
