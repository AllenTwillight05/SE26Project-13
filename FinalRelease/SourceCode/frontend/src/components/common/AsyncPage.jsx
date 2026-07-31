import { Result, Skeleton } from "antd";

export function AsyncPage({ loading, error, children }) {
  if (loading) {
    return (
      <div className="page-stack">
        <div className="glass-panel">
          <Skeleton active paragraph={{ rows: 8 }} />
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="page-stack">
        <Result
          status="error"
          title="页面数据加载失败"
          subTitle={error.message || "请稍后重试。"}
        />
      </div>
    );
  }

  return <>{children}</>;
}
