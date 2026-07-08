import { CheckCircleFilled } from "@ant-design/icons";
import { List, Tag, Typography } from "antd";
import { PageSectionHeader } from "../common/PageSectionHeader";

const { Text } = Typography;

// 学习计划面板只展示今日任务列表，计划生成和更新交给 profile 服务。
export function LearningPlanPanel({ plan }) {
  return (
    <section className="glass-panel">
      <PageSectionHeader
        eyebrow="Learning Plan"
        title="今日学习计划"
        extra={
          <Tag bordered={false} className="soft-tag">
            自动规划已开启
          </Tag>
        }
      />
      <List
        dataSource={plan.items}
        renderItem={(item) => (
          <List.Item className="plain-list-item">
            <div className="plan-row">
              <div>
                <Text strong>{item.task}</Text>
                <div className="plan-meta">
                  {item.time} · {item.meta}
                </div>
              </div>
              {item.done ? <CheckCircleFilled className="success-icon" /> : null}
            </div>
          </List.Item>
        )}
      />
    </section>
  );
}
