# LLM Prompt Lab Usage

## Purpose

Use this directory to inspect and exercise the exact speaking-agent prompt shape sent by the production backend. It is intentionally independent of Spring Boot, but it is not an alternative prompt design surface.

Production source of truth:

```text
backend/src/main/java/com/englishlearningcopilot/backend/service/agent/SjtuDeepSeekSpeakingAgentClient.java
```

The corresponding lab renderer is `backend-prompt.mjs`. Keep them aligned whenever the backend changes.

## Starting A Session

```bash
cd /mnt/c/SE26Project-13/llm-prompt-lab
node chat.mjs --scenario G-03-hotel
node chat.mjs --scenario IELTS-P1-practice --topic Hometown
```

`--topic` corresponds to the session's selected topic or cue card. Omit it to use the production default, `Not selected.`

The CLI starts with the configured scenario opening line, matching the backend's fast `createSession` path. Every learner input rebuilds the system message from the shared contract, the selected scenario prompt, runtime scene facts, and recent history, just as the backend does.

CLI commands:

- `/system`: print the system prompt that a normal next turn would use.
- `/reload`: reload the scenario JSON and static prompt template.
- `/reset`: create a new simulated session and request its opening turn.
- `/save`: write the parsed conversation to `transcripts/`.
- `/scenarios`, `/help`, `/exit`: list scenarios, show help, and exit.

## Prompt Inspection

No API key or network access is required for this check:

```bash
node chat.mjs --scenario G-03-hotel --print-system
node chat.mjs --scenario IELTS-P2-practice --topic "Describe a person you admire" --print-system
```

The output must contain the shared contract plus the selected scenario's prompt protocol. It must not inject the old generic `conversationFlow`, `stateRules`, `errorHandling`, or expression-reference arrays as a substitute for scenario behavior.

## Editing Rules

- Change `common/agent-contract.md` only for shared response-format, relevance, TTS, or safety requirements.
- Change `prompts/{scenarioId}-system.md` for that scenario's role, dialogue flow, completion, feedback, and language-help behavior.
- Change `scenarios/*.json` for structured scene facts and UI data.
- Change `backend-prompt.mjs` whenever Java changes prompt composition, history limits, input-language detection, or JSON parsing.
- Every scenario prompt is loaded by production; do not place production behavior only in the CLI.
- After a change, inspect at least one G-series and one IELTS system prompt with `--print-system`.

## Regression Helpers

The API-based helpers use the shared production-equivalent renderer:

```bash
node test-all.mjs
node test-ielts.mjs
node retest.mjs
node demo-conversations.mjs
```

They need `SJTU_AI_API_KEY` and access to the SJTU API. They write logs to `test-outputs/`; they do not modify application data.

For an offline structural check, run:

```bash
node --check backend-prompt.mjs
node --check chat.mjs
node chat.mjs --scenario G-03-hotel --print-system
node chat.mjs --scenario IELTS-P1-practice --topic Hometown --print-system
```
