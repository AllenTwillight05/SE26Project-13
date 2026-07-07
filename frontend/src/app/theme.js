export const appTheme = {
  token: {
    colorPrimary: "#111111",
    colorInfo: "#0f766e",
    colorSuccess: "#0f9f75",
    colorWarning: "#c08a1d",
    colorError: "#c2410c",
    colorBgBase: "#f4f5f7",
    colorTextBase: "#111111",
    fontFamily:
      'Inter, -apple-system, BlinkMacSystemFont, "SF Pro Display", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif',
    borderRadius: 8,
    wireframe: false
  },
  components: {
    Layout: {
      bodyBg: "#f4f5f7",
      headerBg: "rgba(255,255,255,0.72)",
      siderBg: "transparent"
    },
    Card: {
      borderRadiusLG: 8,
      boxShadowTertiary: "0 18px 50px rgba(15, 23, 42, 0.08)"
    },
    Button: {
      borderRadius: 999,
      defaultShadow: "none",
      primaryShadow: "none"
    },
    Segmented: {
      trackBg: "rgba(17, 17, 17, 0.06)",
      itemSelectedBg: "#ffffff"
    },
    Progress: {
      defaultColor: "#111111",
      remainingColor: "rgba(17, 17, 17, 0.08)"
    }
  }
};
