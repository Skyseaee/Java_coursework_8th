import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateKeyValueAuto {

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
            if(fi.getName().equals(key)) {
                return fi;
            }
        }
        System.out.println("File not found!");
        return null;
    }
}
