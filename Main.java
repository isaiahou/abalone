import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        Game newGame = new Game(setting);
        newGame.showBoard();
        System.out.println();
        //testMove(newGame);
        runGame(newGame);
        //System.out.println("The winner is " + _winner + "!");
        }

        static String[] testMove1 = { "c5,d5",
                "i5-g5,f5",
                "a1-c3,d4",
                "i9-g7,f6",
                "a4-d4,e4",
                "d4-b4,e4",
                "h4,g4",
                "d5,e5"} ;

        static void testMove(Game newGame) {
            for (int i = 0; i < testMove1.length; i++) {
                Move Move = new Move(testMove1[i], newGame.getCurrentTurn(), newGame.getBoard());
                if (Move.isValidMove()) {
                    newGame.executeMove(Move);
                    newGame.showBoard();
                    System.out.println("move " + i + " " + Move.getCurrentColor());
                    System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
                    System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
                }
            }
        }

        static void runGame(Game newGame) {
            while (!newGame.hasWinner()) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Black's move:");
                conductMove(newGame, scanner, "Black's move:");
                if (!newGame.hasWinner()) {
                    System.out.println("White's move:");
                    conductMove(newGame, scanner, "White's move:");
                }
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
            Move Move = new Move(userInput, newGame.getCurrentTurn(),newGame.getBoard());
            if (Move.isValidMove()) {
                newGame.executeMove(Move);
                _moveExecuted = true;
            } else {
                System.out.println(message);
                userInput = scanner.nextLine();
                System.out.println(userInput);
            }
        }
        newGame.showBoard();
        System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    static boolean _moveExecuted = false;
    static String _winner;
}
