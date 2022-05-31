import java.io.IOException;
import java.util.Scanner;


/** Entry point for the simple abalone game.
 *  @author Isaiah Ou
 */

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String setting = args[0];
        String aiPlaying = args[1];
        Game newGame = new Game(setting);
        GUI gui = new GUI(newGame);
        newGame.showBoard();
        System.out.println();
        if (aiPlaying.equals("false")) {
            runGame(newGame);
        } else if (aiPlaying.equals("true")) {
            String aiPlayers = args[2];
            if (aiPlayers.equals("2")) {
                runGameAI2(newGame);
            } else if (aiPlayers.equals("1")) {
                runGameAI(newGame);
            }
        }
        System.out.println("The winner is " + _winner + "!");
    }

    private static void runGame(Game newGame) throws IOException, ClassNotFoundException {
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

    private static void conductMove(Game newGame, Scanner scanner, String message) throws IOException, ClassNotFoundException {
        String userInput = scanner.nextLine();
        System.out.println(userInput);
        while (!_moveExecuted) {
            Move Move = new Move(userInput, newGame.getCurrentTurn(),newGame.getBoard(), false);
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

    private static void runGameAI (Game newGame) throws IOException, ClassNotFoundException {
        while (!newGame.hasWinner()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Black's move:");
            conductMove(newGame, scanner, "Black's move:");
            if (!newGame.hasWinner()) {
                conductMoveAI(newGame);
            }
        }
        if (newGame.getCurrentTurn() == Pieces.WHITE) {
            _winner = "black";
        } else {
            _winner = "white";
        }
    }

    private static void runGameAI2 (Game newGame) throws IOException, ClassNotFoundException {
        int counter = 0;
        while (!newGame.hasWinner()) {
            conductMoveAI(newGame);
            counter++;
            if (!newGame.hasWinner()) {
                conductMoveAI(newGame);
                counter++;
            }
        }
        System.out.println("counter: " + counter + " moves");
        if (newGame.getCurrentTurn() == Pieces.WHITE) {
            _winner = "black";
        } else {
            _winner = "white";
        }
    }


    private static void conductMoveAI(Game newGame) throws IOException, ClassNotFoundException {
        AI ai = new AI(newGame);
        Move bestMove = ai.findMove();
        newGame.executeMove(bestMove);
        newGame.showBoard();
        System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    /** Boolean value to record whether a move has been executed. */
    private static boolean _moveExecuted = false;

    /** String that states the winner's color. */
    private static String _winner;

}
