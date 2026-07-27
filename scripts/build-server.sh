#!/usr/bin/env bash

# Build deployable frontend and backend artifacts from a checked-out repository.
# This script deliberately does not pull Git changes, publish files, or restart
# services. The current server test suites are not release gates, so tests run
# only when explicitly requested with --with-tests.

set -Eeuo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
frontend_dir="$repo_root/frontend"
backend_dir="$repo_root/backend"
with_tests=false

usage() {
  cat <<'EOF'
Usage: scripts/build-server.sh [--with-tests]

Builds the backend JAR and frontend distribution from the current checkout.
Tests are skipped by default because the server's legacy suites are not yet
maintained as release gates. Pass --with-tests only when they are known to pass.
EOF
}

while (($# > 0)); do
  case "$1" in
    --with-tests)
      with_tests=true
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      printf 'Unknown option: %s\n' "$1" >&2
      usage >&2
      exit 2
      ;;
  esac
  shift
done

require_command() {
  local command_name="$1"
  if ! command -v "$command_name" >/dev/null 2>&1; then
    printf 'Missing required command: %s\n' "$command_name" >&2
    exit 1
  fi
}

require_supported_node() {
  local version major minor patch
  version="$(node --version | sed 's/^v//')"
  IFS='.' read -r major minor patch <<<"$version"

  if ((major < 20 || (major == 20 && minor < 19))); then
    printf 'Node.js %s is unsupported. Vite 8 requires Node.js >= 20.19 or >= 22.12.\n' "$version" >&2
    exit 1
  fi
}

require_command git
require_command node
require_command npm
require_command java
require_command mvn
require_supported_node

# Production defaults: real authentication API, mock data for unfinished modules.
# Callers can override any VITE_* variable when a module's backend is ready.
export VITE_API_MODE="${VITE_API_MODE:-mixed}"
export VITE_API_BASE_URL="${VITE_API_BASE_URL:-}"
export VITE_AUTH_API_MODE="${VITE_AUTH_API_MODE:-http}"
export VITE_DASHBOARD_API_MODE="${VITE_DASHBOARD_API_MODE:-mock}"
export VITE_SPEAKING_API_MODE="${VITE_SPEAKING_API_MODE:-mock}"
export VITE_VOCABULARY_API_MODE="${VITE_VOCABULARY_API_MODE:-mock}"
export VITE_GRAMMAR_API_MODE="${VITE_GRAMMAR_API_MODE:-mock}"
export VITE_PROFILE_API_MODE="${VITE_PROFILE_API_MODE:-mock}"

printf 'Building commit %s from %s\n' "$(git -C "$repo_root" rev-parse --short HEAD)" "$repo_root"
printf 'Frontend API mode: %s (auth: %s, vocabulary: %s)\n' \
  "$VITE_API_MODE" "$VITE_AUTH_API_MODE" "$VITE_VOCABULARY_API_MODE"

if "$with_tests"; then
  printf '==> Building backend and running its tests\n'
  (
    cd "$backend_dir"
    mvn -B clean package
  )
else
  printf '==> Building backend (tests skipped)\n'
  (
    cd "$backend_dir"
    mvn -B clean package -Dmaven.test.skip=true
  )
fi

printf '==> Installing locked frontend dependencies\n'
(
  cd "$frontend_dir"
  npm ci
)

if "$with_tests"; then
  printf '==> Running frontend unit tests\n'
  (
    cd "$frontend_dir"
    npm run test:run
  )
fi

printf '==> Building frontend\n'
(
  cd "$frontend_dir"
  npm run build
)

printf '\nBuild completed. Artifacts:\n'
printf '  frontend: %s\n' "$frontend_dir/dist"
printf '  backend:  %s\n' "$backend_dir/target/backend-0.1.0-SNAPSHOT.jar"
