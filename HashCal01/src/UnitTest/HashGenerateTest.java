package UnitTest;

import FileClass.DFSFolder;
import FileClass.GitUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class HashGenerateTest implements GitUtils {

    private final File folder = new File("E:\\Learning Materials\\Peking\\FirstSemester\\JavaProgramming\\Test");
    private final String filePath = folder.getAbsolutePath();
    private final String newPath = filePath + "newFolder";

    @Before
    public void init()  throws IOException, NoSuchAlgorithmException {
        DFSFolder dfs = new DFSFolder(filePath, newPath);
    }

    @Test
    public void DFSFolderTest() throws IOException, NoSuchAlgorithmException {
        boolean res = true;
        File totalFile = new File(newPath);
        File[] fileList = totalFile.listFiles(); // 新文件夹文件的名称列表
        BufferedReader br = null;
        for(File fi:fileList) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fi)));
            String line = null;
            String tempfilehash = null;
            File folderFile = null;
            br.mark(10000);
            if((line = br.readLine())!=null) {
                String[] lines = line.split(" ");
                if(lines[0].equals("Tree") || lines[0].equals("Blob")) {
                    System.out.println("A folder is here.");
                    folderFile = fi;
                }
            }

            br.reset();
            if(folderFile!=null) {
                File tempFolder = UnitTestUtils.DFSFolder(folder, fi.getName());
                File[] files = tempFolder.listFiles();
                int fileindex = 0;
                while ((line = br.readLine())!=null) {
                    String[] lines = line.split(" ");
                    tempfilehash = lines[1];
                    if(lines[0].equals("Tree")) {
                        String tempHash = UnitTestUtils.FolderHash(files[fileindex++].getAbsolutePath());
                        if(!tempfilehash.equals(tempHash)) {
                            res = false;
                        }
                    }
                    else {
                        String tempHash = GitUtils.HashCompute(new FileInputStream(files[fileindex++]));
                        if(!tempfilehash.equals(tempHash)) {
                            System.out.println(tempfilehash+"-->"+tempHash);
                            res = false;
                        }
                    }
                }
            }
        }
        Assert.assertTrue(res);
        br.close();
    }

    @After
    public void close() {
        System.out.println("The unit test is over!");
    }
}
