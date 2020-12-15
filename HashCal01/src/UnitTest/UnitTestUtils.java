package UnitTest;

import FileClass.FiletoBlob;
import FileClass.FiletoTree;
import FileClass.GitUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UnitTestUtils implements GitUtils{
    public static File DFSFolder(File file, String hashcode) throws IOException, NoSuchAlgorithmException {
        hashcode = hashcode.split("\\.")[0];
        if(hashcode.equals(FolderHash(file.getAbsolutePath()))) {
            return file;
        }
        File res = null;
        File[] fs = file.listFiles();
        for(File fi:fs) {
            if(fi.isDirectory()) {
                if(hashcode.equals(FolderHash(fi.getAbsolutePath()))) {
                    return fi;
                }
                else {
                    File temp = DFSFolder(fi, hashcode);
                    if(temp!=null) {
                        res = temp;
                    }
                }
            }
        }
        return res;
    }

    public static String FolderHash(String filePath) throws IOException, NoSuchAlgorithmException {
        StringBuilder tempcontent = new StringBuilder();
        String hashcode = "";
        File dir = new File(filePath);
        File[] fs = dir.listFiles();

        for(File item: fs) {
            if(item.isFile()) {
                FiletoBlob fb = new FiletoBlob(item);
                tempcontent.append(fb.getContent());
            }
            if(item.isDirectory()) {
                FiletoTree ft = new FiletoTree(item, FolderHash(filePath + File.separator + item.getName()));
                tempcontent.append(ft.getFolderContent());
            }
        }
        hashcode = GitUtils.HashCompute(new ByteArrayInputStream(tempcontent.toString().getBytes()));
        return hashcode;
    }

    public static File FindFileinthenewFolder(File[] filelist,String hashcode) {
        String filename = hashcode + ".txt";
        for(File fi:filelist) {
            if(fi.getName().equals(filename))
                return fi;
        }
        return null;
    }
}
