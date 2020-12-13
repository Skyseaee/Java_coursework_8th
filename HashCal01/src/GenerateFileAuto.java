import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class GenerateFileAuto {

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
        String tempname = path;
        StringBuilder tempcontent = new StringBuilder();

        File dir = new File(path);
        File[] fs = dir.listFiles();

        for(File item: fs) {
            if(item.isFile()) {
                // 如果是文件，则对文件内容进行哈希并将哈希值作为新文件的文件名，原文件内容依然不变的方式创建新文件
                InputStream inputStream = new FileInputStream(item);

                String hashcode = GenerateKeyValueAuto.Hashcompute(inputStream);
                tempcontent.append("Blob ");
                tempcontent.append(hashcode);
                tempcontent.append(" ");
                tempcontent.append(item.getName());
                tempcontent.append(" ");
                tempcontent.append('\n');
                tempname = newPathname + "\\" + hashcode + ".txt";
                GenerateKeyValueAuto.generateFileValue(item.getAbsolutePath(), tempname);
            }
            if(item.isDirectory()) {
                // 如果是文件夹，则首先深度遍历，返回时arrayList已经存储了其子文件以及子文件夹的所有内容，可以进行文件夹的哈希计算
                tempcontent.append("Tree ");
                tempcontent.append(" ");
                tempcontent.append(dfs(path + File.separator + item.getName(), newPathname));
                tempcontent.append(" ");
                tempcontent.append(item.getName());
                tempcontent.append('\n');
            }
        }
        String hashcode = GenerateKeyValueAuto.Hashcompute(new ByteArrayInputStream(tempcontent.toString().getBytes()));
        tempname = newPathname + "\\" + hashcode + ".txt";
        File newFile = new File(tempname);
        GenerateKeyValueAuto.generateValue(newFile, tempcontent.toString());
        return hashcode;
    }

}
