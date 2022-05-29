import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class GUIBoard extends JComponent implements Serializable {
    public GUIBoard() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g.drawPolygon(new Polygon(xPoly, yPoly, 6));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    int[] xPoly = { 0, 150, 450, 600, 450, 150 };
    int[] yPoly = { 300, 0, 0, 300, 600, 600 };
}
