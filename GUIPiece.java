import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class GUIPiece extends JLabel implements Serializable {
    GUIPiece(GUIPieceIcon color, String position) {
        super(color);
        _position = position;
        _pieceIcon = color;
    }

    public String getPosition() {
        return _position;
    }

    public GUIPieceIcon getPieceIcon() {
        return _pieceIcon;
    }

    public void setColor(Color color) {
        _pieceIcon.setColor(color);
        repaint();
    }

    private String _position;
    private GUIPieceIcon _pieceIcon;

}
