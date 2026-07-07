import { useCallback } from "react";
import { FeedbackPanel } from "../components/Profile/FeedbackPanel";
import { LearningPlanPanel } from "../components/Profile/LearningPlanPanel";
import { ProfileHero } from "../components/Profile/ProfileHero";
import { ProgressPanel } from "../components/Profile/ProgressPanel";
import { AsyncPage } from "../components/common/AsyncPage";
import { useAsyncData } from "../hooks/useAsyncData";
import { useAppServices } from "../services/ServiceContext";

export function ProfilePage() {
  const { profile } = useAppServices();
  const loader = useCallback(() => profile.getSnapshot(), [profile]);
  const { data, loading, error } = useAsyncData(loader, [loader]);

  return (
    <AsyncPage loading={loading} error={error}>
      {data ? (
        <div className="page-stack">
          <ProfileHero profile={data} />

          <section className="split-panel">
            <LearningPlanPanel plan={data.dailyPlan} />
            <ProgressPanel progress={data.dailyPlan.progress} />
          </section>

          <FeedbackPanel feedback={data.feedback} />
        </div>
      ) : null}
    </AsyncPage>
  );
}
