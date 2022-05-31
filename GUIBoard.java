import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public class GUIBoard extends JFrame implements Serializable, MouseListener {
    public GUIBoard(Game game) {
        _game = game;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(null);

        testPiece = new GUIPiece();
        label = new JLabel(testPiece);
        label.addMouseListener(this);
        label.setBounds(0,0,40,40);

        this.setLayout(null);
        this.add(label);
        this.setVisible(true);
    }

    private void setUpPieces() {
        for (char row = 'a'; row <= 'i'; row++) {
            for (int col = 1; col <= 9; col++) {

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked mouse");
        testPiece.setColor(Color.red);
        label.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    JLabel label;
    GUIPiece testPiece;
    private Game _game;
}
