import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;

/** Class that represents an instance of a game.
 *  @author Isaiah Ou
 */

public class Game implements Serializable {

    Game(String setting) {
        _board = new Board(setting);
        _previousBoards = new Stack<>();
        _previousBoards.add(_board);
        _currentTurn = Pieces.BLACK;
    }

    public boolean hasWinner() {
        return _board.getKilledWhite() >= 6
                || _board.getKilledBlack() >= 6;
    }

    public Pieces getWinner() {
        assert hasWinner();
        if (_board.getKilledBlack() >= 6) {
            return Pieces.WHITE;
        } else {
            return Pieces.BLACK;
        }
    }

    public String getWinnerString() {
        Pieces winner = getWinner();
        if (winner == Pieces.BLACK) {
            return "Black";
        } else {
            return "White";
        }
    }

    public void executeMove(Move move) throws IOException, ClassNotFoundException {
        _previousBoards.add(DeepClone.cloneBoard(_board));
        HashSet<Integer> opponentNewPositions = new HashSet<>();
        HashSet<Integer> newPositions = new HashSet<>();
        int direction = move.getDirection();
        if (move.getPushOpponent()) {
            for (int pos: move.getOpponentMarbleString()) {
                int newPos = _board.getAdjacentCells()[pos][direction];
                if (_board.getPiece(newPos) == Pieces.RAIL) {
                    _board.incrementKilled(_board.getPiece(pos));
                } else {
                    _board.updateBoard(newPos, _board.getPiece(pos));
                    opponentNewPositions.add(newPos);
                }
            }
            for (int pos: move.getOpponentMarbleString()) {
                if (!(opponentNewPositions.contains(pos))) {
                    _board.updateBoard(pos, Pieces.EMPTY);
                }
            }
        }
        for (int pos: move.getMarbleString()) {
            int newPos = _board.getAdjacentCells()[pos][direction];
            if (_board.getPiece(newPos) == Pieces.RAIL) {
                _board.incrementKilled(_board.getPiece(pos));
            } else {
                _board.updateBoard(newPos, _board.getPiece(pos));
            }
            newPositions.add(newPos);
        }
        for (int pos: move.getMarbleString()) {
            if (!(newPositions.contains(pos))) {
                _board.updateBoard(pos, Pieces.EMPTY);
            }
        }
        switchCurrentTurn();
    }

    public void undoMove() {
        _board = _previousBoards.pop();
        switchCurrentTurn();
    }

    private void switchCurrentTurn() {
        if (_currentTurn == Pieces.BLACK) {
            _currentTurn = Pieces.WHITE;
        } else {
            _currentTurn = Pieces.BLACK;
        }
    }

    public void showBoard() { _board.displayBoard(); }

    private void resetBoard() {
        _board = new Board("default");
        _currentTurn = Pieces.BLACK;
    }

    public Board getBoard() { return _board; }

    public Stack<Board> getPreviousBoards() { return _previousBoards; }

    public Pieces getCurrentTurn() { return _currentTurn; }

    public String getCurrentTurnString() {
        if (_currentTurn == Pieces.BLACK) {
            return "Black";
        } else {
            return "White";
        }
    }

    /** Holds the board instance of the game. */
    private Board _board;

    /** Holds the board instances of the previous moves in the game. */
    private final Stack<Board> _previousBoards;

    /** Tracks which color's turn it is. */
    private Pieces _currentTurn;

}
