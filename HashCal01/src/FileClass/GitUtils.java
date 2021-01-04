package FileClass;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * GitUtils中存放的都是一些会被常用到的方法，因此将这些方法直接写为静态方法方便它们
 * 在不同的类之间被调用，简化开发流程
 */
public class GitUtils {
    /**
     * 计算哈希值
     * @param inputStream 需要进行哈希的内容
     * @return 16进制的哈希值
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String HashCompute(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        //创建文件缓冲区
        byte[] buffer = new byte[1024];
        // 使用SHA1哈希/摘要算法
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead = 0;
        do {
            numRead = inputStream.read(buffer);
            if(numRead>0) {
                complete.update(buffer,0,numRead);
            }
        }while (numRead!=-1);
        inputStream.close();
        byte[] sha1Bytes = complete.digest();
        BigInteger bigInteger = new BigInteger(1,sha1Bytes);
        return bigInteger.toString(16);
    }

    /**
     * 创建新文件复制原文件到新的目录下
     * @param filepath 现在的文件路径
     * @param newFilePath 需要创建的文件的路径
     */
    public static void generateFileValue(String filepath, String newFilePath){
        // 创建文件
        File srcfile = new File(filepath);

        File newFile = new File(newFilePath);
        // 创建流节点流
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(srcfile));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = bufferedInputStream.read(buffer))!=-1)
            {
                bufferedOutputStream.write(buffer,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // 关闭流
        } finally {
            if(bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将文件夹以txt文件的形式进行保存
     * @param file 要写入的文件
     * @param filecontent 文件内容
     * @throws IOException
     */
    public static void generateFolderValue(File file, String filecontent) throws IOException {

        // 创建流节点流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(filecontent.getBytes()));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = bufferedInputStream.read(buffer))!=-1)
        {
            bufferedOutputStream.write(buffer,0,len);
        }

        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            bufferedOutputStream.close();
            bufferedInputStream.close();
        }
    }

    /**
     * 判断查找到的文件内容是否与原文件相同，即将原文件内容取出重新计算哈希值与新文件的文件名比较看是否相同
     * @param newfileName1 索引文件的文件名
     * @param oldfileName2 旧文件的绝对路径
     * @return bool
     */
    public static boolean isFileSame(String newfileName1, String oldfileName2) {
        InputStream is = null;
        String f1hash = "";
        boolean res = false;
        File fl = new File(oldfileName2);
        try {
            is = new FileInputStream(fl);
            f1hash = HashCompute(is);
            if(f1hash.equals(newfileName1)) {res = true;}
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 查找
     * @param key 对应的哈希值
     * @param pathname 文件夹路径
     * @return File or null if not find
     * @throws IOException
     */
    public static File findFile(String key, String pathname) throws IOException {
        File file = new File(pathname);
        File[] fs = file.listFiles();
        for(File fi:fs) {
            if((fi.getName().split("\\.")[0]).equals(key)) {
                return fi;
            }
        }
        System.out.println("File not found!");
        return null;
    }

    /**
     * 返回文件夹的哈希值
     * @param filePath 文件夹的路径
     * @return 返回文件夹对应的文件需要保存的内容
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static StringBuilder FolderHash(String filePath) throws IOException, NoSuchAlgorithmException {
        StringBuilder tempcontent = new StringBuilder();
        File dir = new File(filePath);
        File[] fs = dir.listFiles();

        for(File item: fs) {
            if(item.isFile()) {
                tempcontent.append("Blob");
                tempcontent.append(" ");
                tempcontent.append(HashCompute(new FileInputStream(item)));
                tempcontent.append(" ");
                tempcontent.append(item.getName());
                tempcontent.append('\n');
            }
            if(item.isDirectory()) {
                tempcontent.append("Tree");
                tempcontent.append(" ");
                tempcontent.append(HashCompute(new ByteArrayInputStream(FolderHash(item.getAbsolutePath()).toString().getBytes())));
                tempcontent.append(" ");
                tempcontent.append(item.getName());
                tempcontent.append('\n');
            }
        }
        return tempcontent;
    }

    /**
     * 读取文件第一行，空格前面的数据
     * @param file 被读取的文件
     * @return 读取的字符串
     * @throws FileNotFoundException
     */
    public static String readFirstLine(File file) throws FileNotFoundException {
        if(!file.exists()){
            System.out.println("the file is not exist, can't read the first line.");
            return null;
        }
        else{
            Scanner input = new Scanner(file);
            String ans = input.nextLine().split(" ")[0];
            input.close();
            return ans;
        }
    }

    /**
     * 读取文件第一行，空格后面的数据
     * @param file 被读取的文件
     * @param index 判断读取文件前面还是后面
     * @return 读取的字符串
     * @throws FileNotFoundException
     */
    public static String readFirstLine(File file,boolean index) throws FileNotFoundException {
        if(!file.exists()){
            System.out.println("the file is not exist, can't read the first line.");
            return null;
        }
        else{
            Scanner input = new Scanner(file);
            String ans = input.nextLine().split(" ")[1];
            input.close();
            return ans;
        }
    }

    /**
     * 重写文件内容
     * @param file 被重写的文件
     * @param content 要写入文件的内容
     * @throws IOException
     */
    public static void writeLine(File file,String content) throws IOException{

        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
//        File tmp = File.createTempFile("tmp", null);
//        RandomAccessFile raf = new RandomAccessFile(file, "rw");
//        FileOutputStream tmpOut = new FileOutputStream(tmp);
//        FileInputStream tmpIn = new FileInputStream(tmp);
//        raf.seek(0);
//        byte[] buf = new byte[64];
//        int hasRead = 0;
//        while((hasRead = raf.read(buf)) > 0){
//            tmpOut.write(buf,0,hasRead);
//        }
//        raf.seek(0);
//        raf.write(content.getBytes());
//        while((hasRead = tmpIn.read(buf)) > 0){
//            raf.write(buf,0,hasRead);
//        }
//        raf.close();
//        tmpIn.close();
//        tmpOut.close();
    }

}
