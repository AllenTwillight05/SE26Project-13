import { screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, describe, expect, it } from "vitest";
import { SpeakingFeedbackPage } from "../../../src/pages/SpeakingFeedbackPage";
import { renderWithProviders } from "../utils/renderWithProviders";

const replayMessages = [
  { role: "coach", text: "Welcome back. What would you like to practice first?", audioUrl: "" },
  { role: "learner", text: "I want to review my meeting opening.", audioUrl: "" },
  { role: "coach", text: "Great. Please start with a short agenda.", audioUrl: "" },
  { role: "learner", text: "First, I will share the project update.", audioUrl: "" }
];

describe("SpeakingFeedbackPage", () => {
  afterEach(() => {
    window.localStorage.clear();
  });

  it("opens the replay modal with chat records and turn segments", async () => {
    window.localStorage.setItem(
      "speaking-history:business-opening",
      JSON.stringify({
        savedAt: "2026-07-09T00:00:00.000Z",
        messages: replayMessages
      })
    );

    renderWithProviders(<SpeakingFeedbackPage />, {
      path: "/speaking/:scenarioId/feedback",
      route: "/speaking/business-opening/feedback"
    });

    await userEvent.click(await screen.findByRole("button", { name: /查看回放/ }));

    const dialog = await screen.findByRole("dialog", { name: /会话回放/ });
    expect(within(dialog).getByText("Welcome back. What would you like to practice first?")).toBeInTheDocument();
    expect(within(dialog).getByText("I want to review my meeting opening.")).toBeInTheDocument();
    expect(within(dialog).getByRole("button", { name: /暂停/ })).toBeInTheDocument();
    expect(within(dialog).getByRole("button", { name: "第 1 轮" })).toBeInTheDocument();
    expect(within(dialog).getByRole("button", { name: "第 2 轮" })).toBeInTheDocument();
  });

  it("can pause and resume replay from the modal controls", async () => {
    renderWithProviders(<SpeakingFeedbackPage />, {
      path: "/speaking/:scenarioId/feedback",
      route: "/speaking/business-opening/feedback"
    });

    await userEvent.click(await screen.findByRole("button", { name: /查看回放/ }));
    const dialog = await screen.findByRole("dialog", { name: /会话回放/ });

    await userEvent.click(within(dialog).getByRole("button", { name: /暂停/ }));
    expect(within(dialog).getByRole("button", { name: /开始/ })).toBeInTheDocument();

    await userEvent.click(within(dialog).getByRole("button", { name: /开始/ }));
    await waitFor(() => {
      expect(within(dialog).getByRole("button", { name: /暂停/ })).toBeInTheDocument();
    });
  });
});
