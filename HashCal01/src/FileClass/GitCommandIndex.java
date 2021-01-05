package FileClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Git命令
 */
public class GitCommandIndex {
    private HashSet<String> commandSet = new HashSet<>();

    public GitCommandIndex() {
        commandSet.add("git");
        commandSet.add("config");
        commandSet.add("branch");
        commandSet.add("checkout");
        commandSet.add("log");
        commandSet.add("reset");
        commandSet.add("-d");
        commandSet.add("-m");
        commandSet.add("--mixed");
        commandSet.add("--hard");
    }

    public HashSet<String> getCommandSet() {
        return commandSet;
    }

    public void setCommandSet(String key) {
        this.commandSet.add(key);
    }

    // Git init -- 创建第一次commit，初始化main分支，
    String initCommand = "git init";
    // 登录
    // git config --global user.name "Your Name"
    String configCommand = "git config userName";
    // 手动commit commit -m "commit content"
    String commitCommand = "git commit -m message";
    // 创建分支 git branch branchName
    String createBranchCommand = "git branch branchName";
    // 切换分支 git checkout branchName / git switch branchName
    String checkoutBranchCommand = "git checkout branchName";
    // 查看历史版本 git log
    String logCommand = "git log";
    // 回滚 git reset -hard HEAD~3
    // 返回上一次提交 git reset --mixed
    String resetCommand = "git reset -hard";
    String resetOnceCommand = "git reset --mixed";
    // 删除分支 git branch -d branchName 不能删除当前分支
    String deleteBranchCommand = "git branch -d";
    // 查看已有分支 git branch
    String checkBranch = "git branch";
}
