package FileClass;

import java.io.File;

public class GitCommand {
    private GitCommandFunction gitCommand;
    private String filePath;
    private String userName;

    public GitCommand() {
        gitCommand = null;
        filePath = "";
        userName = "";
    }

    public GitCommandFunction getGitCommand() {
        return gitCommand;
    }

    public void setGitCommandFunction(GitCommandFunction gitCommandFunction) {
        this.gitCommand = gitCommandFunction;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void switchCommand(String command) throws Exception {
        String[] commandList = command.split(" ");
        if(commandList.length>1 && commandList[0].equals("git")) {
            if(commandList[1].equals("init")) {
                if(commandList.length>2) {
                    filePath = "";
                    for(int i=2;i<commandList.length;i++) {
                        this.filePath += commandList[i];
                        if(i!=commandList.length-1)
                            this.filePath += " ";
                    }
                    if(!new File(filePath+"-git\\head.txt").exists()) {
                        if(userName.length()>=1) {
                            gitCommand = new GitCommandFunction(userName, filePath);
                            normalPrint("Initialization was successful.");
                        }
                        else if(userName.length()<1){
                            errPrint("Your username is empty, please use \"git config userName\" to set your userName.");
                        }
                        else {
                            errPrint("Initialization failed.");
                        }
                    }
                    else if(new File(filePath+"-git\\head.txt").exists()){
                        gitCommand = new GitCommandFunction(filePath);
                        userName = gitCommand.getUserName();
                        normalPrint("Initialization was successful.");
                    }
                    else {
                        errPrint("Your username is empty, please use \"git config userName\" to set your userName.");
                    }
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("config")) {
                if(userName.length()>=1 && commandList.length==2) {
                    normalPrint(userName);
                }
                else if(commandList.length == 3) {
                    if(!new File(filePath+"-git\\config.txt").exists()) {
                        userName = commandList[2];
                        normalPrint("The userName was set successfully, and now it is '"+userName+"'.");
                    }
                    else if(gitCommand!=null){
                        userName = commandList[2];
                        gitCommand.modifyConfigCommand(commandList[2]);
                        normalPrint("The userName was modified successfully, and now it is '"+userName+"'.");
                    }
                    else {
                        errPrint("The repository has not been initialized or the 'git' is not been initialized");
                    }
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("branch")) {
                if(commandList.length==2) {
                    if(gitCommand!=null) {
                        gitCommand.branchCommand();
                        normalPrint("The branch info.");
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else if(commandList.length==3) {
                    if(gitCommand!=null) {
                        if(!commandList[2].equals("-d")) {
                            char temp = commandList[2].toCharArray()[0];
                            if(temp != '-') {
                                gitCommand.createBranchCommand(commandList[2]);
                                gitCommand.branchCommand();
                                normalPrint("Creating branch "+commandList[2]+" successfully.");
                            }
                            else {
                                errPrint("The branch name can't start with '-' .");
                            }
                        }
                        else {
                            gitCommand.deleteBranchCommand(commandList[3]);
                        }
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else if(commandList.length==4) {
                    if(gitCommand!=null) {
                        if(commandList[2].equals("-d")) {
                            boolean res = gitCommand.deleteBranchCommand(commandList[3]);
                            if(res) {
                                normalPrint("The branch was successfully deleted.");
                            }
                            else if(commandList[3].equals(gitCommand.getCurrentBranch().getBranchName()))
                                errPrint("The current branch cannot be deleted.");
                            else {
                                errPrint("The current branch does not exist, please re-check.");
                            }
                        }
                        else {
                            errPrint("the command is illegal, please use '-d' if you want to delete branch.");
                        }
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("commit")) {
                if(commandList.length>=3) {
                    String temp = "";
                    if(gitCommand!=null) {
                        for(int i=2;i<commandList.length;i++) {
                            temp += commandList[i];
                            temp += " ";
                        }
                        gitCommand.commitCommand(temp);
                        normalPrint("Commit success.");
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("checkout")) {
                if(commandList.length==3) {
                    if(gitCommand!=null) {
                        gitCommand.checkoutCommand(commandList[2]);
                        normalPrint("The current branch is "+commandList[2]+".");
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("log")) {
                if(commandList.length==3) {
                    if(gitCommand!=null)
                        gitCommand.logCommand();
                    else
                        errPrint("The repository has not been initialized");
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("reset")) {
                if(commandList.length==3) {
                    boolean res = false;
                    if(gitCommand!=null) {
                        res = gitCommand.rollBackCommand(commandList[2]);
                        if(res)
                            normalPrint("rollback success.");
                        else
                            errPrint("The key is not found.");
                    }
                    else
                        errPrint("The repository has not been initialized");
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else if(commandList[1].equals("exit")) {
                if(commandList.length==2) {
                    System.exit(0);
                }
                else {
                    errPrint("Unrecognized spaces exist.");
                }
            }
            else {
                errPrint("The command you entered is not existed, please check the command below.");
            }
        }
        else {
            errPrint("The command you entered is not a git command, please start with \"git\"");
        }
    }

    public static void normalPrint(String message) {
        System.out.println(message);
        System.out.println("---------------------------------------------------------------------");
    }

    public static void errPrint(String message) {
        System.err.println(message);
        System.out.println("---------------------------------------------------------------------");
    }
}
