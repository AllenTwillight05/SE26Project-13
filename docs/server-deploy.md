# 服务器发布

本项目的标准发布方式是：**在本机确定要发布的 Git 提交，通过 SSH 将该提交交给服务器构建和发布。**

服务器不需要从 GitHub 获取源码。这样，发布内容始终是本机已确认的提交，服务器只负责构建、运行和提供服务。

## 日常发布

在任意已配置服务器 SSH 发布权限的本机 Git 仓库中执行：

```bash
git status --short --branch
./scripts/deploy-server.sh
```

默认发布最新的 `origin/main` 提交。`git status` 仅用于让你确认本机正在修改什么；脚本不会提交、覆盖或删除本机的未提交内容。

发布成功后，在浏览器访问 `http://10.119.4.34/`，按 Ctrl+F5 刷新，并完成关键登录和首页冒烟验证。

## 发布过程

`scripts/deploy-server.sh` 会按以下顺序执行：

```text
本机获取 origin/main
→ 确定一个已提交的 Git commit
→ 创建仅包含该 commit 的 Git bundle
→ 通过 SSH 上传 bundle 到服务器
→ 服务器检出该 commit 并构建前端与后端
→ 发布前端静态文件和后端 JAR
→ 重启后端服务
→ 检查前端和认证接口
```

具体产物和运行位置如下：

| 内容 | 位置 | 用途 |
| --- | --- | --- |
| 服务器源码 | `/srv/english-learning-copilot` | 由 `deploy` 用户维护并在此构建 |
| 前端构建产物 | `frontend/dist` | Vite 生成的静态文件 |
| 对外前端目录 | `/var/www/english-learning-copilot` | Nginx 提供给浏览器的文件 |
| 后端构建产物 | `backend/target/backend-0.1.0-SNAPSHOT.jar` | Maven 生成的 Spring Boot JAR |
| 对外运行 JAR | `/opt/english-learning-copilot/backend.jar` | systemd 启动的后端程序 |
| 后端服务 | `english-learning-copilot` | 由 systemd 管理 |

构建阶段会执行：

```text
后端：mvn clean package -Dmaven.test.skip=true
前端：npm ci && npm run build
```

构建完成后，脚本将前端文件同步至 `/var/www/english-learning-copilot`，将 JAR 安装到 `/opt/english-learning-copilot/backend.jar`，然后重启 `english-learning-copilot` 服务。

最后，脚本会检查：

- `http://127.0.0.1/` 返回 `200`：Nginx 能提供前端首页。
- `http://127.0.0.1/api/auth/me` 在未登录时返回 `401`：Nginx 到后端的 API 链路可用，且认证行为正常。

## 本机未提交改动

日常发布不会影响本机工作区：

- `git fetch` 只更新 `origin/main` 的远程跟踪记录。
- bundle 只包含目标的**已提交**版本。
- 未提交、未暂存和未跟踪文件都不会被上传、提交、覆盖或删除。

因此，默认发布的是 `origin/main`，不是本机正在编辑但尚未提交的代码。

## 发布指定提交

需要验证某个已提交的分支或标签时，明确指定 Git ref：

```bash
./scripts/deploy-server.sh --ref fix/con-on-sql
```

`--ref` 必须能解析为已提交的 commit。该提交会被服务器以 detached HEAD 方式检出并发布；恢复日常版本时，再执行不带 `--ref` 的普通发布命令。

## 运行测试后发布

默认发布只构建，不运行测试。需要在构建阶段运行后端和前端测试时：

```bash
./scripts/deploy-server.sh --with-tests
```

只有构建与测试都通过时，脚本才会继续发布产物和重启服务。

## 前提条件

本机需要：

- Git、SSH、SCP 和 `mktemp`。
- 能获取 `origin/main`。
- 能以具有发布权限的 SSH 用户登录服务器。

服务器需要：

- `/srv/english-learning-copilot` 是干净的 Git 工作区；脚本发现未提交改动会停止发布。
- `deploy` 用户可在源码目录构建。
- Java、Maven、Node.js 和 npm 已安装；Node.js 必须满足 Vite 的版本要求。
- Nginx、`english-learning-copilot` systemd 服务、前端目录和 JAR 目录已配置完成。

构建可能需要下载 Maven 或 npm 依赖，因此服务器也需要能够访问相应的依赖仓库，或已有可用的本地缓存。

## 可配置目标

默认发布目标为 `root@10.119.4.34`。可按需用环境变量覆盖：

```bash
DEPLOY_USER=deploy DEPLOY_HOST=10.119.4.34 ./scripts/deploy-server.sh
```

可用变量：`DEPLOY_HOST`、`DEPLOY_USER`、`DEPLOY_PORT`、`DEPLOY_REPO`、`DEPLOY_BUILD_USER`、`DEPLOY_WEB_ROOT`、`DEPLOY_JAR`、`DEPLOY_SERVICE`。

不要把服务器密码、数据库密码、JWT 密钥或第三方 API 密钥写入仓库或命令历史。

## 发布失败时

发布脚本会在失败处停止，不会执行后续步骤。先保留终端错误信息，再按问题所在检查：

```bash
# 在服务器上查看后端服务状态和日志
systemctl --no-pager --full status english-learning-copilot
journalctl -u english-learning-copilot -n 100 --no-pager

# 检查 Nginx 配置
nginx -t
```

不要在服务器上使用 `npm run dev` 或 `mvn spring-boot:run` 代替发布流程；它们用于开发环境，而不是由 Nginx 和 systemd 管理的正式运行环境。
