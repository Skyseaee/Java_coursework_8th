package FileClass;

import java.io.*;

/**
 * 创建branch文件夹，其中为每个branch创建一个文件保存该分支的信息，文件名变为分支名
 * 初始需要创建一个名为main的文件，代表有main分支(整体初始化的时候创建)
 * 保存分支的文件中需要记录的内容有：本分支下最新的commit的哈希值
 */
public class Branch{
    private String commitHash;
    private String branchName;
    private String branchPath;

    /**
     * 第一次初始化文件夹时，产生Branch为main
     */
    public Branch(String filePath, String commitHash) throws IOException {
        this.branchName = "main";
        File file = new File(filePath+"\\Branch");
        if(!file.exists())
            file.mkdir();
        branchPath = filePath + "\\Branch\\"+branchName+".txt";
        this.commitHash = commitHash;
        // 创建文件
        File tempFile = new File(branchPath);
        GitUtils.generateFolderValue(tempFile,commitHash);
    }

    public Branch(String filePath, String commitHash, String branchName) throws IOException {
        this.branchName = branchName;
        branchPath = filePath + "\\Branch\\"+ branchName +".txt";
        File branchFile = new File(branchPath);
        this.commitHash = commitHash;
        GitUtils.generateFolderValue(branchFile, commitHash);
    }

    /**
     * 通过传入的branch文件对应的路径创建Branch对象
     * @param branchFile 切换的分支对应的文件
     * @throws FileNotFoundException 未找到该分支对应的文件
     */
    public Branch(File branchFile) throws FileNotFoundException {
        branchName = branchFile.getName().split("\\.")[0];
        commitHash = GitUtils.readFirstLine(branchFile);
        branchPath = branchFile.getAbsolutePath();
    }

    /**
     * 更新branch文件中的commit
     * @param newCommitHash 新的commit的哈希值
     * @throws IOException
     */
    public void updateBranch(String newCommitHash) throws IOException {
        commitHash = newCommitHash;
        File branchFile = new File(branchPath);
        FileWriter fileWriter = new FileWriter(branchFile, false);
        fileWriter.write(commitHash);
        fileWriter.close();
    }

    /**
     * 判断新建的分支是否已存在，已存在则不允许创建
     * @param branchFolder 保存分支文件的文件夹
     * @return 存在重复返回true, 否则为false
     */
    public boolean isSameBranch(String branchFolder, String newBranchName) {
        File file = new File(branchFolder);
        File[] files = file.listFiles();
        for(File fi:files) {
            if(fi.getName().equals(newBranchName)) {return true;}
        }
        return false;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public String getCommitHash() { return commitHash;}
}
