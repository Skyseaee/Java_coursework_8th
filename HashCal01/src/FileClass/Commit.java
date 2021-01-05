package FileClass;
import java.io.FileNotFoundException;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.File;

public class Commit{
    private String hashCode; //当前被commit的文件夹的哈希值
    private String author;
    private String committer;
    private String comment;
    private String timeStamp;
    private String lastKey;
    private String treeKey;
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
                this.timeStamp = (new Date()).toString();
                this.treeKey = treeKey;
                setHashCode();
                newPath = newPath + "\\commit\\" + hashCode + ".txt";
                GitUtils.generateFolderValue(new File(newPath), this.toString());
                GitUtils.writeLine(head, hashCode + " " + branchName);
            }
        }
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

    public String toString() {
        return "Tree " + treeKey + '\n' + "parent " + lastKey + '\n' + "author " + author + '\n' +"committer " + committer + '\n' + comment + '\n' + timeStamp;
    }

}
