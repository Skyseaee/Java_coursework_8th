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
        if(hashcode.equals(GitUtils.HashCompute(new ByteArrayInputStream(GitUtils.FolderHash(file.getAbsolutePath()).toString().getBytes())))) {
            return file;
        }
        File res = null;
        File[] fs = file.listFiles();
        for(File fi:fs) {
            if(fi.isDirectory()) {
                if(hashcode.equals(GitUtils.HashCompute(new ByteArrayInputStream(GitUtils.FolderHash(fi.getAbsolutePath()).toString().getBytes())))) {
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

    public static File FindFileinthenewFolder(File[] filelist,String hashcode) {
        String filename = hashcode + ".txt";
        for(File fi:filelist) {
            if(fi.getName().equals(filename))
                return fi;
        }
        return null;
    }
}
