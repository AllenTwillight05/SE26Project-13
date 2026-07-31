import http from "k6/http";
import { check, fail } from "k6";
import { Trend, Rate } from "k6/metrics";

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const accounts = (__ENV.ACCOUNTS || "test2:test2password")
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
const scenarioId = __ENV.SCENARIO_ID || "airport-checkin";
const audioPath = __ENV.AUDIO_FILE || "../data/recording.webm";
const durationMs = __ENV.DURATION_MS || "5000";

const audioFile = open(audioPath, "b");

export const speakingUploadDuration = new Trend("speaking_upload_duration", true);
export const speakingFullChainDuration = new Trend("speaking_full_chain_duration", true);
export const speakingUploadFailures = new Rate("speaking_upload_failures");

export const options = {
  vus: Number(__ENV.VUS || 1),
  iterations: Number(__ENV.ITERATIONS || 5),
  thresholds: {
    http_req_failed: ["rate<0.05"],
    speaking_upload_failures: ["rate<0.05"],
    speaking_upload_duration: ["p(95)<45000"],
    speaking_full_chain_duration: ["p(95)<50000"]
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

function createSpeakingSession(token) {
  const response = http.post(
    `${baseUrl}/api/speaking/sessions`,
    JSON.stringify({ scenarioId }),
    jsonHeaders(token)
  );

  const ok = check(response, {
    "create session status is 201": (res) => res.status === 201,
    "create session returns id": (res) => Boolean(res.json("id"))
  });

  if (!ok) {
    fail(`Create speaking session failed with status ${response.status}: ${response.body}`);
  }

  return response.json("id");
}

function submitRecording(token, sessionId) {
  const response = http.post(
    `${baseUrl}/api/speaking/sessions/${sessionId}/messages`,
    {
      audio: http.file(audioFile, "recording.webm", "audio/webm"),
      durationMs
    },
    {
      headers: {
        Authorization: `Bearer ${token}`
      },
      timeout: "90s"
    }
  );

  speakingUploadDuration.add(response.timings.duration);

  const ok = check(response, {
    "submit recording status is 200": (res) => res.status === 200,
    "submit recording returns user message": (res) => Boolean(res.json("userMessage.id")),
    "submit recording returns agent message": (res) => Boolean(res.json("agentMessage.id")),
    "submit recording returns transcript": (res) => Boolean(res.json("userMessage.transcribedText") || res.json("userMessage.content"))
  });

  speakingUploadFailures.add(!ok);

  if (!ok) {
    fail(`Submit recording failed with status ${response.status}: ${response.body}`);
  }
}

export default function () {
  const startedAt = Date.now();
  const credentials = credentialsForVu();
  const token = login(credentials);
  const sessionId = createSpeakingSession(token);
  submitRecording(token, sessionId);
  speakingFullChainDuration.add(Date.now() - startedAt);
}
