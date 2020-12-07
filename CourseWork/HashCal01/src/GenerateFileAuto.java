import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
        ArrayList<String> arrayList = new ArrayList<>();
        dfs(pathname,arrayList,newPathName);
    }

    /**
     * 进行深度遍历，同时在遍历时创建新的文件保存源文件夹对应文件和子文件夹的内容
     * @param path 原文件夹的路径
     * @param arrayList 用来存储需要被哈希的内容的中间数据结构
     * @param newPathname 创建新的文件夹的地址
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void dfs(String path, ArrayList<String> arrayList, String newPathname) throws IOException, NoSuchAlgorithmException {
        File dir = new File(path);
        File[] fs = dir.listFiles();
        for(File item:fs) {
            if(item.isFile()) {
                // 如果是文件，则对文件内容进行哈希并将哈希值作为新文件的文件名，原文件内容依然不变的方式创建新文件
                InputStream inputStream = new FileInputStream(item);
                // 将文件的内容和文件的名称保存到arrayList当中方便文件夹哈希值的计算
                arrayList.add("----Blob----");
                arrayList.add(item.getName());
                arrayList.add(GenerateKeyValueAuto.copyFileContent(item));
                String hashcode = GenerateKeyValueAuto.Hashcompute(inputStream);
                // 生成新的文件的绝对路径，name为文件的hashcode，格式为txt
                String filename = newPathname + "\\" + hashcode + ".txt";
                GenerateKeyValueAuto.generateFileValue(item.getAbsolutePath(),filename);
            }
            if(item.isDirectory()) {
                // 如果是文件夹，则首先深度遍历，返回时arrayList已经存储了其子文件以及子文件夹的所有内容，可以进行文件夹的哈希计算
                dfs(path+File.separator+item.getName(),arrayList,newPathname);
                // 手动将arrayList的所有内容转为一个大的string
                String tempString = null;
                for(String arr:arrayList) {
                    tempString += arr;
                    tempString += '\n';
                }
                InputStream inputStream = new ByteArrayInputStream(tempString.getBytes(StandardCharsets.UTF_8));
                // 以下两个步骤主要是由于inputStream只能被使用一次，因此首先将inputStream复制一份备用
                ByteArrayOutputStream bos = cloneInputStream(inputStream);
                InputStream inputStream1 = new ByteArrayInputStream(bos.toByteArray());

                // 计算文件夹对应的哈希值
                String hashcode = GenerateKeyValueAuto.Hashcompute(inputStream);
                //新的文件路径，用来保存文件夹
                String foldername = newPathname + "\\" + hashcode + ".txt";
                GenerateKeyValueAuto.generateFileFolderValue(inputStream1 ,foldername);
                // 将文件夹名称保存到路径中,并进行标记
                arrayList.add("--Folder--");
                arrayList.add(item.getName());
            }
        }

    }

    /**
     * 克隆输入流
     * @param input 被克隆的输入流
     * @return 字节中间流
     */
    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
