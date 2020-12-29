package FileClass;

import java.io.File;
import java.io.FileInputStream;

public class FiletoBlob{
    private File file;
    private String hashCode;
    private String fileName;

    public FiletoBlob(File sourceFile, String pathname) {
        file = sourceFile;
        fileName = sourceFile.getName();
        setHashCode();
        GenerateFile(pathname);
    }

    public FiletoBlob(String filePath) {
        file = new File(filePath);
        setHashCode();
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

    public void GenerateFile(String pathname) {
        pathname = pathname + "\\" + getHashCode() + ".txt";
        GitUtils.generateFileValue(file.getAbsolutePath(), pathname);
    }
}
