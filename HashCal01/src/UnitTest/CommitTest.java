package UnitTest;
import FileClass.*;

import java.io.File;

public class CommitTest {

    public static void CommitTest() throws Exception {
        final File folder = new File("E:\\Learning Materials\\Peking\\FirstSemester\\JavaProgramming\\Test");
        final String filePath = folder.getAbsolutePath();
        final String newPath = filePath + "newFolder";
        final String newPath02 = filePath + "newFolder head.txt";
        String treeKey = "";
        treeKey = new FiletoTree(folder,newPath).getHashCode();
        Commit commit01 = new Commit(new File(newPath), treeKey, "小天才", "小天才二号", "快夸我使小天才", newPath02);
        GitUtils.GenerateValue(new File(folder.getAbsolutePath()+"12.txt"), "12345");
        treeKey = new FiletoTree(folder,newPath).getHashCode();
        Commit commit02 = new Commit(new File(newPath), treeKey, "小天才", "小天才二号", "快夸我使小天才", newPath02);
    }

    public static void main(String[] args) throws Exception {
        CommitTest();
    }
}
