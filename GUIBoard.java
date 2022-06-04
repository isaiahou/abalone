import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/** The main JFrame component of the GUI board.
 *  Represents the current the state of the game and actively
 *  updates the board as moves are made.
 *  @author Isaiah Ou
 */

public class GUIBoard extends JFrame implements Serializable, MouseListener {

    public GUIBoard(Game game, int numPlayers) {
        _game = game;
        _pieces = new HashMap<>();
        _numPlayers = numPlayers;
        _marbleString = new LinkedList<>();
        _newPositions = new HashSet<>();

        this.setTitle("Abalone Board Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(null);
        this.getContentPane().setBackground(BACKGROUND_COLOR);

        setUpPieces();
        setUpCounter();
        updateBoard();

        this.setVisible(true);
        this.setResizable(false);

        ImageIcon image = new ImageIcon("IconTransparent.png");
        this.setIconImage(image.getImage());
    }

    private void setUpPieces() {
        int startX = 133;
        int startY = 25;
        int marbleIndexStart = 5;
        int marbleIndexEnd = 9;
        for (char row = 'i'; row >= 'a'; row--) {
            int xPos = startX;
            int yPos = startY;
            for (int col = marbleIndexStart; col <= marbleIndexEnd; col++) {
                String position = "" + row + col;
                GUIPiece piece = new GUIPiece(new GUIPieceIcon(EMPTY_COLOR), position);
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

    private void setUpCounter() {
        _whiteCounter = new JLabel();
        _whiteCounter.setText("White Dead: " + _game.getBoard().getKilledWhite() + "/6");
        _whiteCounter.setFont(new Font("Verdana", Font.BOLD, 14));
        _whiteCounter.setBounds(5,-65,150,150);
        this.add(_whiteCounter);
        _blackCounter = new JLabel();
        _blackCounter.setText("Black Dead: " + _game.getBoard().getKilledBlack() + "/6");
        _blackCounter.setFont(new Font("Verdana", Font.BOLD, 14));
        _blackCounter.setBounds(5,-50,150,150);
        this.add(_blackCounter);
        _whoseTurn = new JLabel();
        _whoseTurn.setText("Turn: " + _game.getCurrentTurnString());
        _whoseTurn.setFont(new Font("Verdana", Font.BOLD, 14));
        _whoseTurn.setBounds(5, -35, 150, 150);
        this.add(_whoseTurn);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (_numPlayers == 0) {
            return;
        }
        if (_numPlayers == 1 && _game.getCurrentTurn() != Pieces.BLACK) {
            return;
        }
        GUIPiece piece = (GUIPiece) e.getSource();
        String piecePosition = piece.getPosition();
        if (_game.getCurrentTurn() == _game.getBoard().getPiece(piecePosition)) {
            if (_marbleString.isEmpty()) {
                piece.setColor(HIGHLIGHT_COLOR);
                _marbleString.add(piecePosition);
            } else if (hasReachableCells(piecePosition) && marbleStringCanReachCell(piecePosition)) {
                if (_marbleString.size() == 1) {
                    piece.setColor(HIGHLIGHT_COLOR);
                    _marbleString.add(piecePosition);
                    int firstMarbleIndex = _game.getBoard().toIndex(_marbleString.getFirst());
                    int lastMarbleIndex = _game.getBoard().toIndex(_marbleString.getLast());
                    createMarbleString(firstMarbleIndex, lastMarbleIndex);
                    for (String marble : _marbleString) {
                        GUIPiece stringPiece = _pieces.get(marble);
                        stringPiece.setColor(HIGHLIGHT_COLOR);
                    }
                } else if (inMarbleStringDirection(piecePosition) || inReverseMarbleStringDirection(piecePosition)) {
                    piece.setColor(HIGHLIGHT_COLOR);
                    _marbleString.add(piecePosition);
                    Collections.sort(_marbleString);
                } else {
                    clearHighlight();
                }
            } else {
                clearHighlight();
            }
        } else if (piece == _potentialMovePiece) {
            try {
                _game.executeMove(_potentialMove);
                _potentialMove = null;
                _potentialMovePiece = null;
                _marbleString.clear();
                updateBoard();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            clearHighlight();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        if (_numPlayers == 0) {
            return;
        }
        if (_numPlayers == 1 && _game.getCurrentTurn() != Pieces.BLACK) {
            return;
        }
        GUIPiece piece = (GUIPiece) e.getSource();
        String piecePosition = piece.getPosition();
        int pieceIndex = _game.getBoard().toIndex(piecePosition);
        String moveString;
        if (!_marbleString.isEmpty()
                && adjacentCells(_game.getBoard().toIndex(_marbleString.getFirst())).contains(pieceIndex)) {
            if (_game.getCurrentTurn() != _game.getBoard().getPiece(piecePosition)) {
                if (_marbleString.size() == 1) {
                    moveString = _marbleString.getFirst() + "," + piecePosition;
                } else {
                    moveString = _marbleString.getFirst() + "-" + _marbleString.getLast() + "," + piecePosition;
                }
                Move move = new Move(moveString, _game.getCurrentTurn(), _game.getBoard(), true);
                if (move.isValidMove()) {
                    markNewPositions(move);
                    _potentialMove = move;
                    _potentialMovePiece = piece;
                }
            }
        } else if (!_marbleString.isEmpty()
                && adjacentCells(_game.getBoard().toIndex(_marbleString.getLast())).contains(pieceIndex)) {
            if (_game.getCurrentTurn() != _game.getBoard().getPiece(piecePosition)) {
                if (_marbleString.size() == 1) {
                    moveString = _marbleString.getLast() + "," + piecePosition;
                } else {
                    moveString = _marbleString.getFirst() + "-" + _marbleString.getLast() + "," + piecePosition;
                }
                Move move = new Move(moveString, _game.getCurrentTurn(), _game.getBoard(), true);
                if (move.isValidMove()) {
                    markNewPositions(move);
                    _potentialMove = move;
                    _potentialMovePiece = piece;
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!_newPositions.isEmpty()) {
            removeNewPositions();
            _potentialMove = null;
            _potentialMovePiece = null;
        }
    }

    public void updateBoard() {
        _whiteCounter.setText("White Dead: " + _game.getBoard().getKilledWhite() + "/6");
        _blackCounter.setText("Black Dead: " + _game.getBoard().getKilledBlack() + "/6");
        _whoseTurn.setText("Turn: " + _game.getCurrentTurnString());
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
        if (_game.hasWinner()) {
            String message = "The winner is: " + _game.getWinnerString();
            JOptionPane.showMessageDialog(this, message);
        }
    }

    public void updateGame(Game game) {
        _game = game;
    }

    private void markNewPositions(Move move) {
        int direction = move.getDirection();
        for (int pos: move.getMarbleString()) {
            int newPos = _game.getBoard().getAdjacentCells()[pos][direction];
            _newPositions.add(newPos);
        }
        for (Integer newPos: _newPositions) {
            String newPosString = _game.getBoard().indexToString(newPos);
            GUIPiece piece = _pieces.get(newPosString);
            piece.setColor(MOVE_COLOR);
        }
    }

    private void removeNewPositions() {
        for (Integer newPos: _newPositions) {
            String newPosString = _game.getBoard().indexToString(newPos);
            GUIPiece piece = _pieces.get(newPosString);
            Color color = colorOf(_game.getBoard().getPiece(newPosString));
            piece.setColor(color);
        }
        for (String highlightedPos: _marbleString) {
            GUIPiece piece = _pieces.get(highlightedPos);
            piece.setColor(HIGHLIGHT_COLOR);
        }
        _newPositions.clear();
    }

    private HashSet<Integer> adjacentCells(int marbleIndex) {
        HashSet<Integer> result = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            result.add(_game.getBoard().getAdjacentCells()[marbleIndex][i]);
        }
        return result;
    }

    private boolean hasReachableCells(String piecePosition) {
        int indexPosition = _game.getBoard().toIndex(piecePosition);
        return _game.getBoard().getReachableCells(indexPosition) != null;
    }

    private boolean marbleStringCanReachCell(String piecePosition) {
        int indexPosition = _game.getBoard().toIndex(piecePosition);
        int firstMarblePosition = _game.getBoard().toIndex(_marbleString.getFirst());
        boolean firstCheck = _game.getBoard().getReachableCells(indexPosition).contains(firstMarblePosition);
        if (!firstCheck) {
            return false;
        } else {
            int direction = 7;
            for (int i = 0; i < 6; i++) {
                if (_game.getBoard().getReachableCellsDirection().get(firstMarblePosition)
                        .get(i).contains(indexPosition)) {
                    direction = i;
                }
            }
            int pointerMarble = _game.getBoard().getAdjacentCells()[firstMarblePosition][direction];
            return _game.getCurrentTurn() == _game.getBoard().getPiece(pointerMarble);
        }
    }

    private boolean inMarbleStringDirection(String piecePosition) {
        int indexPosition = _game.getBoard().toIndex(piecePosition);
        int firstMarblePosition = _game.getBoard().toIndex(_marbleString.getFirst());
        return _game.getBoard().getReachableCellsDirection().get(firstMarblePosition)
                .get(_direction).contains(indexPosition);
    }

    private boolean inReverseMarbleStringDirection(String piecePosition) {
        int indexPosition = _game.getBoard().toIndex(piecePosition);
        int firstMarblePosition = _game.getBoard().toIndex(_marbleString.getFirst());
        return _game.getBoard().getReachableCellsDirection().get(firstMarblePosition)
                .get((_direction + 3) % 6).contains(indexPosition);
    }

    private void createMarbleString(int linearizedFirst, int linearizedSecond) {
        _marbleString.clear();
        for (int i = 0; i < 6; i++) {
            int pointerMarblePosition = linearizedFirst;
            for (int j = 0; j < 3; j++) {
                if (pointerMarblePosition >= 0 && pointerMarblePosition < 121) {
                    _marbleString.add(_game.getBoard().indexToString(pointerMarblePosition));
                    if (linearizedSecond == pointerMarblePosition) {
                        _direction = i;
                        return;
                    } else {
                        pointerMarblePosition = _game.getBoard().getAdjacentCells()[pointerMarblePosition][i];
                    }
                }
            }
        _marbleString.clear();
        }
    }

    private void clearHighlight() {
        for (String marble: _marbleString) {
            GUIPiece stringPiece = _pieces.get(marble);
            stringPiece.setColor(colorOf(_game.getCurrentTurn()));
        }
        _marbleString.clear();
    }

    private Color colorOf(Pieces color) {
        return switch (color) {
            case BLACK -> BLACK_COLOR;
            case WHITE -> WHITE_COLOR;
            case EMPTY -> EMPTY_COLOR;
            case RAIL, FILLER -> null;
        };
    }

    /** Tracks the game instance of the current game. */
    private Game _game;

    /** Maps the string representation of each piece to its GUI . */
    private final HashMap<String, GUIPiece> _pieces;

    /** Indicates the number of real manual players. */
    private final int _numPlayers;

    /** Indicates the number of dead white pieces on the GUI. */
    private JLabel _whiteCounter;

    /** Indicates the number of dead black pieces on the GUI. */
    private JLabel _blackCounter;

    /** Indicates the color of the current turn on the GUI. */
    private JLabel _whoseTurn;

    /** Holds the strings of the marbles currently selected. */
    private final LinkedList<String> _marbleString;

    /** Tracks the direction of the marbles in the marble string */
    private int _direction;

    /** Determines the new positions of marbles after hypothetically moving. */
    private final HashSet<Integer> _newPositions;

    /** Move object representing a selected and hovered move. */
    private Move _potentialMove;

    /** Holds the destination piece of the hovered move. */
    private GUIPiece _potentialMovePiece;

    /** The color of white marbles. */
    private final Color WHITE_COLOR = new Color(230, 230, 240);

    /** The color of black marbles. */
    private final Color BLACK_COLOR = new Color(39,44,47);

    /** The color of an empty spot. */
    private final Color EMPTY_COLOR = new Color(210, 180, 140);

    /** The color of the highlight on selected marbles. */
    private final Color HIGHLIGHT_COLOR = new Color(150,150,150);

    /** The color of hypothetical marbles on a hovered move. */
    private final Color MOVE_COLOR = new Color (255, 160, 160);

    /** The color of the background of the GUI. */
    private final Color BACKGROUND_COLOR = new Color(141, 115, 93);

}
