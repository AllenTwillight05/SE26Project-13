# 服务器发布

日常发布只需要在任意具有服务器 SSH 发布权限的本机 Git 仓库中执行：

```bash
./scripts/deploy-server.sh
```

该命令会拉取最新 `origin/main`、将代码作为 Git bundle 上传到服务器、在服务器构建、发布前端和后端产物、重启服务，并检查首页返回 `200` 与未登录认证接口返回 `401`。

服务器不能直接访问 GitHub，因此不要登录服务器后执行 `git pull`。发布脚本已处理这件事。

## 发布前提

- 改动已经合入 `main`。
- 发布者可通过 SSH 登录服务器。推荐使用个人 SSH 公钥；不要共享或记录服务器密码。
- SSH 用户需要能写入服务器源码目录，并通过受限的 `sudo` 权限发布文件和重启 `english-learning-copilot` 服务。当前服务器也可由管理员使用 `root` 登录。

部署目标可按环境变量调整：

```bash
DEPLOY_USER=deploy DEPLOY_HOST=10.119.4.34 ./scripts/deploy-server.sh
```

## 测试策略

服务器上的旧测试套件目前不是可靠发布门禁，因此默认只构建，不执行测试。测试恢复稳定后，可在发布时显式运行：

```bash
./scripts/deploy-server.sh --with-tests
```

## 合并前的临时验证

需要验证某个分支时，明确指定已经提交的 Git ref：

```bash
./scripts/deploy-server.sh --ref fix/con-on-sql
```

这会将服务器源码切换到该提交的 detached HEAD，仅用于临时验证；验证完成并合入 `main` 后，再执行普通发布命令恢复到 `main` 最新版本。

## 发布后使用

浏览器访问 `http://10.119.4.34/`，按 Ctrl+F5 强制刷新，然后完成关键登录和首页冒烟验证。
