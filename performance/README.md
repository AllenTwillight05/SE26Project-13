# Performance tests

This folder contains performance test scripts that are not part of normal unit
or integration test runs.

## Speaking upload full chain

Script:

```powershell
k6 run performance/k6/speaking-upload-chain.js
```

Before running it, put a real browser-recorded WebM/Opus sample at:

```text
performance/data/recording.webm
```

Recommended first run:

```powershell
k6 run --vus 1 --iterations 5 performance/k6/speaking-upload-chain.js
```

Optional variables:

```text
BASE_URL      Backend base URL. Default: http://localhost:8080
ACCOUNTS      Comma-separated account:password list. Default: test1:test1password,test2:test2password
SCENARIO_ID   Speaking scenario id. Default: business-opening
AUDIO_FILE    Audio sample path relative to this script. Default: ../data/recording.webm
DURATION_MS   Duration sent with the audio multipart request. Default: 5000
```

Two users running at the same time:

```powershell
k6 run --vus 2 --iterations 2 performance/k6/speaking-upload-chain.js
```

The script has two default test accounts built in:

```text
test1 / test1password
test2 / test2password
```

You can still override them with `ACCOUNTS` when needed.

The upload endpoint includes the expensive chain: audio persistence, ASR, ISE,
agent reply, optional TTS, and database writes. When real XFYUN services are
enabled, start with low concurrency to avoid vendor-side rate limits.

## Vocabulary practice chain

Script:

```powershell
k6 run --vus 1 --iterations 5 performance/k6/vocabulary-practice-chain.js
```

It simulates: login, fetch vocabulary practice words, submit ratings for a
configurable number of words, then request practice progress and memory data as
the end-of-practice refresh.

Two users running at the same time:

```powershell
k6 run --vus 2 --iterations 2 performance/k6/vocabulary-practice-chain.js
```

Optional variables:

```text
BASE_URL       Backend base URL. Default: http://localhost:8080
ACCOUNTS       Comma-separated account:password list. Default: test1:test1password,test2:test2password
LEVEL          Vocabulary level. Default: starter
QUESTION_COUNT Number of fetched words to answer per iteration. Default: 5
```

## Vocabulary practice chain with registration

Script:

```powershell
k6 run --vus 2 --iterations 2 performance/k6/vocabulary-register-practice-chain.js
```

It simulates: register the VU-owned user, login, fetch vocabulary practice
words, submit ratings, then request practice progress and memory data. Usernames
follow the fixed pattern `test1`, `test2`, etc. The matching email, password,
and display name are `test1@example.com`, `test1password`, and `test1`.
The finish phase is reported both as a combined duration and as separate
`vocabulary_progress_duration` and `vocabulary_memory_duration` metrics.

The script accepts `201 Created` and `409 Conflict` for registration, so it can
be rerun when the same generated users already exist.

Run with existing users only, without calling the register endpoint:

```powershell
k6 run -e BASE_URL=http://10.119.4.34 -e REGISTER_USERS=false --vus 100 --iterations 100 performance/k6/vocabulary-register-practice-chain.js
```

Run with existing users and submit vocabulary ratings in parallel:

```powershell
k6 run -e BASE_URL=http://10.119.4.34 -e REGISTER_USERS=false -e PARALLEL_RATINGS=true --vus 100 --iterations 100 performance/k6/vocabulary-register-practice-chain.js
```

Optional variables:

```text
BASE_URL       Backend base URL. Default: http://localhost:8080
REGISTER_USERS Whether to register generated users before login. Default: true
PARALLEL_RATINGS Whether to submit ratings in one batch per iteration. Default: false
USER_PREFIX    Generated username prefix. Default: test
LEVEL          Vocabulary level. Default: starter
QUESTION_COUNT Number of fetched words to answer per iteration. Default: 5
```

## Grammar practice chain

Script:

```powershell
k6 run --vus 1 --iterations 5 performance/k6/grammar-practice-chain.js
```

It simulates: login, fetch grammar practice questions, submit answer results,
submit ratings, then request grammar progress and overview as the
end-of-practice refresh.

Two users running at the same time:

```powershell
k6 run --vus 2 --iterations 2 performance/k6/grammar-practice-chain.js
```

Optional variables:

```text
BASE_URL       Backend base URL. Default: http://localhost:8080
ACCOUNTS       Comma-separated account:password list. Default: test1:test1password,test2:test2password
CATEGORY       Grammar category. Default: Tense
QUESTION_COUNT Number of fetched questions to answer per iteration. Default: 3
```

## Grammar practice chain with registration

Script:

```powershell
k6 run -e BASE_URL=http://10.119.4.34 -e REGISTER_USERS=false -e PARALLEL_RATINGS=true --vus 100 --iterations 100 performance/k6/grammar-register-practice-chain.js
```

It simulates: register the VU-owned user, login, fetch grammar practice
questions, submit practice results and ratings, then request grammar progress
and overview data. Usernames follow the fixed pattern `test1`, `test2`, etc.
The matching email, password, and display name are `test1@example.com`,
`test1password`, and `test1`.

The script accepts `201 Created` and `409 Conflict` for registration, so it can
be rerun when the same generated users already exist. Set `REGISTER_USERS=false`
to skip registration and directly login generated users.

Optional variables:

```text
BASE_URL         Backend base URL. Default: http://localhost:8080
REGISTER_USERS  Whether to register generated users before login. Default: true
PARALLEL_RATINGS Whether to submit grammar results and ratings in batches. Default: false
USER_PREFIX      Generated username prefix. Default: test
CATEGORY         Grammar category. Default: Tense
QUESTION_COUNT   Number of fetched questions to answer per iteration. Default: 1
```
