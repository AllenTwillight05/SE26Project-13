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
