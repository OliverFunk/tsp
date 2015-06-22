
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Oliver Funk
 */
public class UI {

    public static void main(String[] args) {
        new UI().run();
    }

    private void run() {
        try {
            Graph g = new Graph(2, 1, 0.1f, 10, 0.6f, 10, 10);
            System.out.println(g.startACO());
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, "OH SH!T", ex);
        }
    }

}
