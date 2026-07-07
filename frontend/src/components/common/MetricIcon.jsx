import {
  AudioOutlined,
  ClockCircleOutlined,
  FireOutlined,
  RiseOutlined,
  SoundOutlined
} from "@ant-design/icons";

const iconMap = {
  microphone: <AudioOutlined />,
  waveform: <SoundOutlined />,
  streak: <FireOutlined />,
  trend: <RiseOutlined />,
  clock: <ClockCircleOutlined />
};

export function MetricIcon({ icon }) {
  return <>{iconMap[icon]}</>;
}
