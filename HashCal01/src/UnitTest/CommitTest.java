package UnitTest;
import FileClass.*;

import java.io.File;

public class CommitTest {

    public static void commitTest() throws Exception {
        final File folder = new File("E:\\Learning Materials\\Peking\\FirstSemester\\JavaProgramming\\Test");
        final String filePath = folder.getAbsolutePath();
        final String newPath = filePath + "newFolder";
        final String newPath02 = newPath + "\\" +"head.txt";
        new DFSFolder(filePath, newPath);
        String treeKey = "";
        treeKey = new FiletoTree(folder,newPath).getHashCode();

        File headFile = new File(newPath02);
        Commit commit01 = new Commit(headFile, treeKey, "小天才", "小天才二号", "快夸我是小天才", newPath, "main");
        GitUtils.generateFolderValue(new File(folder.getAbsolutePath()+"\\123.txt"), "1234512345");
        treeKey = new FiletoTree(folder,newPath).getHashCode();
        Commit commit02 = new Commit(headFile, treeKey, "小天才", "小天才二号", "快夸我是小天才", newPath, "main");
    }

    public static void main(String[] args) throws Exception {
        commitTest();
    }
}
