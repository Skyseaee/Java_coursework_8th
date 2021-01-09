package FileClass;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 将文件夹转变为tree的类
 */
public class FiletoTree{
    private File file;
    private String hashCode;
    private String folderName;
    private String folderContent;

    public FiletoTree(File file,String newPath) throws IOException, NoSuchAlgorithmException {
        this.file = file;
        folderName = file.getName();
        setFolderContent();
        setHashCode();
        newPath = newPath + "\\" + hashCode + ".txt";
        GitUtils.generateFolderValue(new File(newPath), folderContent);
    }

    public FiletoTree(String filePath) {
        this.file = new File(filePath);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode() {
        try {
            this.hashCode = GitUtils.HashCompute(new ByteArrayInputStream(folderContent.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFolderContent() {
        return folderContent;
    }

    public void setFolderContent() throws IOException, NoSuchAlgorithmException {
        this.folderContent = GitUtils.FolderHash(file.getAbsolutePath()).toString();
    }

}
