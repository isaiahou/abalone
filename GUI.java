import javax.swing.*;
import java.io.Serializable;

public class GUI implements Serializable {
    GUI() {
        _frame = new JFrame();
        createJFrame();
    }

    public void createJFrame() {
        GUIBoard board = new GUIBoard();
        _frame.add(board);
        _frame.setTitle("Abalone Board Game");
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.setResizable(true);
        _frame.pack();
        _frame.setVisible(true);

        ImageIcon image = new ImageIcon("IconTransparent.png");
        _frame.setIconImage(image.getImage());
    }

    private JFrame _frame;

}


