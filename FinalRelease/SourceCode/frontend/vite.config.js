import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    host: "0.0.0.0",
    port: 4173
  },
  test: {
    environment: "jsdom",
    setupFiles: "./test/speaking/setup.js",
    globals: true,
    include: ["test/speaking/unit/**/*.{test,spec}.{js,jsx}"]
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes("node_modules/antd") || id.includes("@ant-design/icons")) {
            return "antd";
          }

          if (id.includes("node_modules/react") || id.includes("node_modules/react-dom")) {
            return "react";
          }

          return undefined;
        }
      }
    }
  }
});
