import {
  AudioOutlined,
  ClockCircleOutlined,
  FireOutlined,
  RiseOutlined,
  SoundOutlined
} from "@ant-design/icons";
import type { QuickStat } from "../../services/contracts";

const iconMap: Record<QuickStat["icon"], React.ReactNode> = {
  microphone: <AudioOutlined />,
  waveform: <SoundOutlined />,
  streak: <FireOutlined />,
  trend: <RiseOutlined />,
  clock: <ClockCircleOutlined />
};

export function MetricIcon({ icon }: Pick<QuickStat, "icon">) {
  return <>{iconMap[icon]}</>;
}

