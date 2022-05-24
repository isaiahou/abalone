import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

/** Class that contains the method used by the AI.
 *  @author Isaiah Ou
 */

public class AI {
    AI (Board board, Pieces color) {
        _board = new Board(board);
        _AIColor = color;
    }

    List<String> getMyPieces(Game copyNewGame) {
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

    List<String> getMarbleStrings(Game copyNewGame) {
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

    List<Move> getLegalMoves(Game copyNewGame) {
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

    /** Tracks the board instance of the given game. */
    Board _board;

    /** Tracks the AI's color in the given game. */
    Pieces _AIColor;
}
