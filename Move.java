import java.util.LinkedList;

public class Move {
// later use regex for input validation

    Move(String positionSpecification, Board board) {
        _board = board;
        _marbleString = new LinkedList<>();
        _opponentMarbleString = new LinkedList<>();
        _firstMarble = positionSpecification.substring(0, 2);
        _currentColor = _board.getPiece(_firstMarble);
        _opponentColor = oppositeColor(_currentColor);
        _linearizedFirst = _board.toIndex(_firstMarble);
        if (positionSpecification.charAt(2) == '-') {
            _secondMarble = positionSpecification.substring(3, 5);
            _linearizedSecond = _board.toIndex(_secondMarble);
            createMarbleString(_firstMarble, _secondMarble);
            _destinationMarble = positionSpecification.substring(6);
            _linearizedDestination = _board.toIndex(_destinationMarble);
        } else {
            _marbleString.add(_board.toIndex(_firstMarble));
            _destinationMarble = positionSpecification.substring(3, 5);
            _linearizedDestination = _board.toIndex(_destinationMarble);
        }
        if (validateLineSize()
                && validateDestination()
                && validateObstacles()) {
            _validMove = true;
        }
    }

    Pieces oppositeColor(Pieces color) {
        if (color == Pieces.WHITE) {
            return Pieces.BLACK;
        }
        return Pieces.WHITE;
    }

    void createMarbleString(String firstMarble, String secondMarble) {
        for (int i = 0; i < 6; i++) {
            int pointerMarblePosition = _linearizedFirst;
            for (int j = 0; j < 3; j++) {
                if (_board.getPiece(pointerMarblePosition) == _currentColor) {
                    _marbleString.add(pointerMarblePosition);
                    if (_linearizedSecond == pointerMarblePosition) {
                        return;
                    } else {
                        pointerMarblePosition = _board.getAdjencentCells()[pointerMarblePosition][i];
                    }
                }
            }
            _marbleString.clear();
        }
        System.out.println("Invalid move: marble not in a line.");
    }

    boolean validateLineSize() {
        if (_marbleString.size() > 3) {
            System.out.println("Invalid move: marble line is too long (max 3).");
            return false;
        }
        return true;
    }

    boolean validateDestination() {
        if (_linearizedFirst == _linearizedDestination || _linearizedSecond == _linearizedDestination) {
            System.out.println("Invalid move: no marbles were moved.");
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjencentCells()[_linearizedFirst][i] == _linearizedDestination) {
                _commandFormat = "first";
                _direction = i;
                return true;
            }
        }
        for (int i = 0; i < 6; i++) {
            if (_board.getAdjencentCells()[_linearizedSecond][i] == _linearizedDestination) {
                _commandFormat = "second";
                _direction = i;
                return true;
            }
        }
        System.out.println("Invalid move: destination not reachable by selected marbles.");
        return false;
    }

    boolean validateObstacles() {
        for (int pos: _marbleString) {
            int direction = _direction;
            int newPos = _board.getAdjencentCells()[pos][direction];
            if (_board.getPiece(newPos) != Pieces.EMPTY) {
                if (_board.getPiece(newPos) == _opponentColor) {
                    _pushOpponent = true;
                    _opponentMarbleString.add(newPos);
                    int opponentPointer =_board.getAdjencentCells()[newPos][direction];
                    for (int i = 1; i < 3; i++) {
                        Pieces opponentPointerPiece = _board.getPiece(opponentPointer);
                        if (opponentPointerPiece == _opponentColor) {
                            _opponentMarbleString.add(opponentPointer);
                            opponentPointer = _board.getAdjencentCells()[opponentPointer][direction];
                        } else if (opponentPointerPiece == _currentColor) {
                            System.out.println("Invalid move: one of your own marbles is in the way.");
                            return false;
                        } else {
                            break;
                        }
                    }
                    if (_opponentMarbleString.size() >= 3 || _opponentMarbleString.size() > _marbleString.size()) {
                        System.out.println("Invalid move: cannot push opponent without sumito advantage.");
                        return false;
                    }
                } else if (_board.getPiece(newPos) == _currentColor
                        && !(_marbleString.contains(newPos))) {
                    System.out.println("Invalid move: one of your own marbles is in the way.");
                    return false;
                }
            }
        }
        return true;
    }

    public int getLinearizedFirst() {
        return _linearizedFirst;
    }

    public int getLinearizedSecond() {
        return _linearizedSecond;
    }

    public LinkedList<Integer> getMarbleString() {
        return _marbleString;
    }

    public boolean isValidMove() {
        return _validMove;
    }

    public int getDirection() {
        return _direction;
    }

    public boolean getPushOpponent() { return _pushOpponent; }

    public Pieces getCurrentColor() { return _currentColor; }

    public LinkedList<Integer> getOpponentMarbleString() { return _opponentMarbleString; }

    public String getCommandFormat() {
        return _commandFormat;
    }


    private String _firstMarble;
    private int _linearizedFirst;
    private String _secondMarble;
    private int _linearizedSecond;
    private Board _board;
    private String _destinationMarble;
    private int _linearizedDestination;
    private LinkedList<Integer> _marbleString;
    private int _direction;
    private String _commandFormat;
    private Pieces _currentColor;
    private Pieces _opponentColor;
    private LinkedList<Integer> _opponentMarbleString;
    private boolean _validMove = false;
    private boolean _pushOpponent = false;
}
