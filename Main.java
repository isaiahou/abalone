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

        static String[] testMove1 = { "c5-a5,d5",
                "g5,f5",
                "d5-b5,e5",
                "i5-h4,g3",
                "e5-c5,f5",
                "h4-g3,f2",
                "f5-d5,g5",
                "g3-f2,e1",
                "e5-g5,h5",
                "i7-g7,f7",
                "h5-f5,i5",
                "h7-f5,e4" }; //move 11

        static void testMove(Game newGame) {
            for (int i = 0; i < testMove1.length; i++) {
                Move Move = new Move(testMove1[i], newGame.getBoard());
                if (Move.isValidMove()) {
                    if (Move.getCurrentColor() == newGame.getCurrentTurn()) {
                        newGame.executeMove(Move);
                        newGame.showBoard();
                        System.out.println("move " + i + " " + Move.getCurrentColor());
                    } else {
                        System.out.println("Invalid move: you cannot move your opponent's marbles.");
                    }
                }
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
