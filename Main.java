import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("default");
        board.displayBoard();
        Scanner scanner = new Scanner(System.in);
        System.out.println("next move");
        String userInput = scanner.nextLine();
        System.out.println(userInput);
    }
}
