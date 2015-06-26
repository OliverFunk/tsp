
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Oliver Funk
 */
public class FileUtils {

    private FileUtils() {
    }

    private static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public static void loadNodesIntoGraph(Graph graph, String filename) {
        try {
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
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "OH SH!T IOgoneAPE!", ex);
        }
    }

    public static void genNewFile(int numberOfNodes, int distanceRange, String filename) {
        File f = new File(filename);

        if (f.exists()) {
            File newFile;
            int n = 0;
            do {
                n++;
                newFile = new File(filename.substring(0, filename.length() - 5) + (Integer.parseInt("" + filename.charAt(filename.length() - 5)) + n) + ".txt");
            } while (newFile.exists());
            f.renameTo(newFile);
        }

        try {
            f.createNewFile();
            FileWriter writer = new FileWriter(f);

            String toWrite = "";
            Random rand = new Random();

            //Genrate the permutations for nodes conencted to one another
            //Starting at 'A' and going to 'A' + numberOfNodes
            for (int p = 0; p < numberOfNodes - 1; p++) {
                String pNode = "N"+p;
                for (int c = p + 1; c < numberOfNodes; c++) {
                    String cNode = "N"+c;

                    toWrite = toWrite.concat("{" + pNode + "," + cNode + "," + (1 + rand.nextInt(distanceRange - 1)) + "}\n");
                }
            }

            //Remove the trailing spaces
            toWrite = toWrite.substring(0, toWrite.length() - 1);

            //Write, flush and exit the stream.
            writer.write(toWrite);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "OH SH!T IOgoneAPE!", ex);
        }
    }
}
