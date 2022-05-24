import java.util.List;
import java.util.Random;
import java.util.Scanner;


/** Entry point for the simple abalone game.
 *  @author Isaiah Ou
 */

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        String aiPlaying = args[1];
        Game newGame = new Game(setting);
        newGame.showBoard();
        System.out.println();
        if (aiPlaying.equals("false")) {
            runGame(newGame);
        } else if (aiPlaying.equals("true")) {
            String aiPlayers = args[2];
            if (aiPlayers.equals("2")) {
                runGameAIDumb2(newGame);
            } else if (aiPlayers.equals("1")) {
                runGameAIDumb(newGame);
            }
        }
        System.out.println("The winner is " + _winner + "!");
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

    static void runGameAIDumb (Game newGame) {
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

    static void runGameAIDumb2(Game newGame) {
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

    private static void conductMoveAI(Game newGame) {
        AI ai = new AI(newGame.getBoard(), newGame.getCurrentTurn());
        Random rand = new Random();
        List<Move> moves = ai.getLegalMoves(newGame);
        int randomIndex = rand.nextInt(moves.size());
        newGame.executeMove(moves.get(randomIndex));
        newGame.showBoard();
        System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    /** Boolean value to record whether a move has been executed. */
    static boolean _moveExecuted = false;

    /** String that states the winner's color. */
    static String _winner;
}
