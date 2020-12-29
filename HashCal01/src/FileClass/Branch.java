package FileClass;

import java.io.*;

/**
 * 创建branch文件夹，其中为每个branch创建一个文件保存该分支的信息，文件名变为分支名
 * 初始需要创建一个名为main的文件，代表有main分支(整体初始化的时候创建)
 * 保存分支的文件中需要记录的内容有：本分支下最新的commit的哈希值
 */
public class Branch {
    private String commitHash;
    private final String branchName;
    private String branchPath;

    public Branch(String branchName, String filePath, String commitHash) throws IOException {
        this.branchName = branchName;
        if(!isSameBranch(filePath+"\\Branch")) {
            branchPath = filePath + "\\Branch\\"+branchName;
            File branchFile = new File(branchPath);
            this.commitHash = commitHash;
            GitUtils.generateFolderValue(branchFile, commitHash);
        }
        else {
            updateBranch(commitHash);
        }
    }

    /**
     * 通过传入的branch文件对应的路径创建Branch对象
     * @param branchPath 传入文件的路径
     * @throws FileNotFoundException
     */
    public Branch(String branchPath) throws FileNotFoundException {
        File branchFile = new File(branchPath);
        branchName = branchFile.getName();
        commitHash = GitUtils.readFirstLine(branchFile);
        this.branchPath = branchPath;
    }
    /**
     * 更新branch文件中的commit，可用于切换分支
     * @param newCommitHash 新的commit的哈希值
     * @throws IOException
     */
    public void updateBranch(String newCommitHash) throws IOException {
        File srcFile = new File(branchPath);
        FileReader in = new FileReader(srcFile);
        BufferedReader bufIn = new BufferedReader(in);
        // 内存流，作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
        // 替换
        String line = bufIn.readLine();
        line = line.replaceAll(commitHash,newCommitHash);
        tempStream.write(line);
        bufIn.close();
        FileWriter out = new FileWriter(srcFile);
        tempStream.writeTo(out);
        out.close();
    }

    /**
     * 判断新建的分支是否已存在，已存在则不允许创建
     * @param branchFolder 保存分支文件的文件夹
     * @return 存在重复返回true, 否则为false
     */
    private boolean isSameBranch(String branchFolder) {
        File file = new File(branchFolder);
        File[] files = file.listFiles();
        for(File fi:files) {
            if(fi.getName().equals(branchName)) {return true;}
        }
        return false;
    }

    public String getBranchName() {
        return branchName;
    }
}
