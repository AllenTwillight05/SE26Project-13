import { afterEach, describe, expect, it, vi } from "vitest";
import { createHttpServices } from "../../../src/services/httpServices";

describe("vocabulary HTTP service", () => {
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("sends the selected practice level as a query parameter", async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      headers: { get: () => "application/json" },
      json: async () => []
    });
    vi.stubGlobal("fetch", fetchMock);

    const services = createHttpServices("http://localhost:8080");
    await services.vocabulary.getVocabularyPracticeWords({ level: "advanced" });

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/vocabulary/practice-words?level=advanced",
      expect.objectContaining({ headers: expect.any(Object) })
    );
  });
});

describe("grammar HTTP service", () => {
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  function stubSuccessfulFetch(body) {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      headers: { get: () => "application/json" },
      json: async () => body
    });
    vi.stubGlobal("fetch", fetchMock);
    return fetchMock;
  }

  it("loads the authenticated user's grammar notebook", async () => {
    const fetchMock = stubSuccessfulFetch([]);
    const services = createHttpServices("http://localhost:8080");

    await services.grammar.getNotebookQuestions();

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/grammar/notebook-questions",
      expect.objectContaining({ headers: expect.any(Object) })
    );
  });

  it("loads practice questions for the selected grammar category", async () => {
    const fetchMock = stubSuccessfulFetch([]);
    const services = createHttpServices("http://localhost:8080");

    await services.grammar.getPracticeQuestions({ category: "时态与语态" });

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/grammar/practice-questions?category=%E6%97%B6%E6%80%81%E4%B8%8E%E8%AF%AD%E6%80%81",
      expect.objectContaining({ headers: expect.any(Object) })
    );
  });

  it("submits whether a grammar answer was incorrect", async () => {
    const fetchMock = stubSuccessfulFetch({ message: "received" });
    const services = createHttpServices("http://localhost:8080");

    await services.grammar.submitGrammarPracticeResult({
      grammarQuestionId: 12,
      incorrect: true
    });

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/grammar/practice-results",
      expect.objectContaining({
        method: "POST",
        body: JSON.stringify({ grammarQuestionId: 12, incorrect: true })
      })
    );
  });

  it("submits a grammar self-rating separately from the answer result", async () => {
    const fetchMock = stubSuccessfulFetch({ message: "received" });
    const services = createHttpServices("http://localhost:8080");

    await services.grammar.submitGrammarRating({
      grammarQuestionId: 12,
      score: 4
    });

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/grammar/practice-ratings",
      expect.objectContaining({
        method: "POST",
        body: JSON.stringify({ grammarQuestionId: 12, score: 4 })
      })
    );
  });

  it("toggles a grammar notebook favorite", async () => {
    const fetchMock = stubSuccessfulFetch({ grammarQuestionId: 12, favorited: true });
    const services = createHttpServices("http://localhost:8080");

    await services.grammar.toggleGrammarFavorite({ grammarQuestionId: 12 });

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/grammar/notebook-favorites",
      expect.objectContaining({
        method: "POST",
        body: JSON.stringify({ grammarQuestionId: 12 })
      })
    );
  });
});
