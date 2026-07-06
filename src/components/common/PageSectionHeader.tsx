import type { ReactNode } from "react";
import { Flex, Typography } from "antd";

const { Title, Text, Paragraph } = Typography;

interface PageSectionHeaderProps {
  eyebrow: string;
  title: string;
  description?: string;
  extra?: ReactNode;
}

export function PageSectionHeader({
  eyebrow,
  title,
  description,
  extra
}: PageSectionHeaderProps) {
  return (
    <Flex justify="space-between" align="start" gap={16} className="section-head">
      <div>
        <Text className="eyebrow">{eyebrow}</Text>
        <Title level={3}>{title}</Title>
        {description ? <Paragraph>{description}</Paragraph> : null}
      </div>
      {extra}
    </Flex>
  );
}

