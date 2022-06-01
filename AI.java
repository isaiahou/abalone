import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that contains the method used by the AI.
 *  @author Isaiah Ou
 */

public class AI {
    AI (Game game) {
        _game = game;
    }

    private List<String> getMyPieces(Game copyNewGame) {
        ArrayList<String> myPieces = new ArrayList<>();
        for (char row = 'a'; row <= 'i'; row++) {
            for (int col = 1; col <= 9; col++) {
                String position = "" + row + col;
                if (copyNewGame.getBoard().getPiece(position) == copyNewGame.getCurrentTurn()) {
                    myPieces.add(position);
                }
                if (myPieces.size() == 26) {
                    return myPieces;
                }
            }
        }
        return myPieces;
    }

    private List<String> getMarbleStrings(Game copyNewGame) {
        ArrayList<String> marbleStrings = new ArrayList<>();
        Pattern positionString = Pattern.compile("[a-i][1-9]");
        for (String myPiecePos : getMyPieces(copyNewGame)) {
            char myPieceRow = myPiecePos.charAt(0);
            char myPieceCol = myPiecePos.charAt(1);
            for (int rowShift = -2; rowShift <= 2; rowShift++) {
                for (int colShift = -2; colShift <= 2; colShift++) {
                    char destinationRow = ((char) (myPieceRow + rowShift));
                    char destinationCol = ((char) (myPieceCol + colShift));
                    String destinationPos = "" + destinationRow + destinationCol;
                    Matcher positionMatcher = positionString.matcher(destinationPos);
                    if (positionMatcher.matches()
                        && copyNewGame.getBoard().getPiece(copyNewGame.getBoard().toIndex(destinationPos)) == copyNewGame.getCurrentTurn()
                        && !myPiecePos.equals(destinationPos)
                        && !marbleStrings.contains(myPiecePos + "-" + destinationPos)
                        && !marbleStrings.contains(destinationPos + "-" + myPiecePos)) {
                        marbleStrings.add(myPiecePos + "-" + destinationPos);
                    }
                }
            }
        }
        marbleStrings.addAll(getMyPieces(copyNewGame));
        return marbleStrings;
    }

    private ArrayList<Move> getLegalMoves(Game copyNewGame) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        Pattern positionString = Pattern.compile("[a-i][1-9]");
        for (String marbleString : getMarbleStrings(copyNewGame)) {
            for (int rowShift = -1; rowShift <= 1; rowShift++) {
                for (int colShift = -1; colShift <=1; colShift++) {
                    char myPieceRow = marbleString.charAt(0);
                    char myPieceCol = marbleString.charAt(1);
                    char destinationRow = ((char) (myPieceRow + rowShift));
                    char destinationCol = ((char) (myPieceCol + colShift));
                    String destinationPos = "" + destinationRow + destinationCol;
                    Matcher positionMatcher = positionString.matcher(destinationPos);
                    if (positionMatcher.matches() && !marbleString.equals(destinationPos)) {
                        Move move = new Move(marbleString + "," + destinationPos, copyNewGame.getCurrentTurn(), copyNewGame.getBoard(), true);
                        if (move.isValidMove()) {
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public Move findMove() throws IOException, ClassNotFoundException {
        Game copyGame = DeepClone.cloneGame(_game);
        _bestMove = null;
        if (_game.getCurrentTurn() == Pieces.BLACK) {
            minMax(copyGame, MAX_DEPTH, -INFINITY, INFINITY, true, true);
        } else if (_game.getCurrentTurn() == Pieces.WHITE){
            minMax(copyGame, MAX_DEPTH, -INFINITY, INFINITY, false, true);
        }
        return _bestMove;
    }

    private int minMax(Game game, int depth, int alpha, int beta, boolean maximizingPlayer, boolean saveMove) throws IOException, ClassNotFoundException {
        if (depth == 0 || game.hasWinner()) {
            return staticEvaluation(game);
        }
        Move best;
        int bestScore;
        if (maximizingPlayer) {
            bestScore = -INFINITY;
        } else {
            bestScore = INFINITY;
        }
        ArrayList<Move> legalMoves = getLegalMoves(game);
        Collections.shuffle(legalMoves);
        for (Move m: legalMoves) {
            if (maximizingPlayer) {
                game.executeMove(m);
                int response = minMax(game, depth - 1, alpha, beta, false, false);
                if (response > bestScore) {
                    best = m;
                    bestScore = response;
                    if (saveMove) {
                        _bestMove = best;
                    }
                }
                game.undoMove();
            } else {
                game.executeMove(m);
                int response = minMax(game, depth - 1, alpha, beta, true, false);
                if (response < bestScore) {
                    best = m;
                    bestScore = response;
                    if (saveMove) {
                        _bestMove = best;
                    }
                }
                game.undoMove();
            }
        }
        return staticEvaluation(game);
    }

    int staticEvaluation(Game game) {
        if (game.hasWinner()) {
            if (game.getWinner() == Pieces.BLACK) {
                return WINNING_VALUE;
            } else {
                return -WINNING_VALUE;
            }
        } else {
            int[] centerPieceIndices = { 60, 71, 72, 61, 49, 48, 59 };
            int centerCounter = 0;
            for (int i: centerPieceIndices) {
                if (game.getBoard().getPiece(i) == Pieces.BLACK) {
                    centerCounter++;
                }
            }
            return (int) (game.getBoard().getKilledWhite() + centerCounter/7*0.05);
        }
    }

    /** Integer value representing the static evaluation of a win. */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;

    /** Integer value representing infinity for the minMax algorithm. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** Integer value of the max depth that the minMax algorithm will search. */
    private static final int MAX_DEPTH = 2;

    /** Move instance that records the best move found from the minMax algorithm. */
    private static Move _bestMove;

    /** Tracks the current game the AI is playing. */
    Game _game;

}
