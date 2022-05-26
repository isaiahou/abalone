import java.io.IOException;
import java.io.Serializable;
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

    boolean hasWinner() {
        return _board.getKilledWhite() >= 6
                || _board.getKilledBlack() >= 6;
    }

    Pieces getWinner() {
        assert hasWinner();
        if (_board.getKilledBlack() >= 6) {
            return Pieces.WHITE;
        } else {
            return Pieces.BLACK;
        }
    }

    void executeMove(Move move) throws IOException, ClassNotFoundException {
        _previousBoards.add(DeepClone.cloneBoard(_board));
        TreeSet<Integer> opponentNewPositions = new TreeSet<>();
        TreeSet<Integer> newPositions = new TreeSet<>();
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

    void undoMove() {
        _board = _previousBoards.pop();
        switchCurrentTurn();
    }

    void switchCurrentTurn() {
        if (_currentTurn == Pieces.BLACK) {
            _currentTurn = Pieces.WHITE;
        } else {
            _currentTurn = Pieces.BLACK;
        }
    }

    void showBoard() { _board.displayBoard(); }

    void resetBoard() {
        _board = new Board("default");
        _currentTurn = Pieces.BLACK;
    }

    Board getBoard() { return _board; }

    Stack<Board> getPreviousBoards() { return _previousBoards; }

    Pieces getCurrentTurn() { return _currentTurn; }

    /** Holds the board instance of the game. */
    private Board _board;

    /** Holds the board instances of the previous moves in the game. */
    private final Stack<Board> _previousBoards;

    /** Tracks which color's turn it is. */
    private Pieces _currentTurn;

}
