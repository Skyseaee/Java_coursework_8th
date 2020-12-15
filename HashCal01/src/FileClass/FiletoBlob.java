package FileClass;

import java.io.File;
import java.io.FileInputStream;

public class FiletoBlob implements GitUtils{
    private File file;
    private String hashCode;
    private String fileName;
    private StringBuilder content;
    private final String fileType = "Blob ";

    public FiletoBlob(File sourceFile) {
        file = sourceFile;
        fileName = sourceFile.getName();
        setHashCode();
        setContent();
    }

    public FiletoBlob(String filePath) {
        file = new File(filePath);
        setHashCode();
        setContent();
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
            this.hashCode = GitUtils.HashCompute(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setContent() {
        this.content = new StringBuilder();
        this.content.append(fileType);
        this.content.append(hashCode);
        this.content.append(" ");
        this.content.append(fileName);
        this.content.append('\n');
    }

    public StringBuilder getContent() {
        return content;
    }

    public void GenerateFile(String pathname) {
        pathname = pathname + "\\" + getHashCode() + ".txt";
        GitUtils.generateFileValue(file.getAbsolutePath(), pathname);
    }
}
