package FileClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Git一些配置文件
 */
public class GitConfig {
    private String userName;
    private String filePath;
    private String configPath;

    /**
     * 记录配置文件
     * @param userName 用户名
     * @param filePath 原文件夹地址
     * @param newPath 新的文件夹地址
     * @throws IOException
     */
    public GitConfig(String userName, String filePath, String newPath) throws IOException {
        this.filePath = filePath;
        this.userName = userName;
        configPath = newPath;
        File configFile = new File(configPath+"\\config.txt");
        if(!configFile.exists())
            GitUtils.generateFolderValue(configFile, toString());
    }

    public GitConfig(String configPath) throws FileNotFoundException {
        this.configPath = configPath;
        File configFile = new File(configPath);
        userName = GitUtils.readFirstLine(configFile);
        filePath = new File(configFile.getParent()).getParent();
    }

    public String toString() {
        return userName + " " + filePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) throws IOException {
        this.userName = userName;
        // 修改配置文件
        GitUtils.writeLine(new File(configPath+"\\config.txt"),userName + " " + filePath);
    }
}
