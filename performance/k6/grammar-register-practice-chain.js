import http from "k6/http";
import { check, fail } from "k6";
import { Trend, Rate } from "k6/metrics";

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const category = __ENV.CATEGORY || "介词与固定搭配";
const questionCount = Number(__ENV.QUESTION_COUNT || 1);
const userPrefix = __ENV.USER_PREFIX || "test";
const registerUsers = (__ENV.REGISTER_USERS || "true").toLowerCase() !== "false";
const parallelRatings = (__ENV.PARALLEL_RATINGS || "false").toLowerCase() === "true";

let registered = false;

export const grammarRegisterDuration = new Trend("grammar_register_duration", true);
export const grammarLoginDuration = new Trend("grammar_login_duration", true);
export const grammarFetchQuestionsDuration = new Trend("grammar_fetch_questions_duration", true);
export const grammarAnswerDuration = new Trend("grammar_answer_duration", true);
export const grammarRatingDuration = new Trend("grammar_rating_duration", true);
export const grammarFinishDuration = new Trend("grammar_finish_duration", true);
export const grammarProgressDuration = new Trend("grammar_progress_duration", true);
export const grammarOverviewDuration = new Trend("grammar_overview_duration", true);
export const grammarFullChainDuration = new Trend("grammar_full_chain_duration", true);
export const grammarPracticeFailures = new Rate("grammar_practice_failures");
export const grammarRegisterFailures = new Rate("grammar_register_failures");

export const options = {
  vus: Number(__ENV.VUS || 1),
  iterations: Number(__ENV.ITERATIONS || 5),
  thresholds: {
    http_req_failed: ["rate<0.05"],
    grammar_register_failures: ["rate<0.05"],
    grammar_practice_failures: ["rate<0.05"],
    grammar_register_duration: ["avg<3000"],
    grammar_login_duration: ["avg<3000"],
    grammar_fetch_questions_duration: ["avg<3000"],
    grammar_answer_duration: ["avg<3000"],
    grammar_rating_duration: ["avg<3000"],
    grammar_finish_duration: ["avg<3000"],
    grammar_progress_duration: ["avg<3000"],
    grammar_overview_duration: ["avg<3000"],
    grammar_full_chain_duration: ["avg<12000"]
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

  grammarRegisterDuration.add(response.timings.duration);

  const ok = check(response, {
    "register status is 201 or existing 409": (res) => res.status === 201 || res.status === 409,
    "register returns token when created": (res) => res.status !== 201 || Boolean(res.json("token"))
  });

  grammarRegisterFailures.add(!ok);

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

  grammarLoginDuration.add(response.timings.duration);

  const ok = check(response, {
    "login status is 200": (res) => res.status === 200,
    "login returns token": (res) => Boolean(res.json("token"))
  });

  grammarPracticeFailures.add(!ok);

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
    "submit grammar result returns message": (res) => Boolean(res.json("message"))
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
    "submit grammar rating returns message": (res) => Boolean(res.json("message"))
  });

  grammarPracticeFailures.add(!ok);

  if (!ok) {
    fail(`Submit grammar rating failed with status ${response.status}: ${response.body}`);
  }
}

function submitQuestionsInParallel(token, questions) {
  const resultStartedAt = Date.now();
  const resultRequests = questions.map((question, index) => [
    "POST",
    `${baseUrl}/api/grammar/practice-results`,
    JSON.stringify({
      grammarQuestionId: question.id,
      incorrect: index % 2 === 1
    }),
    jsonHeaders(token)
  ]);

  const resultResponses = http.batch(resultRequests);
  grammarAnswerDuration.add(Date.now() - resultStartedAt);

  for (let index = 0; index < resultResponses.length; index += 1) {
    const response = resultResponses[index];
    const ok = check(response, {
      "submit grammar result status is 200": (res) => res.status === 200,
      "submit grammar result returns message": (res) => Boolean(res.json("message"))
    });

    grammarPracticeFailures.add(!ok);

    if (!ok) {
      fail(`Submit grammar practice result failed with status ${response.status}: ${response.body}`);
    }
  }

  const ratingStartedAt = Date.now();
  const ratingRequests = questions.map((question, index) => [
    "POST",
    `${baseUrl}/api/grammar/practice-ratings`,
    JSON.stringify({
      grammarQuestionId: question.id,
      score: (index % 4) + 1
    }),
    jsonHeaders(token)
  ]);

  const ratingResponses = http.batch(ratingRequests);
  grammarRatingDuration.add(Date.now() - ratingStartedAt);

  for (let index = 0; index < ratingResponses.length; index += 1) {
    const response = ratingResponses[index];
    const ok = check(response, {
      "submit grammar rating status is 200": (res) => res.status === 200,
      "submit grammar rating returns message": (res) => Boolean(res.json("message"))
    });

    grammarPracticeFailures.add(!ok);

    if (!ok) {
      fail(`Submit grammar rating failed with status ${response.status}: ${response.body}`);
    }
  }
}

function finishPractice(token) {
  const startedAt = Date.now();
  const progress = http.get(`${baseUrl}/api/grammar/progress`, jsonHeaders(token));
  grammarProgressDuration.add(progress.timings.duration);

  const overview = http.get(`${baseUrl}/api/grammar/overview`, jsonHeaders(token));
  grammarOverviewDuration.add(overview.timings.duration);

  grammarFinishDuration.add(Date.now() - startedAt);

  const ok = check(progress, {
    "finish grammar progress status is 200": (res) => res.status === 200,
    "finish grammar progress returns completed": (res) => res.json("completed") !== undefined
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
  const credentials = credentialsForVu();

  if (registerUsers && !registered) {
    register(credentials);
    registered = true;
  }

  const token = login(credentials);
  const questions = fetchPracticeQuestions(token);

  if (parallelRatings) {
    submitQuestionsInParallel(token, questions);
  } else {
    for (let index = 0; index < questions.length; index += 1) {
      submitPracticeResult(token, questions[index], index);
      submitRating(token, questions[index], index);
    }
  }

  finishPractice(token);
  grammarFullChainDuration.add(Date.now() - startedAt);
}
