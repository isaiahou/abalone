import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that represents an instance of a move in the game.
 *  @author Isaiah Ou
 */

public class Move {

    Move(String positionSpecification, Pieces whoseTurn, Board board, boolean AI) {
        Pattern singleMove = Pattern.compile("[a-i][1-9],[a-i][1-9]");
        Pattern multiMove = Pattern.compile("[a-i][1-9]-[a-i][1-9],[a-i][1-9]");
        Matcher singleMoveMatch = singleMove.matcher(positionSpecification);
        Matcher multiMoveMatch = multiMove.matcher(positionSpecification);
        if (!singleMoveMatch.matches() && !multiMoveMatch.matches()) {
            System.out.println("Invalid move: please follow the correct syntax for moves.");
            _validMove = false;
            return;
        }
        _board = board;
        _marbleString = new LinkedList<>();
        _opponentMarbleString = new LinkedList<>();
        String firstMarble = positionSpecification.substring(0, 2);
        _currentTurn = whoseTurn;
        _currentColor = _board.getPiece(firstMarble);
        _opponentColor = oppositeColor(_currentColor);
        _linearizedFirst = _board.toIndex(firstMarble);
        String destinationMarble;
        if (positionSpecification.charAt(2) == '-') {
            String secondMarble = positionSpecification.substring(3, 5);
            _linearizedSecond = _board.toIndex(secondMarble);
            createMarbleString();
            destinationMarble = positionSpecification.substring(6);
        } else {
            _marbleString.add(_board.toIndex(firstMarble));
            _marbleInLine = true;
            destinationMarble = positionSpecification.substring(3, 5);
        }
        _linearizedDestination = _board.toIndex(destinationMarble);
        if (AI) {
            setValidMoveAI();
        } else {
            setValidMove();
        }
    }

    private void setValidMoveAI() {
        if (validateTurnAI()
                && validateLineAI()
                && validateDestinationAI()
                && validateObstaclesAI()
                && _marbleInLine
                && !_unreasonableMove) {
            _validMove = true;
        }
    }

    private void setValidMove() {
        if (validateTurn()
                && validateLine()
                && validateDestination()
                && validateObstacles()
                && _marbleInLine) {
            _validMove = true;
        }
    }

    private void createMarbleString() {
        for (int i = 0; i < 6; i++) {
            int pointerMarblePosition = _linearizedFirst;
            for (int j = 0; j < 26; j++) {
                if (pointerMarblePosition >= 0 && pointerMarblePosition < 121) {
                    if (_board.getPiece(pointerMarblePosition) == _currentColor) {
                        _marbleString.add(pointerMarblePosition);
                        if (_linearizedSecond == pointerMarblePosition) {
                            _marbleInLine = true;
                            _marbleDirection = i;
                            return;
                        } else {
                            pointerMarblePosition = _board.getAdjacentCells()[pointerMarblePosition][i];
                        }
                    }
                }
            }
            _marbleString.clear();
        }
    }

    private boolean validateTurn() {
        if (_currentColor == _currentTurn) {
            return true;
        }
        else if (_currentColor == oppositeColor(_currentTurn)) {
            System.out.println("Invalid move: you cannot move your opponent's marbles.");
            return false;
        } else {
            System.out.println("Invalid move: please select one of your marbles.");
            return false;
        }
    }

    private boolean validateLine() {
        if (!_marbleInLine) {
            System.out.println("Invalid move: marble not in a straight, connected line.");
            return false;
        }
        if (_marbleString.size() > 3) {
            System.out.println("Invalid move: marble line is too long (max 3).");
            return false;
        }
        return true;
    }

    private boolean validateDestination() {
        if (_linearizedFirst == _linearizedDestination || _linearizedSecond == _linearizedDestination) {
            System.out.println("Invalid move: no marbles were moved.");
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjacentCells()[_linearizedFirst][i] == _linearizedDestination) {
                _direction = i;
                return true;
            }
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjacentCells()[_linearizedSecond][i] == _linearizedDestination) {
                _direction = i;
                return true;
            }
        }
        System.out.println("Invalid move: destination not reachable by selected marbles.");
        return false;
    }

    private boolean validateObstacles() {
        for (int pos: _marbleString) {
            int direction = _direction;
            int newPos = _board.getAdjacentCells()[pos][direction];
            if (_board.getPiece(newPos) != Pieces.EMPTY) {
                if (_board.getPiece(newPos) == _opponentColor) {
                    if (_direction != _marbleDirection
                        && ((_direction + 3) % 6) != _marbleDirection) {
                        System.out.println("Invalid move: cannot push marbles with side step.");
                        return false;
                    }
                    _pushOpponent = true;
                    _opponentMarbleString.add(newPos);
                    int opponentPointer =_board.getAdjacentCells()[newPos][direction];
                    for (int i = 1; i < 3; i++) {
                        Pieces opponentPointerPiece = _board.getPiece(opponentPointer);
                        if (opponentPointerPiece == _opponentColor) {
                            _opponentMarbleString.add(opponentPointer);
                            opponentPointer = _board.getAdjacentCells()[opponentPointer][direction];
                        } else if (opponentPointerPiece == _currentColor) {
                            System.out.println("Invalid move: one of your own marbles is in the way.");
                            return false;
                        } else {
                            break;
                        }
                    }
                    if (_opponentMarbleString.size() >= 3 || _opponentMarbleString.size() >= _marbleString.size()) {
                        System.out.println("Invalid move: cannot push opponent without sumito advantage.");
                        return false;
                    }
                } else if (_board.getPiece(newPos) == _currentColor
                        && !(_marbleString.contains(newPos))) {
                    System.out.println("Invalid move: one of your own marbles is in the way.");
                    return false;
                } else if (_board.getPiece(newPos) == Pieces.RAIL) {
                    _unreasonableMove = true;
                }
            }
        }
        return true;
    }

    private boolean validateTurnAI() {
        if (_currentColor == _currentTurn) {
            return true;
        }
        else if (_currentColor == oppositeColor(_currentTurn)) {
            return false;
        } else {
            return false;
        }
    }

    private boolean validateLineAI() {
        if (!_marbleInLine) {
            return false;
        }
        return _marbleString.size() <= 3;
    }

    private boolean validateDestinationAI() {
        if (_linearizedFirst == _linearizedDestination || _linearizedSecond == _linearizedDestination) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjacentCells()[_linearizedSecond][i] == _linearizedDestination) {
                _direction = i;
                return true;
            }
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjacentCells()[_linearizedFirst][i] == _linearizedDestination) {
                _direction = i;
                return true;
            }
        }
        return false;
    }

    private boolean validateObstaclesAI() {
        for (int pos: _marbleString) {
            int direction = _direction;
            int newPos = _board.getAdjacentCells()[pos][direction];
            if (_board.getPiece(newPos) != Pieces.EMPTY) {
                if (_board.getPiece(newPos) == _opponentColor) {
                    if (_direction != _marbleDirection
                            && ((_direction + 3) % 6) != _marbleDirection) {
                        return false;
                    }
                    _pushOpponent = true;
                    _opponentMarbleString.add(newPos);
                    int opponentPointer =_board.getAdjacentCells()[newPos][direction];
                    for (int i = 1; i < 3; i++) {
                        Pieces opponentPointerPiece = _board.getPiece(opponentPointer);
                        if (opponentPointerPiece == _opponentColor) {
                            _opponentMarbleString.add(opponentPointer);
                            opponentPointer = _board.getAdjacentCells()[opponentPointer][direction];
                        } else if (opponentPointerPiece == _currentColor) {
                            return false;
                        } else {
                            break;
                        }
                    }
                    if (_opponentMarbleString.size() >= 3 || _opponentMarbleString.size() >= _marbleString.size()) {
                        return false;
                    }
                } else if (_board.getPiece(newPos) == _currentColor
                        && !(_marbleString.contains(newPos))) {
                    return false;
                } else if (_board.getPiece(newPos) == Pieces.RAIL) {
                    _unreasonableMove = true;
                }
            }
        }
        return true;
    }

    public boolean isValidMove() { return _validMove; }

    private Pieces oppositeColor(Pieces color) {
        if (color == Pieces.WHITE) {
            return Pieces.BLACK;
        }
        return Pieces.WHITE;
    }

    public LinkedList<Integer> getMarbleString() {
        return _marbleString;
    }

    public int getDirection() {
        return _direction;
    }

    public boolean getPushOpponent() { return _pushOpponent; }

    public LinkedList<Integer> getOpponentMarbleString() { return _opponentMarbleString; }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Move m)) {
            return false;
        }
        return this.getDirection() == m.getDirection()
                && this.getMarbleString().size() == m.getMarbleString().size()
                && this.getMarbleString().containsAll(m.getMarbleString());
    }

    /** Integer representing the index of the first marble. */
    private int _linearizedFirst;

    /** Integer representing the index of the second marble. */
    private int _linearizedSecond;

    /** Integer representing the index of the destination marble. */
    private int _linearizedDestination;

    /** Board instance of the move executed on. */
    private Board _board;

    /** Linked list containing all indices of the marbles in the marble string. */
    private LinkedList<Integer> _marbleString;

    /** Linked list containing all indices of the marbles in the
     * marble string of the opponent if pushed. */
    private LinkedList<Integer> _opponentMarbleString;

    /** Direction of move indexed with the top left corner as 0. */
    private int _direction;

    /** Direction of marble alignment indexed with the top left corner as 0. */
    private int _marbleDirection;

    /** Indication of the move's color. */
    private Pieces _currentColor;

    /** Indication of the move's opponent color. */
    private Pieces _opponentColor;

    /** Indication of the current turn of the board instance. */
    private Pieces _currentTurn;

    /** Boolean value representing the validity of a move. */
    private boolean _validMove = false;

    /** Boolean value indicating whether an opponent was pushed. */
    private boolean _pushOpponent = false;

    /** Boolean value indicating whether the marbles are in line. */
    private boolean _marbleInLine = false;

    /** Boolean value indicating whether the move involves suicide. */
    private boolean _unreasonableMove = false;

}
