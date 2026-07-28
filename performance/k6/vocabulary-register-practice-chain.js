import http from "k6/http";
import { check, fail } from "k6";
import { Trend, Rate } from "k6/metrics";

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const level = __ENV.LEVEL || "starter";
const questionCount = Number(__ENV.QUESTION_COUNT || 1);
const userPrefix = __ENV.USER_PREFIX || "test";
const registerUsers = (__ENV.REGISTER_USERS || "true").toLowerCase() !== "false";
const parallelRatings = (__ENV.PARALLEL_RATINGS || "false").toLowerCase() === "true";

let registered = false;

export const vocabularyRegisterDuration = new Trend("vocabulary_register_duration", true);
export const vocabularyLoginDuration = new Trend("vocabulary_login_duration", true);
export const vocabularyFetchWordsDuration = new Trend("vocabulary_fetch_words_duration", true);
export const vocabularyAnswerDuration = new Trend("vocabulary_answer_duration", true);
export const vocabularyFinishDuration = new Trend("vocabulary_finish_duration", true);
export const vocabularyProgressDuration = new Trend("vocabulary_progress_duration", true);
export const vocabularyMemoryDuration = new Trend("vocabulary_memory_duration", true);
export const vocabularyFullChainDuration = new Trend("vocabulary_full_chain_duration", true);
export const vocabularyPracticeFailures = new Rate("vocabulary_practice_failures");
export const vocabularyRegisterFailures = new Rate("vocabulary_register_failures");

export const options = {
  vus: Number(__ENV.VUS || 1),
  iterations: Number(__ENV.ITERATIONS || 5),
  thresholds: {
    http_req_failed: ["rate<0.05"],
    vocabulary_register_failures: ["rate<0.05"],
    vocabulary_practice_failures: ["rate<0.05"],
    vocabulary_register_duration: ["p(95)<3000"],
    vocabulary_login_duration: ["p(95)<3000"],
    vocabulary_fetch_words_duration: ["p(95)<3000"],
    vocabulary_answer_duration: ["p(95)<3000"],
    vocabulary_finish_duration: ["p(95)<3000"],
    vocabulary_progress_duration: ["p(95)<3000"],
    vocabulary_memory_duration: ["p(95)<3000"],
    vocabulary_full_chain_duration: ["p(95)<12000"]
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
  const username = `${userPrefix}${__VU}`;
  return {
    username,
    email: `${username}@example.com`,
    password: `${username}password`,
    displayName: username
  };
}

function register(credentials) {
  const response = http.post(
    `${baseUrl}/api/auth/register`,
    JSON.stringify(credentials),
    jsonHeaders()
  );

  vocabularyRegisterDuration.add(response.timings.duration);

  const ok = check(response, {
    "register status is 201 or existing 409": (res) => res.status === 201 || res.status === 409,
    "register returns token when created": (res) => res.status !== 201 || Boolean(res.json("token"))
  });

  vocabularyRegisterFailures.add(!ok);

  if (!ok) {
    fail(`Register failed with status ${response.status}: ${response.body}`);
  }
}

function login(credentials) {
  const response = http.post(
    `${baseUrl}/api/auth/login`,
    JSON.stringify({
      account: credentials.username,
      password: credentials.password
    }),
    jsonHeaders()
  );

  vocabularyLoginDuration.add(response.timings.duration);

  const ok = check(response, {
    "login status is 200": (res) => res.status === 200,
    "login returns token": (res) => Boolean(res.json("token"))
  });

  vocabularyPracticeFailures.add(!ok);

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

function answerQuestionsInParallel(token, words) {
  const startedAt = Date.now();
  const requests = words.map((word, index) => {
    const score = (index % 4) + 1;
    return [
      "POST",
      `${baseUrl}/api/vocabulary/practice-ratings`,
      JSON.stringify({
        vocabularyId: word.id,
        score
      }),
      jsonHeaders(token)
    ];
  });

  const responses = http.batch(requests);
  vocabularyAnswerDuration.add(Date.now() - startedAt);

  for (let index = 0; index < responses.length; index += 1) {
    const response = responses[index];
    const ok = check(response, {
      "submit rating status is 200": (res) => res.status === 200,
      "submit rating returns message": (res) => Boolean(res.json("message"))
    });

    vocabularyPracticeFailures.add(!ok);

    if (!ok) {
      fail(`Submit vocabulary rating failed with status ${response.status}: ${response.body}`);
    }
  }
}

function finishPractice(token) {
  const startedAt = Date.now();
  const progress = http.get(`${baseUrl}/api/vocabulary/practice-progress`, jsonHeaders(token));
  vocabularyProgressDuration.add(progress.timings.duration);

  const memory = http.get(`${baseUrl}/api/vocabulary/memory`, jsonHeaders(token));
  vocabularyMemoryDuration.add(memory.timings.duration);

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
  const credentials = credentialsForVu();

  if (registerUsers && !registered) {
    register(credentials);
    registered = true;
  }

  const token = login(credentials);
  const words = fetchPracticeWords(token);

  if (parallelRatings) {
    answerQuestionsInParallel(token, words);
  } else {
    for (let index = 0; index < words.length; index += 1) {
      answerQuestion(token, words[index], index);
    }
  }

  finishPractice(token);
  vocabularyFullChainDuration.add(Date.now() - startedAt);
}
