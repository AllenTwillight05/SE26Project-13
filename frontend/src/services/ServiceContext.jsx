import { createContext, useContext } from "react";

const AppServicesContext = createContext(null);

export function AppServicesProvider({
  services,
  children
}) {
  return (
    <AppServicesContext.Provider value={services}>
      {children}
    </AppServicesContext.Provider>
  );
}

export function useAppServices() {
  // 页面统一从 context 取服务，避免各页面自己决定走 mock 还是 http。
  const services = useContext(AppServicesContext);

  if (!services) {
    throw new Error("AppServicesContext is not available.");
  }

  return services;
}
