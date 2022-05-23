import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Main {
    public static void main(String[] args) {
        String setting = args[0];
        String ai = args[1];
        Game newGame = new Game(setting);
        newGame.showBoard();
        System.out.println();
        //testMove(newGame);
        if (ai.equals("false")) {
            runGame(newGame);
        } else if (ai.equals("true")){
            runGameAI2(newGame);
        }
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
                Move Move = new Move(testMove1[i], newGame.getCurrentTurn(), newGame.getBoard(), false);
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

    static void runGameAI(Game newGame) {
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

    static void runGameAI2(Game newGame) {
        int counter = 0;
        while (!newGame.hasWinner()) {
            conductMoveAISmart(newGame);
            counter++;
            if (!newGame.hasWinner()) {
                conductMoveAISmart(newGame);
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
        List<Move> moves = ai.getLegalMoves();
        int randomIndex = rand.nextInt(moves.size());
        newGame.executeMove(moves.get(randomIndex));
        newGame.showBoard();
        System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    private static void conductMoveAISmart(Game newGame) {
        AI ai = new AI(newGame.getBoard(), newGame.getCurrentTurn());
        Move move = ai.findMove(newGame);
        newGame.executeMove(move);
        newGame.showBoard();
        System.out.println("White dead: " + newGame.getBoard().getKilledWhite());
        System.out.println("Black dead: " + newGame.getBoard().getKilledBlack());
        _moveExecuted = false;
    }

    static boolean _moveExecuted = false;
    static String _winner;
}
