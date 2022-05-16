import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        Board board = new Board(setting);
        Scanner scanner = new Scanner(System.in);
        System.out.println("next move");
        String userInput = scanner.nextLine();
        System.out.println(userInput);
    }
}
