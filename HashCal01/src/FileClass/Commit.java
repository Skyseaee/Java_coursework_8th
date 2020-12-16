package FileClass;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.File;
public class Commit{
    private String hashCode;
    private String author;
    private String committer;
    private String comment;
    private String timestramp;
    private String lastKey;
    private String treeKey;
    private final String type = "Commit";
    public Commit(File head,String treeKey,String author,String committer,String comment,String newPath) throws Exception{
        if(head == null){
            this.author = author;
            this.committer = committer;
            this.comment = comment;
            this.timestramp = (new Date()).toString(); 
            this.treeKey = treeKey;
            this.lastKey = null;
            setHashCode();
            newPath = newPath + '\\' + hashCode + ".txt";
            GitUtils.GenerateValue(new File(newPath), this.toString());
            newPath = "head.txt";
            GitUtils.GenerateValue(new File(newPath), hashCode);
        }
        else{
            this.lastKey = getLastKey(head);
            if(treeKey.equals(lastKey)){
                this.author = author;
                this.committer = committer;
                this.comment = comment;
                this.timestramp = (new Date()).toString(); 
                this.treeKey = treeKey;
                setHashCode();
                newPath = newPath + '\\' + hashCode + ".txt";
                GitUtils.GenerateValue(new File(newPath), this.toString());
                GitUtils.writeLine(head, hashCode);
            }
        }
    }
    public String getLastKey(File head){
        try {
            String lastCommitKey = GitUtils.readFirstLine(head);
            File lastCommiFile = GitUtils.FindFile(lastCommitKey, head.getParent());
            try{
                String res = (GitUtils.readFirstLine(lastCommiFile).split(" "))[1];
                return res;
            }
            catch (Exception e){
                return null;
            }
        } 
        catch (Exception e) {
            return null;
        }
    }
    public String getHashCode() {
        return hashCode;
    }
    public void setHashCode() {
        try {
            this.hashCode = GitUtils.HashCompute(new ByteArrayInputStream(this.toString().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public String toString(){
        String res = "Tree " + treeKey + '\n' + "parent " + lastKey + '\n' + "author " + author + '\n' +"committer " + committer + '\n' + comment + '\n' + timestramp;
        return res;
    }

    
}
