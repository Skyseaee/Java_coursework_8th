import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import FileClass.*;

public class DFSTest implements GitUtils{
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the absolute path");
        String absolutePath = scanner.nextLine();
        createFileFold(absolutePath);
    }

    /**
     * 创建新的文件夹保存遍历源文件后生成的文件
     * @param pathname 被遍历的原文件的绝对路径
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void createFileFold(String pathname) throws IOException, NoSuchAlgorithmException {
        String newPathName = pathname + "hashFolder";
        File file = new File(newPathName);
        if(!file.exists()) {
            file.mkdir();
        }
        dfs(pathname, newPathName);
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
                FiletoBlob fb = new FiletoBlob(item);
                tempcontent.append(fb.getContent());
                fb.GenerateFile(newPathname);
            }
            if(item.isDirectory()) {
                FiletoTree ft = new FiletoTree(item, dfs(path + File.separator + item.getName(), newPathname));
                tempcontent.append(ft.getFolderContent());
            }
        }
        hashcode = GitUtils.HashCompute(new ByteArrayInputStream(tempcontent.toString().getBytes()));
        tempname = newPathname + "\\" + hashcode + ".txt";
        File newFile = new File(tempname);
        GitUtils.GenerateValue(newFile, tempcontent.toString());
        return hashcode;
    }

}
