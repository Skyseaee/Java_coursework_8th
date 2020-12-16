package FileClass;

import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * 深度遍历文件夹生成对应新的git仓库
 */
public class DFSFolder implements GitUtils{
    private String path;
    private String newPath;
    /**
     * 构造函数,根据私有属性path来调用其他方法进行后续操作
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public DFSFolder(String path, String newPath) throws IOException, NoSuchAlgorithmException {
        this.path = path;
        this.newPath = newPath;
        createFileFold(path, newPath);
    }

    /**
     * 创建新的文件夹保存遍历源文件后生成的文件
     * @param pathname 被遍历的原文件的绝对路径
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void createFileFold(String pathname, String newPath) throws IOException, NoSuchAlgorithmException {
        File file = new File(newPath);
        if(!file.exists()) {
            file.mkdir();
        }
        dfs(pathname, newPath);
    }

    /**
     * 进行深度遍历，同时在遍历时创建新的文件保存源文件夹对应文件和子文件夹的内容
     * @param path 原文件夹的路径
     * @param newPathname 创建新的文件夹的地址
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String dfs(String path, String newPathname) throws IOException, NoSuchAlgorithmException {
        String tempname = "";
        StringBuilder tempcontent = new StringBuilder();
        String hashcode = "";
        File dir = new File(path);
        File[] fs = dir.listFiles();

        for(File item: fs) {
            if(item.isFile()) {
                // 如果是文件，则对文件内容进行哈希并将哈希值作为新文件的文件名，原文件内容依然不变的方式创建新文件
                new FiletoBlob(item, newPathname);
            }
            if(item.isDirectory()) {
                FiletoTree ft = new FiletoTree(item, newPathname);
                dfs(path + File.separator + item.getName(), newPathname);
                tempcontent.append(ft.getFolderContent());
            }
        }

        hashcode =  GitUtils.HashCompute(new ByteArrayInputStream(tempcontent.toString().getBytes()));
        return hashcode;
    }

}
