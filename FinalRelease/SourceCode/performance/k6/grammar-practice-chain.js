import http from "k6/http";
import { check, fail } from "k6";
import { Trend, Rate } from "k6/metrics";

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const accounts = (__ENV.ACCOUNTS || "test5:test5password,test6:test6password")
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

const category = __ENV.CATEGORY || "介词与固定搭配";
const questionCount = Number(__ENV.QUESTION_COUNT || 3);

export const grammarFetchQuestionsDuration = new Trend("grammar_fetch_questions_duration", true);
export const grammarAnswerDuration = new Trend("grammar_answer_duration", true);
export const grammarRatingDuration = new Trend("grammar_rating_duration", true);
export const grammarFinishDuration = new Trend("grammar_finish_duration", true);
export const grammarFullChainDuration = new Trend("grammar_full_chain_duration", true);
export const grammarPracticeFailures = new Rate("grammar_practice_failures");

export const options = {
  vus: Number(__ENV.VUS || 1),
  iterations: Number(__ENV.ITERATIONS || 5),
  thresholds: {
    http_req_failed: ["rate<0.05"],
    grammar_practice_failures: ["rate<0.05"],
    grammar_fetch_questions_duration: ["avg<3000"],
    grammar_answer_duration: ["avg<3000"],
    grammar_rating_duration: ["avg<3000"],
    grammar_finish_duration: ["avg<3000"],
    grammar_full_chain_duration: ["avg<10000"]
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
    "login returns token": (res) => res.status === 200 && Boolean(res.json("token"))
  });

  if (!ok) {
    fail(`Login failed with status ${response.status}: ${response.body}`);
  }

  return response.json("token");
}

function fetchPracticeQuestions(token) {
  const response = http.get(
    `${baseUrl}/api/grammar/practice-questions?category=${encodeURIComponent(category)}`,
    jsonHeaders(token)
  );

  grammarFetchQuestionsDuration.add(response.timings.duration);

  const questions = response.status === 200 ? response.json() : null;
  const ok = check(response, {
    "fetch grammar questions status is 200": (res) => res.status === 200,
    "fetch grammar questions returns array": () => Array.isArray(questions),
    "fetch grammar questions returns at least one question": () => Array.isArray(questions) && questions.length > 0
  });

  grammarPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Fetch grammar questions failed with status ${response.status}: ${response.body}`);
  }

  return questions.slice(0, questionCount);
}

function submitPracticeResult(token, question, index) {
  const incorrect = index % 2 === 1;
  const response = http.post(
    `${baseUrl}/api/grammar/practice-results`,
    JSON.stringify({
      grammarQuestionId: question.id,
      incorrect
    }),
    jsonHeaders(token)
  );

  grammarAnswerDuration.add(response.timings.duration);

  const ok = check(response, {
    "submit grammar result status is 200": (res) => res.status === 200,
    "submit grammar result returns message": (res) => res.status === 200 && Boolean(res.json("message"))
  });

  grammarPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Submit grammar practice result failed with status ${response.status}: ${response.body}`);
  }
}

function submitRating(token, question, index) {
  const score = (index % 4) + 1;
  const response = http.post(
    `${baseUrl}/api/grammar/practice-ratings`,
    JSON.stringify({
      grammarQuestionId: question.id,
      score
    }),
    jsonHeaders(token)
  );

  grammarRatingDuration.add(response.timings.duration);

  const ok = check(response, {
    "submit grammar rating status is 200": (res) => res.status === 200,
    "submit grammar rating returns message": (res) => res.status === 200 && Boolean(res.json("message"))
  });

  grammarPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Submit grammar rating failed with status ${response.status}: ${response.body}`);
  }
}

function finishPractice(token) {
  const startedAt = Date.now();
  const progress = http.get(`${baseUrl}/api/grammar/progress`, jsonHeaders(token));
  const overview = http.get(`${baseUrl}/api/grammar/overview`, jsonHeaders(token));

  grammarFinishDuration.add(Date.now() - startedAt);

  const ok = check(progress, {
    "finish grammar progress status is 200": (res) => res.status === 200,
    "finish grammar progress returns completed": (res) => res.status === 200 && res.json("completed") !== undefined
  }) && check(overview, {
    "finish grammar overview status is 200": (res) => res.status === 200
  });

  grammarPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Finish grammar practice failed. progress=${progress.status}, overview=${overview.status}`);
  }
}

export default function () {
  const startedAt = Date.now();
  const token = login(credentialsForVu());
  const questions = fetchPracticeQuestions(token);

  for (let index = 0; index < questions.length; index += 1) {
    submitPracticeResult(token, questions[index], index);
    submitRating(token, questions[index], index);
  }

  finishPractice(token);
  grammarFullChainDuration.add(Date.now() - startedAt);
}
