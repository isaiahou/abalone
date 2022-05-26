import java.io.*;

/** Class containing functions that deep clone selected objects.
 *  Credits to online references for source code.
 */

public class DeepClone {

    static Game cloneGame(Game game) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(game);
        oos.flush();
        oos.close();
        bos.close();
        byte[] byteData = bos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
        return (Game) new ObjectInputStream(bais).readObject();
    }

    static Board cloneBoard(Board board) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(board);
        oos.flush();
        oos.close();
        bos.close();
        byte[] byteData = bos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
        return (Board) new ObjectInputStream(bais).readObject();
    }
}
