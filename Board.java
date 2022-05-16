import java.util.Arrays;

public class Board {
    Board(String setting) {
        defaultSet();
    }

    private void defaultSet() {
        linearizedArray = new Pieces[121];
        Arrays.fill(linearizedArray, Pieces.EMPTY);
        setUpRails();
        setupPieces();
    }

    void setUpRails() {
        for (int i = 0; i < 11; i++) {
            linearizedArray[i] = Pieces.RAIL;
        }
        for (int i = 110; i < 121; i++) {
            linearizedArray[i] = Pieces.RAIL;
        }
        for (int i = 11; i < 110; i += 11) {
            linearizedArray[i] = Pieces.RAIL;
        }
        for (int i = 21; i < 121; i += 11) {
            linearizedArray[i] = Pieces.RAIL;
        }
        int[] fillerSpaces = {100, 101, 102, 103, 89, 90, 91, 78, 79, 67,
                            53, 41, 42, 29, 30, 31, 17, 18, 19, 20 };
        for (int pos : fillerSpaces) {
            linearizedArray[pos] = Pieces.RAIL;
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
            linearizedArray[white] = Pieces.WHITE;
        }
        for (int black : blackPieces) {
            linearizedArray[black] = Pieces.BLACK;
        }
    }

    void displayBoard() {
        int indent = 0;
        for (char row = 'i'; row >= 'a'; row--) {
            String str = " ";
            String repeated = str.repeat(indent);
            indent += 2;
            System.out.print(repeated);
            for (int column = 1; column <= 9; column++) {
                String pos = "" + row + column;
                Pieces piece = linearizedArray[toIndex(pos)];
                if (piece == Pieces.WHITE) {
                    System.out.print(" O ");
                } else if (piece == Pieces.BLACK) {
                    System.out.print(" @ ");
                } else if (piece == Pieces.EMPTY) {
                    System.out.print(" + ");
                } else if (piece == Pieces.RAIL) {
                    System.out.print("   ");
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
        for (int i = linearizedArray.length - 1 ; i >= 0; i--) {
            Pieces piece = linearizedArray[i];
            if (piece == Pieces.WHITE) {
                rowStringBuilder.insert(0, " O ");
            } else if (piece == Pieces.BLACK) {
                rowStringBuilder.insert(0, " @ ");
            } else if (piece == Pieces.EMPTY) {
                rowStringBuilder.insert(0, " + ");
            } else if (piece == Pieces.RAIL) {
                rowStringBuilder.insert(0, " X ");
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

    private Pieces[] linearizedArray;
}
