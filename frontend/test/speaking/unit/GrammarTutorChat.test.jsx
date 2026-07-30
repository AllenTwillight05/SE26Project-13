import { screen, render } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { GrammarTutorChat } from "../../../src/components/Grammar/GrammarTutorChat";

describe("GrammarTutorChat", () => {
  it("sends the current question context and renders the tutor reply", async () => {
    const onSend = vi.fn().mockResolvedValue({
      reply: "因为这里需要看主句和从句的关系。",
      relatedMistakeCount: 2
    });
    render(<GrammarTutorChat onSend={onSend} questionId={12} selectedAnswer="B" />);

    const input = screen.getByRole("textbox", { name: "向语法导师提问" });
    await userEvent.type(input, "为什么不能选 B？");
    await userEvent.click(screen.getByRole("button", { name: "发送问题" }));

    expect(onSend).toHaveBeenCalledWith({
      grammarQuestionId: 12,
      selectedAnswer: "B",
      message: "为什么不能选 B？",
      history: []
    });
    expect(await screen.findByText("因为这里需要看主句和从句的关系。" )).toBeInTheDocument();
    expect(screen.getByText(/参考你在同类语法中的 2 道/)).toBeInTheDocument();
  });

  it("does not submit when the input is blank", async () => {
    const onSend = vi.fn();
    render(<GrammarTutorChat onSend={onSend} questionId={12} selectedAnswer="A" />);

    expect(screen.getByRole("button", { name: "发送问题" })).toBeDisabled();
    await userEvent.keyboard("{Enter}");
    expect(onSend).not.toHaveBeenCalled();
  });
});
