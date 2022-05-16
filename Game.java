public class Game {

    Game(String setting) {
        _board = new Board(setting);
    }

    boolean checkWinner() {
        return _board.getKilledWhite() >= 6
                || _board.getKilledBlack() >= 6;
    }

    private Board _board;
    private Pieces currentTurn = Pieces.WHITE;
}
