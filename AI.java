import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AI {
    AI (Board board, Pieces color) {
        _board = board;
        _AIColor = color;

    }

    List<String> getMyPieces() {
        ArrayList<String> myPieces = new ArrayList<>();
        for (char row = 'a'; row <= 'i'; row++) {
            for (int col = 1; col <= 9; col++) {
                String position = "" + row + col;
                if (_board.getPiece(position) == _AIColor) {
                    myPieces.add(position);
                }
                if (myPieces.size() == 26) {
                    return myPieces;
                }
            }
        }
        return myPieces;
    }

    List<String> getMarbleStrings() {
        ArrayList<String> marbleStrings = new ArrayList<>();
        Pattern positionString = Pattern.compile("[a-i][1-9]");
        for (String myPiecePos : getMyPieces()) {
            char myPieceRow = myPiecePos.charAt(0);
            char myPieceCol = myPiecePos.charAt(1);
            for (int rowShift = -2; rowShift <= 2; rowShift++) {
                for (int colShift = -2; colShift <= 2; colShift++) {
                    char destinationRow = ((char) (myPieceRow + rowShift));
                    char destinationCol = ((char) (myPieceCol + colShift));
                    String destinationPos = "" + destinationRow + destinationCol;
                    Matcher positionMatcher = positionString.matcher(destinationPos);
                    if (positionMatcher.matches()
                        && _board.getPiece(_board.toIndex(destinationPos)) == _AIColor
                        && !myPiecePos.equals(destinationPos)
                        && !marbleStrings.contains(myPiecePos + "-" + destinationPos)
                        && !marbleStrings.contains(destinationPos + "-" + myPiecePos)) {
                        marbleStrings.add(myPiecePos + "-" + destinationPos);
                    }
                }
            }
        }
        marbleStrings.addAll(getMyPieces());
        return marbleStrings;
    }

    List<Move> getLegalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();
        Pattern positionString = Pattern.compile("[a-i][1-9]");
        for (String marbleString : getMarbleStrings()) {
            for (int rowShift = -1; rowShift <= 1; rowShift++) {
                for (int colShift = -1; colShift <=1; colShift++) {
                    char myPieceRow = marbleString.charAt(0);
                    char myPieceCol = marbleString.charAt(1);
                    char destinationRow = ((char) (myPieceRow + rowShift));
                    char destinationCol = ((char) (myPieceCol + colShift));
                    String destinationPos = "" + destinationRow + destinationCol;
                    Matcher positionMatcher = positionString.matcher(destinationPos);
                    if (positionMatcher.matches() && !marbleString.equals(destinationPos)) {
                        Move move = new Move(marbleString + "," + destinationPos, _AIColor, _board, true);
                        if (move.isValidMove()) {
                            legalMoves.add(move);
                        }
                    }
//                    if (marbleString.length() == 2) {
//                        if (positionMatcher.matches()
//                                && !marbleString.equals(destinationPos)) {
//                            Move move = new Move(marbleString + "," + destinationPos, _AIColor, _board);
//                            if (move.isValidMove()) {
//                                legalMoves.add(move);
//                            }
//                        }
//                    } else {
//                        boolean possibleMove = true;
//                        for (String marble: createMarbleString(marbleString)) {
//                            char myPieceRow2 = marble.charAt(0);
//                            char myPieceCol2 = marble.charAt(1);
//                            char destinationRow2 = ((char) (myPieceRow2 + rowShift));
//                            char destinationCol2 = ((char) (myPieceCol2 + colShift));
//                            String destinationPos2 = "" + destinationRow2 + destinationCol2;
//                            Matcher positionMatcher2 = positionString.matcher(destinationPos2);
//                            if (!positionMatcher2.matches()
//                                    || marble.equals(destinationPos)) {
//                                possibleMove = false;
//                                }
//                            }
//                        if (possibleMove) {
//                            char myPieceRow3 = marbleString.charAt(0);
//                            char myPieceCol3 = marbleString.charAt(1);
//                            char destinationRow3 = ((char) (myPieceRow3 + rowShift));
//                            char destinationCol3 = ((char) (myPieceCol3 + colShift));
//                            String destinationPos3 = "" + destinationRow3 + destinationCol3;
//                            Move move = new Move(marbleString + "," + destinationPos3, _AIColor, _board);
//                            if (move.isValidMove()) {
//                                legalMoves.add(move);
//                            }
//                        }
//                    }
                }
            }
        }
        return legalMoves;
    }

    LinkedList<String> createMarbleString(String marbleString) {
        LinkedList<String> marbleStringList = new LinkedList<>();
        String firstMarble = marbleString.substring(0,2);
        String secondMarble = marbleString.substring(3);
        for (int i = 0; i < 6; i++) {
            int pointerMarblePosition = _board.toIndex(firstMarble);
            for (int j = 0; j < 3; j++) {
                if (_board.getPiece(pointerMarblePosition) == _AIColor) {
                    String marblePosition = _board.indexToString(pointerMarblePosition);
                    marbleStringList.add(marblePosition);
                    if (_board.toIndex(secondMarble) == pointerMarblePosition) {
                        return marbleStringList;
                    } else {
                        pointerMarblePosition = _board.getAdjencentCells()[pointerMarblePosition][i];
                    }
                }
            }
            marbleStringList.clear();
        }
        return marbleStringList;
    }

    Move findMove(Game newGame) {
        Game copyNewGame = new Game("default", newGame);
        _lastFoundMove = null;
        if (newGame.getCurrentTurn() == Pieces.BLACK) {
            minMax(copyNewGame, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(copyNewGame, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    private int minMax(Game newGame, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        if (depth == 0 || newGame.hasWinner()) {
            return staticScore(newGame, WINNING_VALUE + depth);
        }

        Move best;
        best = null;
        int bestScore;
        if (sense == 1) {
            bestScore = -INFTY;
        } else {
            bestScore = INFTY;
        }

        List<Move> legalMoves = getLegalMoves();
        for (Move m : legalMoves) {
            if (sense == 1) {
                newGame.executeMove(m);
                int response = minMax(newGame, depth - 1, false, -1, alpha, beta);
                if (response > bestScore) {
                    best = m;
                    bestScore = response;
                    alpha = max(alpha, bestScore);
                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
                newGame.undoMove();
            } else {
                newGame.executeMove(m);
                int response = minMax(newGame, depth - 1, false, 1, alpha, beta);
                if (response < bestScore) {
                    best = m;
                    bestScore = response;
                    beta = min(beta, bestScore);
                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
                newGame.undoMove();
            }
        }

        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }


    private int staticScore(Game game, int winningValue) {
        if (game.hasWinner()) {
            return switch (game.getCurrentTurn()) {
                case BLACK -> winningValue;
                case WHITE -> -winningValue;
                default -> 0;
            };
        }
        return game.getBoard().getKilledWhite() - game.getBoard().getKilledBlack();
    }

    private static final int MAX_DEPTH = 1;
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    private static final int INFTY = Integer.MAX_VALUE;
    private Move _lastFoundMove;
    Board _board;
    Pieces _AIColor;
}
