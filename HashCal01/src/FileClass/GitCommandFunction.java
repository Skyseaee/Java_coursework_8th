package FileClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * 整个Git的命令对应的函数放在此类中
 */
public class GitCommandFunction {
    private String userName;
    private String treeHash; // 最新的文件夹对应的哈希值
    private final String filePath;
    private final String newPath;
    private String lastHash;
    private Branch currentBranch; // 当前分支对应的类
    private File headFile; // 存放head指针的文件对应的类
    private GitConfig config; // 配置文件对应的类
    private Commit lastCommit; // 最后一次commit对应的类

    /**
     * 初始化整个Git
     * @param userName 用户配置的用户名
     * @param filePath 原文件夹路径
     */
    public GitCommandFunction(String userName, String filePath) throws Exception {
        this.userName = userName;
        this.filePath = filePath;
        this.newPath = filePath + "-git";
        File newFolder = new File(newPath);
        if(!newFolder.exists())
            newFolder.mkdir();
        // 创建配置文件并保存
        File configFile = new File(newPath+"\\config.txt");
        config = new GitConfig(userName,filePath,newPath);
        // DFS创建文件夹
        File objectFolder = new File(newPath+"\\object");
        if(!objectFolder.exists()) {
            objectFolder.mkdir();
        }
        DFSFolder.createFileFold(filePath,newPath+"\\object");
        // 创建commit，main分支并保存第一个head文件和branch文件
        File folder = new File(filePath);
        treeHash = new FiletoTree(folder,newPath+"\\object").getHashCode();
        // 创建head文件
        headFile = new File(newPath+"\\head.txt");
        lastCommit = new Commit(headFile, treeHash, userName, userName, "Initialize warehouse", newPath, "main");
        // 初始化main分支
        currentBranch = new Branch(newPath, treeHash);
        lastHash = currentBranch.getCommitHash();
    }

    /**
     * 提交一次新的commit
     * @param message 提交commit的message
     * @return 提交成功返回true，否则返回false
     * @throws Exception
     */
    public boolean commitCommand(String message) throws Exception {
        File folder = new File(filePath);
        DFSFolder.createFileFold(filePath,newPath+"\\object");
        treeHash = new FiletoTree(folder,newPath+"\\object").getHashCode();
        Commit tempCommit = new Commit(headFile, treeHash, userName, userName, message, newPath, currentBranch.getBranchName());
        // 更新branch的内容
        if(tempCommit.getHashCode()!=null) {
            lastCommit = tempCommit;
            currentBranch.updateBranch(lastCommit.getHashCode());
            return true;
        }
        return false;
    }

    /**
     * 创建分支
     * 如果想要创建的分支已经存在则输出提示信息
     * @param branchName 要创建的分支的分支名
     * @throws IOException
     */
    public void createBranchCommand(String branchName) throws IOException {
        if(!currentBranch.isSameBranch(newPath+"\\Branch",branchName)) {
            currentBranch = new Branch(newPath,currentBranch.getCommitHash(),branchName);
        }
        else {
            System.out.println("the branch has already existed, you can use \"git branch\" to look over your current branch.");
        }
    }

    /**
     * 切换分支函数
     * 需要判断该分支是否已存在，同时切换分支需要进行该分支下文件状态的复现（类似于回滚操作）
     * @param branchName 要切换到的分支名
     * @throws Exception
     */
    public void checkoutCommand(String branchName) throws Exception {
        // 主要是修改head中的内容 以及 currentBranch
        File file = new File(newPath+"\\Branch\\"+branchName+".txt");
        if(file.exists()) {
            String commitHash = GitUtils.readFirstLine(file);
            GitUtils.writeLine(headFile,commitHash+ " " +branchName);
            currentBranch = new Branch(file);
            File tempFile = new File(newPath+"\\commit\\"+commitHash+".txt");
            treeHash = GitUtils.readFirstLine(tempFile,false);
            CommitUtils.rollBackFolder(filePath,treeHash,newPath+"\\object\\");
        }
    }

    /**
     * 根据输入的key找对应的commit的key
     * @param tempKey commit对应的hash值的前六位
     * @return 找到的完整的hash值
     */
    private String findKey(String tempKey) {
        File file = new File(newPath+"\\commit");
        File[] files = file.listFiles();
        for(File fi:files) {
            if(tempKey.equals(fi.getName().substring(0,6))) {
                return fi.getName().split(".")[0];
            }
        }
        return "no";
    }

    /**
     * 回滚功能的实现（未验证）
     * @param key commit的key
     * @return 是否回滚成功
     * @throws IOException
     */
    public boolean rollBackCommand(String key) throws IOException {
        key = findKey(key);
        if(!key.equals("no")) {
            // 找到对应的commit 重复上述操作 再修改head
            CommitUtils.rollBack(key,newPath,currentBranch);
            // 通过commit的key读取tree的hash
            File file = new File(newPath+"\\commit\\"+key+".txt");
            treeHash = GitUtils.readFirstLine(file);
            currentBranch.updateBranch(key);
            CommitUtils.rollBackFolder(filePath,treeHash,newPath+"\\object\\");
            return true;
        }
        return false;
    }

    /**
     * 查看分支
     */
    public void branchCommand() {
        LinkedList<String> linkedList = new LinkedList<>();
        File folder = new File(newPath+"\\Branch");
        File[] files = folder.listFiles();
        for(File fi:files) {
            linkedList.add(fi.getName().split("\\.")[0]);
        }
        for(String s1:linkedList) {
            if(s1.equals(currentBranch.getBranchName())) {
                System.out.print("* ");
            }
            System.out.println(s1);
        }
    }

    /**
     * 查看commit情况(格式化输出)
     */
    public void logCommand() {

    }

    /**
     * 修改配置文件
     * @param newUserName 新的用户名
     */
    public void modifyConfigCommand(String newUserName) throws IOException {
        if(!newUserName.equals(userName)) {
            this.userName = newUserName;
            config.setUserName(userName);
        }
        else
            System.out.println("The new userName you input is as same as the old one.");
    }

    /**
     * 根据原有的文件夹内容初始化对象，一般用于文件夹的第二次初始化
     */
    public GitCommandFunction(String filePath) throws IOException {
        this.filePath = filePath;
        this.newPath = filePath + "-git";
        this.userName = GitUtils.readFirstLine(new File(newPath+"\\config.txt"));
        this.config = new GitConfig(userName,filePath,newPath);
        headFile = new File(newPath+"\\head.txt");
        String branchName = GitUtils.readFirstLine(headFile,false);
        this.currentBranch = new Branch(new File(newPath+"\\Branch\\"+branchName+".txt"));
        // 读取head文件中的哈希
        lastHash = GitUtils.readFirstLine(headFile);
        this.lastCommit = new Commit(new File(newPath+"\\commit\\"+lastHash+".txt"));
        treeHash = lastCommit.getTreeKey();
    }

    public Branch getCurrentBranch() {return currentBranch;}

    /**
     * 删除分支
     * @param branchName 要删除的分支
     * @return 是否成功删除分支
     */
    public boolean deleteBranchCommand(String branchName) {
        System.out.println(currentBranch.getBranchName());
        if(!branchName.equals(currentBranch.getBranchName())) {
            File file = new File(filePath+"-git\\Branch\\"+branchName+".txt");
            if(file.exists()) {
                return file.delete();
            }
            return false;
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }
}
