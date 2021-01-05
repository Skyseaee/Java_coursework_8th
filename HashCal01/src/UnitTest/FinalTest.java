package UnitTest;

import FileClass.GitCommand;

import java.util.Scanner;

public class FinalTest {
    public static void main(String[] args) throws Exception {
        GitCommand git = new GitCommand();
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter command here,sweetie. ->> ");
            String command = input.nextLine();
            git.switchCommand(command);
            input.close();
        }
    }
}
