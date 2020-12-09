import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class GenerateKeyValueAuto {

    /**
     * 创建新文件复制原文件
     * @param filepath 现在的文件路径
     * @param newFilePath 需要创建的文件的路径
     * @throws IOException
     */
    public static void generateFileValue(String filepath, String newFilePath) throws IOException {
        // 创建文件
        File srcfile = new File(filepath);

        File newFile = new File(newFilePath);
        // 创建流节点流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcfile));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = bufferedInputStream.read(buffer))!=-1)
        {
            bufferedOutputStream.write(buffer,0,len);
        }
        if(bufferedOutputStream!=null)
        {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(bufferedInputStream!=null)
        {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateValue(File file, String filecontent) throws IOException {
        // 创建文件
        File srcfile = file;

        // 创建流节点流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(filecontent.getBytes()));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = bufferedInputStream.read(buffer))!=-1)
        {
            bufferedOutputStream.write(buffer,0,len);
        }
        if(bufferedOutputStream!=null)
        {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(bufferedInputStream!=null)
        {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为之前的文件夹创建新的文件，名字为文件夹内容的哈希值，内容为子文件的内容以及子文件的名称
     * @param inputStream 需要进行保存的内容
     * @param newFilePath 保存的新文件的位置
     * @throws IOException
     */
    public static void generateFileFolderValue(InputStream inputStream, String newFilePath) throws IOException {
        String newPath = newFilePath;
        File destfile = new File(newPath);
        // 创建流节点流
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destfile));
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer))!=-1)
        {
            bufferedOutputStream.write(buffer,0,len);
        }
        if(bufferedOutputStream!=null)
        {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 计算文件的哈希值作为文件名

    /**
     * 计算哈希值
     * @param inputStream 需要进行哈希的内容
     * @return 16进制的哈希值
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String Hashcompute(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
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
     * 查找
     * @param key 对应的哈希值
     * @param pathname 文件夹路径
     * @return File
     * @throws IOException
     */
    public File FindFile(String key, String pathname) throws IOException {
        // 读入文件生成哈希表
        File file = new File(pathname);
        File[] fs = file.listFiles();
        for(File fi:fs){
            if(fi.getName()==key) {
                return fi;
            }
        }
        System.out.println("File not found!");
        return null;
    }

    /**
     * 复制文件的内容
     * @param file 被复制的文件
     * @return 将需要被处理的文件转化为String格式
     * @throws IOException
     */
    public static String copyFileContent(File file) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s = bf.readLine())!=null) {
            buffer.append(s);
        }
        String content = buffer.toString();
        bf.close();
        return content;
    }
}
