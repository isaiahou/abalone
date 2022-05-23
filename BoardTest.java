import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void toIndex() {
        Board b = new Board("default");
        assertEquals(12, b.toIndex("a1"));
        assertEquals(23, b.toIndex("b1"));
        assertEquals(105, b.toIndex("i6"));
    }

    @Test
    void displayBoard() {
        Board b = new Board("default");
        b.displayBoard();
    }

    @Test
    void testToString() {
        Board b = new Board("default");
        System.out.println(b);
    }

    @Test
    void indexToString() {
        Board b = new Board("default");
        for (int i = 0; i < 121; i++) {
            System.out.println(b.indexToString(i));
        }
    }
}