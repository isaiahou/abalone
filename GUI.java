import javax.swing.*;
import java.io.Serializable;

public class GUI implements Serializable {
    GUI() {
        _frame = new JFrame();
        createJFrame();
    }

    public void createJFrame() {
        _frame.setTitle("Abalone Board Game");
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.setResizable(false);
        _frame.setSize(600,600);
        _frame.setVisible(true);
        ImageIcon image = new ImageIcon("IconTransparent.png");
        _frame.setIconImage(image.getImage());
    }

    private JFrame _frame;

}


