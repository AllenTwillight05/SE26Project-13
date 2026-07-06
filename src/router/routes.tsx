import type { RouteObject } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { AppLayout } from "../layouts/AppLayout";
import { DashboardPage } from "../pages/DashboardPage";
import { GrammarPage } from "../pages/GrammarPage";
import { ProfilePage } from "../pages/ProfilePage";
import { SpeakingPage } from "../pages/SpeakingPage";
import { VocabularyPage } from "../pages/VocabularyPage";

export const appRoutes: RouteObject[] = [
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { index: true, element: <DashboardPage /> },
      { path: "speaking", element: <SpeakingPage /> },
      { path: "vocabulary", element: <VocabularyPage /> },
      { path: "grammar", element: <GrammarPage /> },
      { path: "profile", element: <ProfilePage /> },
      { path: "practice", element: <Navigate to="/speaking" replace /> },
      { path: "feedback", element: <Navigate to="/profile" replace /> },
      { path: "plan", element: <Navigate to="/profile" replace /> }
    ]
  },
  {
    path: "*",
    element: <Navigate to="/" replace />
  }
];
