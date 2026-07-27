# LLM Prompt Lab

This is the standalone debugging environment for the speaking agent. Its system prompt and message construction intentionally mirror the production implementation in `backend/src/main/java/com/englishlearningcopilot/backend/service/agent/SjtuDeepSeekSpeakingAgentClient.java`.

It does not start Spring Boot or write to the database. It calls the SJTU OpenAI-compatible API directly, using the same scenario JSON that production uses.

## Source Of Truth

The prompt assets used by the production backend are the source of truth. The lab renders its prompt from:

```text
common/agent-contract.md
+ prompts/{scenarioId}-system.md
+ scenarios/{scenarioId}.json for runtime scene facts
+ runtime topic, turn index, input language, and recent-message window
```

`common/agent-contract.md` contains only the response schema, relevance, TTS, and safety boundaries. Each file in `prompts/` owns the dialogue protocol for one scenario. JSON remains structured scene data; it is not used to inject generic conversation-flow, state-rule, or expression-reference arrays into the prompt.

The renderer reproduces these production constraints:

- Returns `{"content": "...", "instantTip": "..."}` only.
- Uses `content` as the persisted assistant history and the TTS text.
- Exposes whether the current input is English or Chinese/mixed; each scenario prompt decides its dialogue behavior.
- Keeps the newest eight messages and summarizes earlier messages to at most 600 characters.

## Run

```bash
cd /mnt/c/SE26Project-13/llm-prompt-lab
node chat.mjs --scenario G-03-hotel
node chat.mjs --scenario IELTS-P1-practice --topic Hometown
```

The CLI starts with the same configured `openingMessage` as the backend, without an LLM request. It displays `Agent>` for `content` and `Tip>` for a non-empty `instantTip`.

Use `--print-system` for a non-networked inspection of the first learner-response prompt:

```bash
node chat.mjs --scenario G-03-hotel --print-system
node chat.mjs --scenario IELTS-P1-practice --topic Hometown --print-system
```

## Configuration

Copy `.env.example` to a private `.env` file and set the required key. Do not commit it.

```text
SJTU_AI_API_KEY=your-key
SJTU_AI_ENDPOINT=https://models.sjtu.edu.cn/api/v1
SJTU_AI_MODEL=deepseek-chat
SJTU_AI_TEMPERATURE=0.7
SJTU_AI_MAX_TOKENS=120
```

The CLI also supports `--temperature` and `--max-tokens`. The defaults now match the backend configuration: `0.7` and `120`.

## Files

- `common/agent-contract.md`: shared output and safety contract.
- `prompts/*.md`: per-scenario dialogue protocols loaded by both the CLI and production backend.
- `backend-prompt.mjs`: production-equivalent prompt composition, history preparation, input-language detection, and response parsing.
- `chat.mjs`: interactive CLI built on the shared renderer.
- `scenarios/*.json`: shared scenario data for role-play and IELTS entries.
- `test-all.mjs`, `test-ielts.mjs`, `retest.mjs`, `demo-conversations.mjs`: API-based regression helpers that now use the shared renderer.

See [USAGE.md](USAGE.md) for operational details.
