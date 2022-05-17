import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Game {

    Game(String setting) {
        _board = new Board(setting);
    }

    boolean checkWinner() {
        return _board.getKilledWhite() >= 6
                || _board.getKilledBlack() >= 6;
    }

    void executeMove(Move move) {
        TreeSet<Integer> newPositions = new TreeSet<>();
        for (int pos: move.getMarbleString()) {
            int direction = move.getDirection();
            int newPos = _board.getAdjencentCells()[pos][direction];
            _board.updateBoard(newPos, _board.getPiece(pos));
            newPositions.add(newPos);
        }
        for (int pos: move.getMarbleString()) {
            if (!(newPositions.contains(pos))) {
                _board.updateBoard(pos, Pieces.EMPTY);
            }
        }
    }

    Board getBoard() {
        return _board;
    }

    void showBoard() {
        _board.displayBoard();
    }

    private Board _board;
    private Pieces currentTurn = Pieces.WHITE;
}
