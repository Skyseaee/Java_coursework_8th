package FileClass;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class FiletoTree implements GitUtils{
    private File file;
    private String hashCode;
    private String folderName;
    private final String fileType = "Tree ";
    private List<File> childFile;
    private StringBuilder folderContent;

    public FiletoTree(File file, String lastContent) {
        this.file = file;
        folderName = file.getName();
        setFolderContent(lastContent);
        setHashCode();
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
            this.hashCode = GitUtils.HashCompute(new ByteArrayInputStream(folderContent.toString().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFileType() {
        return fileType;
    }

    public List<File> getChildFile() {
        return childFile;
    }

    public void setChildFile(File childFile) {
        this.childFile.add(childFile);
    }

    public StringBuilder getFolderContent() {
        return folderContent;
    }

    public void setFolderContent(String lastContent) {
        this.folderContent = new StringBuilder();
        this.folderContent.append(fileType);
        this.folderContent.append(lastContent);
        this.folderContent.append(" ");
        this.folderContent.append(folderName);
        this.folderContent.append('\n');
    }

}
