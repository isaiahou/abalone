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
        for (int i = 21; i < 120; i += 11) {
            linearizedArray[i] = Pieces.RAIL;
        }
    }

    private void setupPieces() {
        int[] whitePieces = { 104, 105, 106, 107, 107,
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
