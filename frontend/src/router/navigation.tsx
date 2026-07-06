import {
  AudioOutlined,
  BookOutlined,
  HomeOutlined,
  ReadOutlined,
  UserOutlined
} from "@ant-design/icons";
import type { ReactNode } from "react";

export interface NavigationItem {
  key: string;
  label: string;
  path: string;
  icon: ReactNode;
}

export const navigationItems: NavigationItem[] = [
  { key: "home", label: "首页", path: "/", icon: <HomeOutlined /> },
  { key: "speaking", label: "口语", path: "/speaking", icon: <AudioOutlined /> },
  { key: "vocabulary", label: "词汇", path: "/vocabulary", icon: <BookOutlined /> },
  { key: "grammar", label: "语法", path: "/grammar", icon: <ReadOutlined /> },
  { key: "profile", label: "个人", path: "/profile", icon: <UserOutlined /> }
];
