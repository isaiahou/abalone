import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class GUI implements Serializable {
    GUI(Game game) {
        _board = new GUIBoard(game);
    }

    private GUIBoard _board;
    private Game _game;

}


