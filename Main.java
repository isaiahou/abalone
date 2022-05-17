import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        Game newGame = new Game(setting);
        newGame.showBoard();
        System.out.println();
        testMove(newGame);
        //runGame(newGame);
        //System.out.println("The winner is " + _winner + "!");
        }

        static void testMove(Game newGame) {
            Move Move = new Move("c5-a5,d5", newGame.getBoard());
            if (Move.isValidMove() && Move.getCurrentColor() == newGame.getCurrentTurn()) {
                newGame.executeMove(Move);
                newGame.showBoard();
                System.out.println();
            }
            Move Move1 = new Move("g5,f5", newGame.getBoard());
            if (Move1.isValidMove() && Move1.getCurrentColor() == newGame.getCurrentTurn()) {
                newGame.executeMove(Move1);
                newGame.showBoard();
                System.out.println();
            }
            Move Move2 = new Move("d5-b5,e5", newGame.getBoard());
            if (Move2.isValidMove() && Move2.getCurrentColor() == newGame.getCurrentTurn()) {
                newGame.executeMove(Move2);
                newGame.showBoard();
                System.out.println();
            }
            Move Move3 = new Move("i5-h4,g3", newGame.getBoard());
            if (Move3.isValidMove() && Move3.getCurrentColor() == newGame.getCurrentTurn()) {
                newGame.executeMove(Move3);
                newGame.showBoard();
                System.out.println();
            }
        }

        static void runGame(Game newGame) {
            while (!newGame.hasWinner()) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Black's move:");
                conductMove(newGame, scanner, "Black's move:");
                System.out.println("White's move:");
                conductMove(newGame, scanner, "White's move:");
            }
            if (newGame.getCurrentTurn() == Pieces.WHITE) {
                _winner = "black";
            } else {
                _winner = "white";
            }
        }

    private static void conductMove(Game newGame, Scanner scanner, String message) {
        String userInput = scanner.nextLine();
        System.out.println(userInput);
        while (!_moveExecuted) {
            Move Move = new Move(userInput, newGame.getBoard());
            if (Move.isValidMove() && Move.getCurrentColor() == newGame.getCurrentTurn()) {
                newGame.executeMove(Move);
                _moveExecuted = true;
            } else {
                System.out.println("Invalid move: you cannot move your opponent's marbles.");
                System.out.println(message);
                userInput = scanner.nextLine();
                System.out.println(userInput);
            }
        }
        newGame.showBoard();
    }

    static boolean _moveExecuted = false;
    static String _winner;
}
