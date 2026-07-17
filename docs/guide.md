各位开发时不要直接改 `main` 分支。

我们的协作规则如下：

1. `main` 分支只放稳定、能运行的代码。

2. 每个人开发新功能前，先从最新的 `main` 拉取，然后新建要做的功能的分支。

   例如：

   ```bash
   git checkout main
   git pull
   git checkout -b feature/你的功能名
   ```

3. 每个人只在自己的分支上开发，不要直接往 `main` 提交代码。

4. 一个小功能完成后就可以准备合并，不用等整个大模块全部写完。

5. 合并前要确认：

   ```text
   项目能正常运行
   自己的功能基本完成
   没有明显报错
   不影响别人已有功能
   已经同步过最新 main
   ```

   前端功能至少执行一次：

   ```bash
   cd frontend
   npm run build
   ```

6. 合并前先把最新的 `main` 同步到自己的功能分支：

   ```bash
   git checkout main
   git pull
   git checkout feature/你的功能名
   git merge main
   ```

   如果有冲突，先解决冲突，再提交。

7. 小团队可以不强制 Pull Request / Merge Request。代码确认没问题后，先在群里简单说一声要合并什么，再直接合并到 `main`：

   ```bash
   git checkout main
   git merge feature/你的功能名
   git push origin main
   ```

8. 合并原则：

   ```text
   小功能完成就合并
   main 必须始终能运行
   没跑通的代码不要合并
   会影响别人模块的改动先沟通
   公共文件修改前先说一声
   ```

一句话总结：

**每个人在自己的分支开发；小功能做完、能运行、不影响别人，就合并到 main；main 永远保持稳定。**
