import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/** Internal representation of the individual spots on the board.
 *  These spots can be recolored to represent the spot's occupation
 *  by a player piece.
 *  @author Isaiah Ou
 */

public class GUIPiece extends JLabel implements Serializable {

    GUIPiece(GUIPieceIcon color, String position) {
        super(color);
        _position = position;
        _pieceIcon = color;
    }

    public String getPosition() {
        return _position;
    }

    public void setColor(Color color) {
        _pieceIcon.setColor(color);
        repaint();
    }

    /** The string representation of the position. */
    private final String _position;

    /** The Icon color component of the piece on the GUI. */
    private final GUIPieceIcon _pieceIcon;

}
