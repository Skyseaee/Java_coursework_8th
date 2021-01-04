package UnitTest;

import FileClass.GitCommandFunction;
import FileClass.GitUtils;

import java.io.File;

/**
 * 阶段性测试类，主要用来测试
 */
public class StepTest {
    public static void main(String[] args) throws Exception {
        String filePath = "E:\\Learning Materials\\Peking\\FirstSemester\\JavaProgramming\\TestFolder\\Test";
        final File folder = new File(filePath);
        // 初始化仓库
        GitCommandFunction gitTest = new GitCommandFunction("test",filePath);

        // 第一次commit
        GitUtils.generateFolderValue(new File(folder.getAbsolutePath()+"\\123.txt"), "1234512345");
        gitTest.commitCommand("test commit command");

        // 创建分支
        gitTest.createBranchCommand("test01");

        // 第二次commit
        GitUtils.generateFolderValue(new File(folder.getAbsolutePath()+"\\testBranch.txt"), "testBranch");
        gitTest.commitCommand("test branch");

        // 切换分支
        gitTest.checkoutCommand("test01");

        // 查看分支
        gitTest.branchCommand();

    }
}
