package FileClass;


import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public interface GitUtils {
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

    public static void GenerateValue(File file, String filecontent) throws IOException {


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
    }

    /**
     * 判断查找到的文件内容是否与原文件相同，即将原文件内容取出重新计算哈希值与新文件的文件名比较看是否相同
     * @param newfileName1 索引文件的文件名
     * @param oldfileName2 旧文件的绝对路径
     * @return
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    public static File FindFile(String key, String pathname) throws IOException {
        File file = new File(pathname);
        File[] fs = file.listFiles();
        for(File fi:fs){
            if(fi.getName().equals(key)) {
                return fi;
            }
        }
        System.out.println("File not found!");
        return null;
    }

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
    public static String readFirstLine(File file) throws Exception{
        if(!file.exists()){
            return null;
        }
        else{
            Scanner input = new Scanner(file);
            String ans = input.nextLine();
            input.close();
            return ans;
        }
    }
}
