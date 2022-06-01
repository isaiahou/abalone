import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class GUIPieceIcon implements Serializable, Icon {

    public GUIPieceIcon(String position, Color color) {
        _color = color;
        _position = position;
    }

    public void setColor(Color color) {
        this._color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(_color);
        g2d.fillOval(0, 0, 40, 40);
    }

    @Override
    public int getIconWidth() {
        return 40;
    }

    @Override
    public int getIconHeight() {
        return 40;
    }

    private Color _color;

    private String _position;

}
