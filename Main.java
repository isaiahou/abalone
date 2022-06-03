import java.io.IOException;
import java.util.Scanner;


/** Entry point for the simple abalone game.
 *  @author Isaiah Ou
 */

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String setting = args[0];
        String gui = args[1];
        String aiPlaying = args[2];
        Game newGame = new Game(setting);
        if (gui.equals("false")) {
            newGame.showBoard();
            System.out.println();
            if (aiPlaying.equals("false")) {
                runGame(newGame);
            } else if (aiPlaying.equals("true")) {
                String aiPlayers = args[3];
                if (aiPlayers.equals("2")) {
                    runGameAI2(newGame, false);
                } else if (aiPlayers.equals("1")) {
                    runGameAI(newGame);
                }
            }
            System.out.println("The winner is " + _winner + "!");
        } else {
            if (aiPlaying.equals("false")) {
                _gui = new GUI(newGame, 2);
            } else if (aiPlaying.equals("true")) {
                String aiPlayers = args[3];
                if (aiPlayers.equals("2")) {
                    _gui = new GUI(newGame, 0);
                    runGameAI2(newGame, true);
                } else if (aiPlayers.equals("1")) {
                    _gui = new GUI(newGame, 1);
                    runGameAIGUI(newGame);
                }
            }
        }
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
        System.out.println("White Dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black Dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    private static void runGameAI (Game newGame) throws IOException, ClassNotFoundException {
        while (!newGame.hasWinner()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Black's move:");
            conductMove(newGame, scanner, "Black's move:");
            if (!newGame.hasWinner()) {
                conductMoveAI(newGame, false);
            }
        }
        if (newGame.getCurrentTurn() == Pieces.WHITE) {
            _winner = "black";
        } else {
            _winner = "white";
        }
    }

    private static void runGameAIGUI(Game newGame) throws IOException, ClassNotFoundException, InterruptedException {
        while (!newGame.hasWinner()) {
            if (newGame.getCurrentTurn() == Pieces.WHITE) {
                conductMoveAI(newGame, true);
            } else {
                Thread.sleep(100);
            }
        }
        if (newGame.getCurrentTurn() == Pieces.WHITE) {
            _winner = "black";
        } else {
            _winner = "white";
        }
    }

    private static void runGameAI2 (Game newGame, boolean GUI) throws IOException, ClassNotFoundException {
        int counter = 0;
        while (!newGame.hasWinner()) {
            conductMoveAI(newGame, GUI);
            counter++;
            if (!newGame.hasWinner()) {
                conductMoveAI(newGame, GUI);
                counter++;
            }
        }
        if (!GUI) {
            System.out.println("Move Counter: " + counter + " moves");
        }
        if (newGame.getCurrentTurn() == Pieces.WHITE) {
            _winner = "black";
        } else {
            _winner = "white";
        }
    }


    private static void conductMoveAI(Game newGame, boolean GUI) throws IOException, ClassNotFoundException {
        AI ai = new AI(newGame);
        Move bestMove = ai.findMove();
        newGame.executeMove(bestMove);
        if (GUI) {
            _gui.updateGame(newGame);
            _gui.getBoard().updateBoard();
        } else {
            newGame.showBoard();
            System.out.println("White Dead: " + newGame.getBoard().getKilledWhite());
            System.out.println("Black Dead: " + newGame.getBoard().getKilledBlack());
        }
        _moveExecuted = false;
    }


    /** Boolean value to record whether a move has been executed. */
    private static boolean _moveExecuted = false;

    /** String that states the winner's color. */
    private static String _winner;

    private static GUI _gui;

}
