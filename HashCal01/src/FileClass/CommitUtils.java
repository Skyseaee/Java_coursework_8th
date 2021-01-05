package FileClass;

import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;

/**
 * CommitUtils类主要用来存放与Commit息息相关的类，但在每次调用时又不需要实例化Commit对象
 * 主要包括我们的回滚方法
 */
public class CommitUtils {

    /**
     * 按回滚次数回滚
     * @param count 回滚次数
     */
    public void rollBack(int count) {

    }

    /**
     * 按commit的key进行回滚
     * @param key 指定回滚到commit的key
     */
    public static void rollBack(String key, String newPath, Branch currentBranch) throws IOException {
        // while循环
        // 重置head指针
        File headFile = new File(newPath+"\\"+"head.txt");

        // 更新head
        FileOutputStream fileOutputStream = new FileOutputStream(headFile, false);
        String newContent = key + " " + currentBranch.getBranchName();
        fileOutputStream.write(newContent.getBytes());
        fileOutputStream.close();
    }

    /**
     * 修改head文件记录的内容
     * @param key 想要回滚到的key
     * @param newPath 新文件夹路径
     * @param currentBranchName 当前分支名
     * @throws IOException
     */
    public static void changeFile(String key, String newPath, String currentBranchName) throws IOException {
        // 更新head
        File headFile = new File(newPath+"\\commit\\"+"head.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(headFile, false);
        StringBuilder newContent = new StringBuilder(key);
        newContent.append(" ");
        newContent.append(currentBranchName);
        fileOutputStream.write(newContent.toString().getBytes());
        fileOutputStream.close();
    }

    /**
     * 把commit对应的根目录Tree对象恢复成一个文件夹
     * @param pathName 原文件夹位置
     * @param hashCode 要创建的文件夹的哈希值，从head文件中读取
     * @param newPath 存放计算哈希值之后的新文件夹位置
     */
    public static void genFolder(String pathName, String hashCode, String newPath) throws IOException {
        // 读取当前commit哈希值对应的文件，
        File file = new File(newPath+hashCode+".txt");
        BufferedReader buf = new BufferedReader(new FileReader(file));
        String line = buf.readLine();
        // while读取文件每一行，并用split观察文件为tree或blob
        while (line!=null) {
            String[] list = line.split(" ");
            // blob获得文件名，根据commit的值找到保存文件并复制文件至原文件夹重命名
            if(list[0].equals("Blob")) {
                GitUtils.generateFileValue(newPath+list[1]+".txt",pathName + "\\" + list[2]);
            }
            // 如果为tree则继续遍历，传入的参数为（pathName+文件夹名，文件夹的hashCode,newPath）
            else if(list[0].equals("Tree")) {
                File folder = new File(pathName+ "\\" + list[2]);
                if(!folder.exists()) {
                    boolean index = folder.mkdir();
                }
                genFolder(pathName+"\\"+list[2]+"\\",list[1],newPath);
            }
            line = buf.readLine();
        }
        buf.close();
    }

    /**
     * 删除源文件所有文件
     * @param folder 原文件夹
     */
    public static void deleteFolder(File folder) {
        if(folder.isDirectory()) {
            File[] files = folder.listFiles();
            for(File fi:files) {
                deleteFolder(fi);
            }
            folder.delete();
        } else {
            folder.delete();
        }
    }

    /**
     * 删除文件夹+回滚
     * @param filePath 原文件夹路径
     * @param treeHash 想要恢复的版本的文件夹的哈希值
     * @param newPath 存放计算哈希值之后的新文件夹位置
     * @throws IOException
     */
    public static void rollBackFolder(String filePath, String treeHash, String newPath) throws IOException {
        // 删除文件，回滚
        CommitUtils.deleteFolder(new File(filePath));
        File folder = new File(filePath);
        folder.isDirectory();
        if(!folder.exists()) {
            folder.mkdir();
        }
        CommitUtils.genFolder(filePath,treeHash,newPath);
    }

//    private String findLastCommitHash(File file) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//        bufferedReader.readLine();
//        String res = bufferedReader.readLine().split(" ")[1];
//        bufferedReader.close();
//        return res;
//    }
}
