package FileClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Git命令
 */
public class GitCommandIndex {
    private Map<String, String> commandMap = new HashMap<String, String>();

    public GitCommandIndex() {
        commandMap.put("git", "git");
        commandMap.put("config", "config");
        commandMap.put("-m", "message");
        commandMap.put("branch", "branch");
        commandMap.put("checkout", "checkout");
        commandMap.put("log", "log");
        commandMap.put("reset", "reset");
        commandMap.put("-d", "delete");
        commandMap.put("--mixed", "--mixed");
        commandMap.put("--hard", "--hard");
    }

    public Map<String, String> getCommandMap() {
        return commandMap;
    }

    public void setCommandMap(String key, String value) {
        this.commandMap.put(key,value);
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
