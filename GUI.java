import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class GUI implements Serializable {
    GUI(Game game, int numPlayers) {
        _board = new GUIBoard(game, numPlayers);
        _numPlayers = numPlayers;
    }

    public GUIBoard getBoard() {
        return _board;
    }

    public void updateGame(Game game) {
        _game = game;
        _board.updateGame(game);
    }

    private GUIBoard _board;
    private Game _game;

    private int _numPlayers;

}


