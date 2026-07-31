#!/usr/bin/env node

import fs from "node:fs";
import path from "node:path";
import readline from "node:readline/promises";
import { stdin as input, stdout as output } from "node:process";
import {
  buildBackendMessages,
  containsChineseCharacters,
  loadScenario,
  normalizeSelectedTopic,
  parseAgentReply
} from "./backend-prompt.mjs";

const moduleRoot = path.dirname(new URL(import.meta.url).pathname);

function parseArgs(argv) {
  const args = { scenario: "G-03-hotel", topic: "", temperature: undefined, maxTokens: undefined };
  for (let index = 0; index < argv.length; index += 1) {
    const arg = argv[index];
    const next = argv[index + 1];
    if (arg === "--scenario" && next) {
      args.scenario = next;
      index += 1;
    } else if (arg === "--topic" && next) {
      args.topic = next;
      index += 1;
    } else if (arg === "--temperature" && next) {
      args.temperature = Number(next);
      index += 1;
    } else if (arg === "--max-tokens" && next) {
      args.maxTokens = Number(next);
      index += 1;
    } else if (arg === "--print-system") {
      args.printSystem = true;
    } else if (arg === "--help" || arg === "-h") {
      args.help = true;
    }
  }
  return args;
}

function loadDotEnv(filePath) {
  if (!fs.existsSync(filePath)) return;
  for (const line of fs.readFileSync(filePath, "utf8").split(/\r?\n/)) {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith("#")) continue;
    const separator = trimmed.indexOf("=");
    if (separator === -1) continue;
    const key = trimmed.slice(0, separator).trim();
    const value = trimmed.slice(separator + 1).trim().replace(/^['"]|['"]$/g, "");
    if (key && process.env[key] === undefined) process.env[key] = value;
  }
}

function buildConfig(args) {
  loadDotEnv(path.join(moduleRoot, ".env"));
  return {
    endpoint: (process.env.SJTU_AI_ENDPOINT || "https://models.sjtu.edu.cn/api/v1").replace(/\/$/, ""),
    apiKey: process.env.SJTU_AI_API_KEY || "",
    model: process.env.SJTU_AI_MODEL || "deepseek-chat",
    temperature: Number.isFinite(args.temperature) ? args.temperature : Number(process.env.SJTU_AI_TEMPERATURE || 0.7),
    maxTokens: Number.isFinite(args.maxTokens) ? args.maxTokens : Number(process.env.SJTU_AI_MAX_TOKENS || 120)
  };
}

async function callChatCompletions(config, messages) {
  if (!config.apiKey) {
    throw new Error("Missing SJTU_AI_API_KEY. Set it in llm-prompt-lab/.env or export it in your shell.");
  }
  const response = await fetch(`${config.endpoint}/chat/completions`, {
    method: "POST",
    headers: { Authorization: `Bearer ${config.apiKey}`, "Content-Type": "application/json" },
    body: JSON.stringify({
      model: config.model,
      messages,
      temperature: config.temperature,
      max_tokens: config.maxTokens
    })
  });
  const responseText = await response.text();
  let body = null;
  try {
    body = responseText ? JSON.parse(responseText) : null;
  } catch {
    body = null;
  }
  if (!response.ok) {
    throw new Error(`API request failed (${response.status}): ${body?.error?.message || body?.message || responseText || response.statusText}`);
  }
  const content = body?.choices?.[0]?.message?.content?.trim();
  if (!content) {
    throw new Error(`API response did not include choices[0].message.content: ${responseText}`);
  }
  return content;
}

function listScenarios() {
  return fs.readdirSync(path.join(moduleRoot, "scenarios"))
    .filter((fileName) => fileName.endsWith(".json"))
    .map((fileName) => loadScenario(path.basename(fileName, ".json")))
    .filter((scenario) => !scenario.id.startsWith("IELTS") || [
      "IELTS-P1-practice", "IELTS-P2-practice", "IELTS-P3-practice", "IELTS-mock-test"
    ].includes(scenario.id))
    .sort((left, right) => left.id.localeCompare(right.id));
}

function printReply(reply) {
  console.log(`Agent> ${reply.content}`);
  if (reply.instantTip) console.log(`Tip> ${reply.instantTip}`);
  console.log("");
}

function printHelp() {
  console.log(`
Commands:
  /help       Show this help.
  /scenarios  List available scenarios.
  /system     Print the system prompt for the next normal turn.
  /reload     Reload the scenario JSON and backend-equivalent prompt template.
  /reset      Start a new backend-equivalent session.
  /save       Save the parsed conversation under transcripts/.
  /exit       Exit.
`);
}

function saveTranscript(state) {
  const outputDir = path.join(moduleRoot, "transcripts");
  fs.mkdirSync(outputDir, { recursive: true });
  const timestamp = new Date().toISOString().replace(/[:.]/g, "-");
  const filePath = path.join(outputDir, `${state.scenario.id}-${timestamp}.json`);
  fs.writeFileSync(filePath, JSON.stringify({
    scenario: state.scenario,
    selectedTopic: state.selectedTopic,
    savedAt: new Date().toISOString(),
    messages: state.history
  }, null, 2));
  return filePath;
}

async function requestTurn(config, state, userMessage, turnIndex) {
  const messages = buildBackendMessages({
    scenario: state.scenario,
    selectedTopic: state.selectedTopic,
    history: state.history,
    userMessage,
    turnIndex
  });
  const rawReply = await callChatCompletions(config, messages);
  const reply = parseAgentReply(rawReply);
  if (!reply.content) throw new Error("The model response did not include usable content.");
  if (userMessage.trim()) state.history.push({ role: "user", content: userMessage });
  state.history.push({ role: "assistant", content: reply.content, instantTip: reply.instantTip });
  return reply;
}

function startSession(state) {
  state.history = [];
  state.practiceTurns = 0;
  const opening = { content: state.scenario.openingMessage, instantTip: null };
  state.history.push({ role: "assistant", content: opening.content, instantTip: null });
  printReply(opening);
}

async function main() {
  const args = parseArgs(process.argv.slice(2));
  if (args.help) {
    console.log("Usage: node chat.mjs [--scenario G-03-hotel] [--topic Hometown] [--temperature 0.7] [--max-tokens 120] [--print-system]");
    printHelp();
    return;
  }

  const scenario = loadScenario(args.scenario);
  const selectedTopic = normalizeSelectedTopic(args.topic);
  if (args.printSystem) {
    console.log(buildBackendMessages({ scenario, selectedTopic, turnIndex: 1 })[0].content);
    return;
  }

  const config = buildConfig(args);
  const state = { scenario, selectedTopic, history: [], practiceTurns: 0 };
  console.log(`LLM Prompt Lab: ${scenario.title}`);
  console.log(`Model: ${config.model}`);
  console.log(`Selected topic: ${selectedTopic}`);
  console.log("Prompt source: backend-agent-core.md + backend-equivalent scenario JSON rendering");
  console.log("Type /help for commands.\n");
  startSession(state);

  const rl = readline.createInterface({ input, output });
  try {
    while (true) {
      const userInput = (await rl.question("You> ")).trim();
      if (!userInput) continue;
      if (userInput === "/exit" || userInput === "/quit") break;
      if (userInput === "/help") {
        printHelp();
        continue;
      }
      if (userInput === "/scenarios") {
        for (const item of listScenarios()) console.log(`${item.id}  ${item.title}  (${item.level})`);
        console.log("");
        continue;
      }
      if (userInput === "/system") {
        console.log(`\n${buildBackendMessages({
          scenario: state.scenario,
          selectedTopic: state.selectedTopic,
          history: state.history,
          turnIndex: state.practiceTurns + 1
        })[0].content}\n`);
        continue;
      }
      if (userInput === "/reload") {
        state.scenario = loadScenario(args.scenario);
        console.log("Reloaded scenario JSON and backend-equivalent prompt template.\n");
        continue;
      }
      if (userInput === "/reset") {
        startSession(state);
        continue;
      }
      if (userInput === "/save") {
        console.log(`Saved transcript: ${path.relative(moduleRoot, saveTranscript(state))}\n`);
        continue;
      }

      const chineseHelpTurn = containsChineseCharacters(userInput);
      const turnIndex = chineseHelpTurn ? state.practiceTurns : state.practiceTurns + 1;
      try {
        const reply = await requestTurn(config, state, userInput, turnIndex);
        if (!chineseHelpTurn) state.practiceTurns += 1;
        printReply(reply);
      } catch (error) {
        console.error(`Error: ${error.message}\n`);
      }
    }
  } finally {
    rl.close();
  }
}

main().catch((error) => {
  console.error(error.message);
  process.exitCode = 1;
});
