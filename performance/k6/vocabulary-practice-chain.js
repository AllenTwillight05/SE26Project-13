import http from "k6/http";
import { check, fail } from "k6";
import { Trend, Rate } from "k6/metrics";

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const accounts = (__ENV.ACCOUNTS || "test3:test3password,test4:test4password")
  .split(",")
  .map((entry) => entry.trim())
  .filter(Boolean)
  .map((entry) => {
    const separatorIndex = entry.indexOf(":");
    if (separatorIndex <= 0 || separatorIndex === entry.length - 1) {
      fail(`Invalid ACCOUNTS entry: ${entry}. Expected account:password.`);
    }
    return {
      account: entry.slice(0, separatorIndex),
      password: entry.slice(separatorIndex + 1)
    };
  });

const level = __ENV.LEVEL || "starter";
const questionCount = Number(__ENV.QUESTION_COUNT || 5);

export const vocabularyFetchWordsDuration = new Trend("vocabulary_fetch_words_duration", true);
export const vocabularyAnswerDuration = new Trend("vocabulary_answer_duration", true);
export const vocabularyFinishDuration = new Trend("vocabulary_finish_duration", true);
export const vocabularyFullChainDuration = new Trend("vocabulary_full_chain_duration", true);
export const vocabularyPracticeFailures = new Rate("vocabulary_practice_failures");

export const options = {
  vus: Number(__ENV.VUS || 1),
  iterations: Number(__ENV.ITERATIONS || 5),
  thresholds: {
    http_req_failed: ["rate<0.05"],
    vocabulary_practice_failures: ["rate<0.05"],
    vocabulary_fetch_words_duration: ["p(95)<3000"],
    vocabulary_answer_duration: ["p(95)<3000"],
    vocabulary_finish_duration: ["p(95)<3000"],
    vocabulary_full_chain_duration: ["p(95)<10000"]
  }
};

function jsonHeaders(token) {
  return {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {})
    }
  };
}

function credentialsForVu() {
  return accounts[(__VU - 1) % accounts.length];
}

function login(credentials) {
  const response = http.post(
    `${baseUrl}/api/auth/login`,
    JSON.stringify(credentials),
    jsonHeaders()
  );

  const ok = check(response, {
    "login status is 200": (res) => res.status === 200,
    "login returns token": (res) => Boolean(res.json("token"))
  });

  if (!ok) {
    fail(`Login failed with status ${response.status}: ${response.body}`);
  }

  return response.json("token");
}

function fetchPracticeWords(token) {
  const response = http.get(
    `${baseUrl}/api/vocabulary/practice-words?level=${encodeURIComponent(level)}`,
    jsonHeaders(token)
  );

  vocabularyFetchWordsDuration.add(response.timings.duration);

  const words = response.json();
  const ok = check(response, {
    "fetch practice words status is 200": (res) => res.status === 200,
    "fetch practice words returns array": () => Array.isArray(words),
    "fetch practice words returns at least one word": () => Array.isArray(words) && words.length > 0
  });

  vocabularyPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Fetch practice words failed with status ${response.status}: ${response.body}`);
  }

  return words.slice(0, questionCount);
}

function answerQuestion(token, word, index) {
  const score = (index % 4) + 1;
  const response = http.post(
    `${baseUrl}/api/vocabulary/practice-ratings`,
    JSON.stringify({
      vocabularyId: word.id,
      score
    }),
    jsonHeaders(token)
  );

  vocabularyAnswerDuration.add(response.timings.duration);

  const ok = check(response, {
    "submit rating status is 200": (res) => res.status === 200,
    "submit rating returns message": (res) => Boolean(res.json("message"))
  });

  vocabularyPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Submit vocabulary rating failed with status ${response.status}: ${response.body}`);
  }
}

function finishPractice(token) {
  const startedAt = Date.now();
  const progress = http.get(`${baseUrl}/api/vocabulary/practice-progress`, jsonHeaders(token));
  const memory = http.get(`${baseUrl}/api/vocabulary/memory`, jsonHeaders(token));

  vocabularyFinishDuration.add(Date.now() - startedAt);

  const ok = check(progress, {
    "finish progress status is 200": (res) => res.status === 200,
    "finish progress returns completed": (res) => res.json("completed") !== undefined
  }) && check(memory, {
    "finish memory status is 200": (res) => res.status === 200
  });

  vocabularyPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Finish vocabulary practice failed. progress=${progress.status}, memory=${memory.status}`);
  }
}

export default function () {
  const startedAt = Date.now();
  const token = login(credentialsForVu());
  const words = fetchPracticeWords(token);

  for (let index = 0; index < words.length; index += 1) {
    answerQuestion(token, words[index], index);
  }

  finishPractice(token);
  vocabularyFullChainDuration.add(Date.now() - startedAt);
}
