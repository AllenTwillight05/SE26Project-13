import { Flex, Typography } from "antd";

const { Title, Text, Paragraph } = Typography;

export function PageSectionHeader({
  eyebrow,
  title,
  description,
  extra
}) {
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
