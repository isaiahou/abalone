import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that contains all the information regarding the game's board.
 *  @author Isaiah Ou
 */

public class Board implements Serializable {
    Board(String setting) {
        defaultSet(setting);
    }

    Board(Board board0) {
        copySet(board0);
    }

    private void defaultSet(String setting) {
        if (setting.equals("default")) {
            _linearizedArray = new Pieces[121];
            Arrays.fill(_linearizedArray, Pieces.EMPTY);
            setupRails();
            setupPieces();
            setupAdjacentCells();
            setupReachableCells();
        }
    }

    private void copySet(Board board0){
        _linearizedArray = board0.getLinearizedArray().clone();
        _adjacentCells = board0.getAdjacentCells();
        _killedBlack = board0.getKilledBlack();
        _killedWhite = board0.getKilledWhite();
    }

    private void setupRails() {
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

    private void setupAdjacentCells() {
        _adjacentCells = new int[121][6];
        for (int i = 0; i < _linearizedArray.length; i++) {
            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0 -> _adjacentCells[i][j] = i + 11;
                    case 1 -> _adjacentCells[i][j] = i + 12;
                    case 2 -> _adjacentCells[i][j] = i + 1;
                    case 3 -> _adjacentCells[i][j] = i - 11;
                    case 4 -> _adjacentCells[i][j] = i - 12;
                    case 5 -> _adjacentCells[i][j] = i - 1;
                }
            }
        }
    }

    private void setupReachableCells() {
        _reachableCells = new HashMap<>();
        for (int i = 0; i < _linearizedArray.length; i++) {
            HashSet<Integer> tmp = new HashSet<>();
            for (int j = 0; j < 6; j++) {
                for (int k = 1; k <= 2; k ++) {
                    switch (j) {
                        case 0 -> tmp.add(i + 11 * k);
                        case 1 -> tmp.add(i + 12 * k);
                        case 2 -> tmp.add(i + k);
                        case 3 -> tmp.add(i - 11 * k);
                        case 4 -> tmp.add(i - 12 * k);
                        case 5 -> tmp.add(i - k);
                    }
                }
            }
            _reachableCells.put(i, tmp);
        }
    }

    public void displayBoard() {
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
                int posIndex = toIndexBottomRow(pos);
                Pieces piece = _linearizedArray[posIndex];
                if (piece == Pieces.WHITE) {
                    System.out.print(" W ");
                } else if (piece == Pieces.BLACK) {
                    System.out.print(" B ");
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
                rowStringBuilder.insert(0, " W ");
            } else if (piece == Pieces.BLACK) {
                rowStringBuilder.insert(0, " B ");
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

    public int toIndex(String position) {
        Pattern positionString = Pattern.compile("[a-i][1-9]");
        Matcher positionMatcher = positionString.matcher(position);
        if (positionMatcher.matches()) {
            return toIndexBottomRow(position);
        } else {
            System.out.println("Invalid index.");
            return 0;
        }
    }

    public int toIndexBottomRow(String position) {
        position = position.toLowerCase();
        int row = position.charAt(0);
        int column = position.charAt(1);
        int lowerCase = 96;
        int number = 48;
        return (row - lowerCase) * 11 + (column - number);
    }

    public String indexToString(int positionIndex) {
        char row = ((char) (96 + positionIndex / 11));
        int col = positionIndex % 11;
        return "" + row + col;
    }

    public void updateBoard(int linearizedPosition, Pieces piece) {
        _linearizedArray[linearizedPosition] = piece;
    }

    public Pieces getPiece(String pos) {
        return _linearizedArray[toIndex(pos)];
    }

    public Pieces getPiece(int linearizedPos) {
        return _linearizedArray[linearizedPos];
    }

    public void incrementKilled(Pieces piece) {
        if (piece == Pieces.WHITE) {
            _killedWhite++;
        } else {
            _killedBlack++;
        }
    }

    public Pieces[] getLinearizedArray() { return _linearizedArray; }

    public int getKilledWhite() { return _killedWhite; }

    public int getKilledBlack() { return _killedBlack; }

    public int[][] getAdjacentCells() { return _adjacentCells; }

    public HashMap<Integer, HashSet<Integer>> getReachableCells() { return _reachableCells; }

    /** Linearized array of pieces representing the board. */
    private Pieces[] _linearizedArray;

    /** Integer tracking the number of killed white pieces. */
    private int _killedWhite = 0;

    /** Integer tracking the number of killed black pieces. */
    private int _killedBlack = 0;

    /** 2D Array containing adjacency information for each piece. */
    private int[][] _adjacentCells;

    /** 2D Array containing reachable marbles to make a string. */
    private HashMap<Integer, HashSet<Integer>> _reachableCells;

}
