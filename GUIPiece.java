import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public class GUIPiece implements Serializable, Icon {

    public GUIPiece() {
        _color = new Color(131, 105, 83);
    }

    public void setColor(Color color) {
        this._color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
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

}
