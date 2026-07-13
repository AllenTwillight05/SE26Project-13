import { screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, describe, expect, it } from "vitest";
import { SpeakingScenarioDetailPage } from "../../../src/pages/SpeakingScenarioDetailPage";
import { renderWithProviders } from "../utils/renderWithProviders";

const historyKey = "speaking-history:business-opening";

describe("SpeakingScenarioDetailPage", () => {
  afterEach(() => {
    window.localStorage.clear();
  });

  it("disables the history button when no prior practice exists", async () => {
    renderWithProviders(<SpeakingScenarioDetailPage />);

    const historyButton = await screen.findByRole("button", { name: /历史记录/ });
    expect(historyButton).toBeDisabled();
  });

  it("opens feedback from history when prior practice exists", async () => {
    window.localStorage.setItem(
      historyKey,
      JSON.stringify({
        savedAt: "2026-07-09T00:00:00.000Z",
        messages: []
      })
    );

    renderWithProviders(<SpeakingScenarioDetailPage />);

    const historyButton = await screen.findByRole("button", { name: /历史记录/ });
    expect(historyButton).toBeEnabled();

    await userEvent.click(historyButton);

    await waitFor(() => {
      expect(screen.getByText("反馈页占位")).toBeInTheDocument();
    });
  });
});
