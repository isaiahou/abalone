import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        Game newGame = new Game(setting);
        newGame.showBoard();
        System.out.println();
        String testMoveStr = "c3-c5,d6";
        Move testMove = new Move(testMoveStr,newGame.getBoard());
        if (testMove.isValidMove()) {
            newGame.executeMove(testMove);
        }
        newGame.showBoard();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("White's turn (O)");
//        String userInput = scanner.nextLine();
//        System.out.println(userInput);
    }
}
