import java.io.Serializable;

/** The GUI object that contains the GUIBoard.
 *  Allows for external access from outside the GUIBoard.
 *  @author Isaiah Ou
 */

public class GUI implements Serializable {

    GUI(Game game, int numPlayers) {
        _board = new GUIBoard(game, numPlayers);
    }

    public GUIBoard getBoard() {
        return _board;
    }

    public void updateGame(Game game) {
        _board.updateGame(game);
    }

    /** The GUI's GUIBoard instance. */
    private final GUIBoard _board;

}


