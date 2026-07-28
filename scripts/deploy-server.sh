#!/usr/bin/env bash

# Deploy a committed Git revision to the shared server.
#
# The server cannot fetch from GitHub itself. This script therefore creates a
# self-contained Git bundle locally, uploads it over SSH, builds on the server,
# publishes the artifacts, restarts the service, and performs health checks.

set -Eeuo pipefail

repo_root="$(git rev-parse --show-toplevel 2>/dev/null)" || {
  printf 'Run this script from inside a Git checkout.\n' >&2
  exit 1
}

server_host="${DEPLOY_HOST:-10.119.4.34}"
server_user="${DEPLOY_USER:-root}"
server_port="${DEPLOY_PORT:-22}"
server_repo="${DEPLOY_REPO:-/srv/english-learning-copilot}"
server_build_user="${DEPLOY_BUILD_USER:-deploy}"
server_web_root="${DEPLOY_WEB_ROOT:-/var/www/english-learning-copilot}"
server_jar="${DEPLOY_JAR:-/opt/english-learning-copilot/backend.jar}"
server_service="${DEPLOY_SERVICE:-english-learning-copilot}"
ref="origin/main"
with_tests=false
temporary_bundle=""

usage() {
  cat <<'EOF'
Usage: scripts/deploy-server.sh [options]

Deploy a committed revision to the shared server. The default revision is the
latest origin/main. Tests are skipped by default because the server's legacy
test suites are not yet release gates.

Options:
  --ref <git-ref>  Deploy this committed local Git ref instead of origin/main.
  --with-tests     Run backend and frontend tests during the server build.
  -h, --help       Show this help text.

Configuration is supplied through environment variables, not committed secrets:
  DEPLOY_HOST, DEPLOY_USER, DEPLOY_PORT, DEPLOY_REPO, DEPLOY_BUILD_USER,
  DEPLOY_WEB_ROOT, DEPLOY_JAR, DEPLOY_SERVICE.
EOF
}

require_command() {
  local command_name="$1"
  if ! command -v "$command_name" >/dev/null 2>&1; then
    printf 'Missing required command: %s\n' "$command_name" >&2
    exit 1
  fi
}

cleanup() {
  if [[ -n "$temporary_bundle" ]]; then
    rm -f -- "$temporary_bundle"
  fi
}
trap cleanup EXIT

while (($# > 0)); do
  case "$1" in
    --ref)
      if (($# < 2)); then
        printf '%s\n' '--ref requires a Git ref.' >&2
        exit 2
      fi
      ref="$2"
      shift
      ;;
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

require_command git
require_command ssh
require_command scp
require_command mktemp

if [[ "$ref" == "origin/main" ]]; then
  printf '==> Fetching origin/main\n'
  git -C "$repo_root" fetch --quiet origin main
fi

commit="$(git -C "$repo_root" rev-parse --verify "${ref}^{commit}")" || {
  printf 'Git ref does not resolve to a commit: %s\n' "$ref" >&2
  exit 1
}
short_commit="${commit:0:12}"
target="${server_user}@${server_host}"
temporary_bundle="$(mktemp "${TMPDIR:-/tmp}/english-learning-copilot-${short_commit}.XXXXXX.bundle")"
remote_bundle="/tmp/english-learning-copilot-${short_commit}.bundle"

printf '==> Creating source bundle for %s (%s)\n' "$short_commit" "$ref"
git -C "$repo_root" bundle create "$temporary_bundle" "$commit"
git -C "$repo_root" bundle verify "$temporary_bundle" >/dev/null

printf '==> Uploading bundle to %s\n' "$target"
scp -P "$server_port" "$temporary_bundle" "${target}:${remote_bundle}"

printf '==> Building and publishing %s on %s\n' "$short_commit" "$server_host"
ssh -p "$server_port" "$target" bash -s -- \
  "$remote_bundle" "$commit" "$server_repo" "$server_build_user" \
  "$server_web_root" "$server_jar" "$server_service" "$with_tests" <<'REMOTE_SCRIPT'
set -Eeuo pipefail

bundle_path="$1"
commit="$2"
repo_path="$3"
build_user="$4"
web_root="$5"
jar_path="$6"
service_name="$7"
with_tests="$8"

run_as_root() {
  if ((EUID == 0)); then
    "$@"
  else
    sudo "$@"
  fi
}

run_as_build_user() {
  if ((EUID == 0)); then
    runuser -u "$build_user" -- "$@"
  else
    sudo -u "$build_user" -- "$@"
  fi
}

cleanup() {
  run_as_root rm -f -- "$bundle_path"
}
trap cleanup EXIT

[[ -d "$repo_path/.git" ]] || {
  printf 'Server repository does not exist: %s\n' "$repo_path" >&2
  exit 1
}
[[ -f "$bundle_path" ]] || {
  printf 'Uploaded bundle does not exist: %s\n' "$bundle_path" >&2
  exit 1
}

if [[ -n "$(run_as_build_user git -C "$repo_path" status --porcelain)" ]]; then
  printf 'Server repository has uncommitted changes; refusing to deploy.\n' >&2
  exit 1
fi

run_as_build_user git -C "$repo_path" bundle unbundle "$bundle_path" >/dev/null
run_as_build_user git -C "$repo_path" cat-file -e "${commit}^{commit}"
run_as_build_user git -C "$repo_path" update-ref refs/deployments/current "$commit"
run_as_build_user git -C "$repo_path" checkout --detach "$commit"

build_args=()
if [[ "$with_tests" == "true" ]]; then
  build_args=(--with-tests)
fi
run_as_build_user "$repo_path/scripts/build-server.sh" "${build_args[@]}"

frontend_dist="$repo_path/frontend/dist/"
backend_jar="$repo_path/backend/target/backend-0.1.0-SNAPSHOT.jar"
[[ -f "${frontend_dist}index.html" ]] || {
  printf 'Frontend build did not produce index.html.\n' >&2
  exit 1
}
[[ -f "$backend_jar" ]] || {
  printf 'Backend build did not produce its JAR.\n' >&2
  exit 1
}

run_as_root rsync -a --delete "$frontend_dist" "$web_root/"
run_as_root install -o elc -g elc -m 640 "$backend_jar" "$jar_path"
run_as_root systemctl restart "$service_name"

for _ in $(seq 1 30); do
  auth_status="$(curl -s -o /dev/null -w '%{http_code}' http://127.0.0.1/api/auth/me || true)"
  if [[ "$auth_status" != "000" ]]; then
    break
  fi
  sleep 1
done

frontend_status="$(curl -s -o /dev/null -w '%{http_code}' http://127.0.0.1/)"
[[ "$frontend_status" == "200" ]] || {
  printf 'Frontend health check failed: HTTP %s\n' "$frontend_status" >&2
  exit 1
}
[[ "$auth_status" == "401" ]] || {
  printf 'Authentication health check failed: expected 401, got %s\n' "$auth_status" >&2
  exit 1
}

printf '\nDeployment complete.\n'
printf '  commit:   %s\n' "$(run_as_build_user git -C "$repo_path" rev-parse --short HEAD)"
printf '  frontend: HTTP %s\n' "$frontend_status"
printf '  auth API: HTTP %s\n' "$auth_status"
REMOTE_SCRIPT
