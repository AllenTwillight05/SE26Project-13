import type { PropsWithChildren } from "react";
import { createContext, useContext } from "react";
import type { AppServices } from "./contracts";

const AppServicesContext = createContext<AppServices | null>(null);

interface AppServicesProviderProps extends PropsWithChildren {
  services: AppServices;
}

export function AppServicesProvider({
  services,
  children
}: AppServicesProviderProps) {
  return (
    <AppServicesContext.Provider value={services}>
      {children}
    </AppServicesContext.Provider>
  );
}

export function useAppServices() {
  const services = useContext(AppServicesContext);

  if (!services) {
    throw new Error("AppServicesContext is not available.");
  }

  return services;
}

