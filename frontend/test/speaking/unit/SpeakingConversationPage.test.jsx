import { screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, describe, expect, it, vi } from "vitest";
import { SpeakingConversationPage } from "../../../src/pages/SpeakingConversationPage";
import { renderWithProviders } from "../utils/renderWithProviders";

describe("SpeakingConversationPage", () => {
  afterEach(() => {
    window.localStorage.clear();
    vi.clearAllMocks();
  });

  it("renders one playback button per bubble and speaks a clicked message", async () => {
    renderWithProviders(<SpeakingConversationPage />, {
      path: "/speaking/:scenarioId/conversation",
      route: "/speaking/business-opening/conversation"
    });

    const playbackButtons = await screen.findAllByRole("button", { name: "播放此句音频" });
    expect(playbackButtons).toHaveLength(4);

    await userEvent.click(playbackButtons[0]);
    expect(window.speechSynthesis.speak).toHaveBeenCalledTimes(1);
  });

  it("counts recording rounds, enables submit after three rounds, and writes local history", async () => {
    renderWithProviders(<SpeakingConversationPage />, {
      path: "/speaking/:scenarioId/conversation",
      route: "/speaking/business-opening/conversation"
    });

    const submitButton = await screen.findByRole("button", { name: /交卷/ });
    expect(submitButton).toBeDisabled();

    for (let count = 1; count <= 3; count += 1) {
      await userEvent.click(screen.getByRole("button", { name: /开始录音/ }));
      expect(await screen.findByText(`当前为第 ${count} 轮`)).toBeInTheDocument();
      await userEvent.click(screen.getByRole("button", { name: /停止录音/ }));
    }

    expect(submitButton).toBeEnabled();
    await userEvent.click(submitButton);

    await waitFor(() => {
      expect(window.localStorage.getItem("speaking-history:business-opening")).toEqual(
        expect.stringContaining("Good morning. Could you briefly introduce today's agenda?")
      );
    });
    expect(await screen.findByText("反馈页占位")).toBeInTheDocument();
  });
});
