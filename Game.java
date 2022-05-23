import java.util.TreeSet;

public class Game {

    Game(String setting) {
        _board = new Board(setting);
        _previousBoard = new Board(setting);
    }

    Game(String setting, Game newGame) {
        _board = new Board(setting, newGame.getBoard());
        _currentTurn = newGame.getCurrentTurn();
        _previousBoard = newGame.getPreviousBoard();
    }

    boolean hasWinner() {
        return _board.getKilledWhite() >= 6
                || _board.getKilledBlack() >= 6;
    }

    void executeMove(Move move) {
        _previousBoard.setBoard(_board);
        TreeSet<Integer> opponentNewPositions = new TreeSet<>();
        TreeSet<Integer> newPositions = new TreeSet<>();
        int direction = move.getDirection();
        if (move.getPushOpponent()) {
            for (int pos: move.getOpponentMarbleString()) {
                int newPos = _board.getAdjencentCells()[pos][direction];
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
            int newPos = _board.getAdjencentCells()[pos][direction];
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
        _board.setBoard(_previousBoard);
        switchCurrentTurn();
    }

    void switchCurrentTurn() {
        if (_currentTurn == Pieces.BLACK) {
            _currentTurn = Pieces.WHITE;
        } else {
            _currentTurn = Pieces.BLACK;
        }
    }

    Board getBoard() { return _board; }

    Board getPreviousBoard() {
        return _previousBoard;
    }

    Pieces getCurrentTurn() { return _currentTurn; }
    
    void showBoard() {
        _board.displayBoard();
    }

    void resetBoard() {
        _board = new Board("default");
        _currentTurn = Pieces.BLACK;
    }

    private Board _board;
    private Pieces _currentTurn = Pieces.BLACK;
    private Board _previousBoard;
}
