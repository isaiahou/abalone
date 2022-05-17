import java.util.Arrays;
import java.util.Set;

public class Board {
    Board(String setting) {
        defaultSet();
    }

    private void defaultSet() {
        _linearizedArray = new Pieces[121];
        Arrays.fill(_linearizedArray, Pieces.EMPTY);
        setupRails();
        setupPieces();
        setupAdjecentCells();
    }

    void setupRails() {
        int[] railSpaces = { 0, 1, 2, 3, 4, 5,
                17, 29, 41, 53, 65, 76, 87, 98, 109,
                11, 22, 33, 44, 55, 67, 79, 91, 103,
                115, 116, 117, 118, 119, 120 };
        for (int pos : railSpaces) {
            _linearizedArray[pos] = Pieces.RAIL;
        }
        int[] fillerSpaces = { 110, 111, 112, 113, 114,
                                99, 100, 101, 102, 88, 89, 90, 77, 78, 66,
        54, 42, 43, 30, 31, 32, 18, 19, 20, 21, 6, 7, 8, 9, 10 };
        for (int pos : fillerSpaces) {
            _linearizedArray[pos] = Pieces.FILLER;
        }
    }

    private void setupPieces() {
        int[] whitePieces = { 104, 105, 106, 107, 108,
                                92, 93, 94, 95, 96, 97,
                                    82, 83, 84 };
        int[] blackPieces = { 36, 37, 38,
                        23, 24, 25, 26, 27, 28,
                        12, 13, 14, 15, 16 };
        for (int white : whitePieces) {
            _linearizedArray[white] = Pieces.WHITE;
        }
        for (int black : blackPieces) {
            _linearizedArray[black] = Pieces.BLACK;
        }
    }

    void setupAdjecentCells() {
        _adjencentCells = new int[121][6];
        for (int i = 0; i < _linearizedArray.length; i++) {
            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0 -> _adjencentCells[i][j] = i + 11;
                    case 1 -> _adjencentCells[i][j] = i + 12;
                    case 2 -> _adjencentCells[i][j] = i + 1;
                    case 3 -> _adjencentCells[i][j] = i - 11;
                    case 4 -> _adjencentCells[i][j] = i - 12;
                    case 5 -> _adjencentCells[i][j] = i - 1;
                }
            }
        }
    }

    void displayBoard() {
        int indent = 0;
        Set<Integer> rowIndices = Set.of(103, 91, 79, 67, 55, 44, 33, 22, 11);
        Set<Integer> columnIndices = Set.of(1, 2, 3, 4, 5, 17, 29, 41, 53);
        for (char row = 'i'; row >= 'a' - 1; row--) {
            String str = " ";
            String repeated = str.repeat(indent);
            indent += 2;
            System.out.print(repeated);
            for (int column = 0; column <= 9; column++) {
                String pos = "" + row + column;
                int posIndex = toIndex(pos);
                Pieces piece = _linearizedArray[posIndex];
                if (piece == Pieces.WHITE) {
                    System.out.print(" O ");
                } else if (piece == Pieces.BLACK) {
                    System.out.print(" X ");
                } else if (piece == Pieces.EMPTY) {
                    System.out.print(" - ");
                } else if (piece == Pieces.FILLER) {
                    System.out.print("   ");
                } else if (piece == Pieces.RAIL) {
                    if (rowIndices.contains(posIndex)) {
                        String rowString = " " + row + " ";
                        System.out.print(rowString.toUpperCase());
                    } else if (columnIndices.contains(posIndex)) {
                        System.out.print(" " + column + " ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        int spacer = 0;
        StringBuilder result = new StringBuilder();
        StringBuilder rowStringBuilder = new StringBuilder();
        for (int i = _linearizedArray.length - 1; i >= 0; i--) {
            Pieces piece = _linearizedArray[i];
            if (piece == Pieces.WHITE) {
                rowStringBuilder.insert(0, " O ");
            } else if (piece == Pieces.BLACK) {
                rowStringBuilder.insert(0, " @ ");
            } else if (piece == Pieces.EMPTY) {
                rowStringBuilder.insert(0, " + ");
            } else if (piece == Pieces.RAIL) {
                rowStringBuilder.insert(0, " X ");
            } else if (piece == Pieces.FILLER) {
                rowStringBuilder.insert(0, " - ");
            }
            if (i % 11 == 0) {
                String str = " ";
                String repeated = str.repeat(spacer);
                spacer+=2;
                result.append(repeated);
                result.append(rowStringBuilder);
                result.append("\n");
                rowStringBuilder.setLength(0);
            }
        }
        return result.toString();
    }

    int toIndex(String position) {
        position = position.toLowerCase();
        int row = position.charAt(0);
        int column = position.charAt(1);
        int lowerCase = 96;
        int number = 48;
        return (row - lowerCase) * 11 + (column - number);
    }

    void updateBoard(int linearizedPosition, Pieces piece) {
        _linearizedArray[linearizedPosition] = piece;
    }

    public Pieces getPiece(String pos) {
        return _linearizedArray[toIndex(pos)];
    }

    public Pieces getPiece(int linearizedPos) {
        return _linearizedArray[linearizedPos];
    }

    public int getKilledWhite() {
        return _killedWhite;
    }

    public int getKilledBlack() {
        return _killedBlack;
    }

    public int[][] getAdjencentCells() {
        return _adjencentCells;
    }

    private Pieces[] _linearizedArray;
    private int _killedWhite = 0;
    private int _killedBlack = 0;
    private int[][] _adjencentCells;
}
