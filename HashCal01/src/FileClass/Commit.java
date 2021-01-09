package FileClass;
import java.io.*;
import java.util.Date;
import java.security.NoSuchAlgorithmException;

/**
 * Commit的类，提交commit时初始化使用，包括创建commit文件等
 */
public class Commit{
    private String hashCode; //当前被commit的文件夹的哈希值
    private String author;
    private String committer;
    private String comment;
    private String timeStamp;
    private String lastKey;
    private String treeKey;
    private String branch;
    private String newPath;
    private final String type = "Commit";

    /**
     * 构造一个Commit
     * @param head 保存当前Head信息的文件
     * @param treeKey 当前被commit的文件夹的哈希值
     * @param author commit的作者
     * @param committer commit的提交者
     * @param comment commit的备注
     * @param newPath 新生成的文件所在的地址
     * @throws Exception
     */
    public Commit(File head,String treeKey,String author,String committer,String comment,String newPath, String branchName) throws Exception {
        if(!head.exists()) {
            this.newPath = newPath;
            this.author = author;
            this.committer = committer;
            this.comment = comment;
            this.timeStamp = (new Date()).toString();
            this.treeKey = treeKey;
            this.lastKey = null;
            setHashCode();
            File tempFile = new File(newPath+"\\commit");
            if(!tempFile.exists()) {
                tempFile.mkdir();
            }
            branch = branchName;
            String newPath01 = newPath + "\\commit\\" + hashCode + ".txt";
            GitUtils.generateFolderValue(new File(newPath01), this.toString());
            String newPath02 = newPath + "\\" + "head.txt";
            GitUtils.generateFolderValue(new File(newPath02), hashCode + " " + branchName);
        }
        else {
            String lastTreeKey = getLastKey(head);
            if(!treeKey.equals(lastTreeKey)) {
                //从head文件中读取上次commit的key
                this.lastKey = GitUtils.readFirstLine(head);
                this.newPath = newPath;
                this.author = author;
                this.committer = committer;
                this.comment = comment;
                branch = branchName;
                this.timeStamp = (new Date()).toString();
                this.treeKey = treeKey;
                setHashCode();
                String tempPath = newPath + "\\commit\\" + hashCode + ".txt";
                GitUtils.generateFolderValue(new File(tempPath), this.toString());
                GitUtils.writeLine(head, hashCode + " " + branchName);
            }
        }
    }

    public Commit(File commitFile) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(commitFile));
        treeKey = bfr.readLine().split(" ")[1];
        lastKey = bfr.readLine().split(" ")[1];
        author = bfr.readLine().split(" ")[1];
        committer = bfr.readLine().split(" ")[1];
        comment = bfr.readLine();
        newPath = commitFile.getParent();
        timeStamp = bfr.readLine();
        bfr.close();
    }

  //获取lastTreekey
    public String getLastKey(File head) {
        try {
            //从head文件总找到上次commit的key
            String lastCommitKey = GitUtils.readFirstLine(head);
            //根据lastcommit的key找到对应的commit文件
            File lastCommitFile = GitUtils.findFile(lastCommitKey, head.getParent()+"\\commit");
            try{
                //从commit文件中读取tree的key
                String res = (GitUtils.readFirstLine(lastCommitFile,false));
                return res;
            }
            catch (FileNotFoundException e) {
                System.out.println("file not found");
                return null;
            }
        } 
        catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException exist");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHashCode() {
        return hashCode;
    }
 
    //根据commmit所有的value计算commit对象的哈希值
    public void setHashCode() {
        try {
            this.hashCode = GitUtils.HashCompute(new ByteArrayInputStream(toString().getBytes()));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getTreeKey() {return treeKey;}

    public String toString() {
        return "Tree " + treeKey + '\n' + "parent " + lastKey + '\n' + "author " + author + '\n' +"committer " + committer + '\n' + comment + '\n' + timeStamp + '\n' + "branch " + branch;
    }

}
