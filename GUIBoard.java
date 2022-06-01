import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

public class GUIBoard extends JFrame implements Serializable, MouseListener {
    public GUIBoard(Game game) {
        _game = game;
        _pieces = new TreeMap<>();
        _marbleString = new LinkedList<String>();

        this.setTitle("Abalone Board Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(null);
        this.getContentPane().setBackground(BACKGROUND_COLOR);

        setUpPieces();
        updateBoard();

        this.setVisible(true);
        this.setResizable(false);

        ImageIcon image = new ImageIcon("IconTransparent.png");
        this.setIconImage(image.getImage());
    }

    private void setUpPieces() {
        int startX = 130;
        int startY = 25;
        int marbleIndexStart = 5;
        int marbleIndexEnd = 9;
        for (char row = 'i'; row >= 'a'; row--) {
            int xPos = startX;
            int yPos = startY;
            for (int col = marbleIndexStart; col <= marbleIndexEnd; col++) {
                String position = "" + row + col;
                GUIPiece piece = new GUIPiece(new GUIPieceIcon(position, EMPTY_COLOR), position);
                _pieces.put(position, piece);
                piece.addMouseListener(this);
                piece.setBounds(xPos, yPos, 40, 40);
                this.add(piece);
                xPos += 45.75;
            }
            if (row > 'e') {
                startX -= 23;
                marbleIndexStart--;
            } else {
                startX += 23;
                marbleIndexEnd--;
            }
            startY += 46;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GUIPiece piece = (GUIPiece) e.getSource();
        if (_game.getCurrentTurn() == _game.getBoard().getPiece(piece.getPosition())) {
            if (_marbleString.isEmpty()) {
                piece.setColor(HIGHLIGHT_COLOR);
                _marbleString.add(piece.getPosition());
            } else if (_game.getBoard().getReachableCells().containsKey(_game.getBoard().toIndex(piece.getPosition()))
                    && _game.getBoard().getReachableCells().get(_game.getBoard().toIndex(piece.getPosition())).contains(_game.getBoard().toIndex(_marbleString.get(0)))) {
                piece.setColor(HIGHLIGHT_COLOR);
                _marbleString.add(piece.getPosition());
            }
        }
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



    public void updateBoard() {
        for (char row = 'a'; row <= 'i'; row++) {
            for (int col = 1; col <= 9; col++) {
                String position = "" + row + col;
                if (_pieces.containsKey(position)) {
                    Pieces piece = _game.getBoard().getPiece(position);
                    switch (piece) {
                        case BLACK -> _pieces.get(position).setColor(BLACK_COLOR);
                        case WHITE -> _pieces.get(position).setColor(WHITE_COLOR);
                        case EMPTY -> _pieces.get(position).setColor(EMPTY_COLOR);
                    }
                }
            }
        }
    }

    public void updateGame(Game game) {
        _game = game;
    }

    private Game _game;

    private TreeMap<String, GUIPiece> _pieces;

    private LinkedList<String> _marbleString;

    private Color WHITE_COLOR = new Color(230, 230, 240);
    private Color BLACK_COLOR = new Color(39,44,47);
    private Color EMPTY_COLOR = new Color(210, 180, 140);
    private Color BACKGROUND_COLOR = new Color(141, 115, 93);
    private Color HIGHLIGHT_COLOR = new Color(150,150,150);

}
