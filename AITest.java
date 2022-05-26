import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void getMyPieces() {
        Board b = new Board("default");
        //AI a = new AI(b, Pieces.BLACK);
        //System.out.println(a.getMyPieces());
    }

    @Test
    void getMarbleStrings() {
        Board b = new Board("default");
        //AI a = new AI(b, Pieces.BLACK);
        //System.out.println(a.getMarbleStrings());
    }

    @Test
    void getLegalMoves() {
    }

    @Test
    void testGetLegalMoves() throws IOException, ClassNotFoundException {
        Game g = new Game("default");
        AI a = new AI(g);
        int counter = 0;
        List<Move> moves = a.getLegalMoves(g);
        for (Move move: moves) {
            g.executeMove(move);
            g.getBoard().displayBoard();
            System.out.println(counter);
            counter++;
            g.resetBoard();
        }
    }
}