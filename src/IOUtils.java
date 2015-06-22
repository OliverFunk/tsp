
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Oliver Funk
 */
public class IOUtils {

    private IOUtils() {
    }

    private static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public static void loadNodesIntoGraph(Graph graph, String filename) throws FileNotFoundException, IOException {
        String TSPdata = readFile(filename);

        Pattern eachNode = Pattern.compile("\\{(.*?)\\}");
        Matcher nodePattern = eachNode.matcher(TSPdata);

        while (nodePattern.find()) {
            String[] vals = nodePattern.group(1).split(",");
            try {
                graph.insertNodesIntoGraph(vals[0], vals[1], Integer.parseInt(vals[2]));
            } catch (NumberFormatException ne) {
                System.out.println("There was an error while inserting: " + ne);
            }
        }
    }
}
