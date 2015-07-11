
import java.util.Scanner;

/**
 *
 * @author Oliver Funk
 */
public class UI {

    public static void main(String[] args) {
        //Start the UI
        new UI().run();
    }

    private void run() {
        try (Scanner sc = new Scanner(System.in)) {

            //Creating the file
            System.out.println("Would you like to create a new nodes file? [Y/N]");
            while (!sc.hasNext("[ynYN]")) {
                System.out.println("Please enter only Y or N");
                sc.next();
            }
            if (sc.next().toUpperCase().charAt(0) == 'Y') {

                System.out.println("Please enter the number of nodes you would like: (whole numbers only):");
                while (!sc.hasNextInt()) {
                    System.out.println("That's not a whole number");
                    System.out.println("Please enter the number of nodes you would like (whole numbers only):");
                    sc.next();
                }
                int nodes = sc.nextInt();
                
                if(nodes >= 300){
                    System.out.println("It may take a while to generate this file.");
                }

                //Create a new file, defining nodes and their relative distances
                FileUtils.genNewNodeFile(nodes, 100);
            }

            //Running brute force
            Graph g = new Graph(2, 1, 0.7f, 10, 0.7f, 20, 5);
            System.out.println("\nWould you like to find the best tour through brute force first? [Y/N]");
            while (!sc.hasNext("[ynYN]")) {
                System.out.println("Please enter only Y or N");
                sc.next();
            }
            if (sc.next().toUpperCase().charAt(0) == 'Y') {
                if (g.getGraph().size() >= 16) {
                    
                    System.out.println("Are you sure you want to run the brute force method on this graph? [Y/N");
                    System.out.println("There are more than 15 nodes so it may take a very long time to complete");
                    while (!sc.hasNext("[ynYN]")) {
                        System.out.println("Please enter only Y or N");
                        sc.next();
                    }
                    if (sc.next().toUpperCase().charAt(0) == 'Y') {
                        System.out.println("Best by brute force: " + g.runBruteForce());
                    }
                } else {
                    System.out.println("Best by brute force: " + g.runBruteForce());
                }
            }

            //Running ACO
            System.out.println("");
            char choice;
            do {
                g = new Graph(2, 1, 0.7f, 10, 0.7f, 20, 5);
                System.out.println("Best by ACO: " + g.runACO());

                System.out.println("Would you like to run the ACO again on a fresh graph? [Y/N]");
                while (!sc.hasNext("[ynYN]")) {
                    System.out.println("Please enter only Y or N");
                    sc.next();
                }
                choice = sc.next().toUpperCase().charAt(0);
            } while (choice == 'Y');

            System.out.println("\nCheers! :D");
        }
    }

    private void testingRun() {
        //Create a new file, defining nodes and their relative distances
        FileUtils.genNewNodeFile(15, 100);

        Graph gForBest = new Graph(2, 1, 0.7f, 10, 0.7f, 20, 5);
        Tour best = gForBest.runBruteForce();
        int bestDist = best.getTourDistance();
        System.out.println("best " + best);
        gForBest = null;

        int gbTot = 0;
        int distTot = 0;
        int noOfPerfect = 0;
        int n = 50;
        for (int i = 0; i < n; i++) {
            Graph g = new Graph(4, 1, 0.7f, 10, 0.6f, 20, 10);
            Tour t = g.runACO();

            int dist = t.getTourDistance();
            distTot += dist;

            int gb = g.getGlobalBestTourSurvival();
            gbTot += gb;

            if (dist == bestDist) {
                noOfPerfect++;
            }

            System.out.println(t);
            System.out.println("gb " + gb);
        }

        System.out.println("gb avg: " + gbTot / n);
        System.out.println("dist avg: " + distTot / n);
        System.out.println("perfect: " + noOfPerfect);

        //(survival avg, dist avg, no of perfect)
        System.out.println("(" + gbTot / n + "," + distTot / n + "," + noOfPerfect + ")");
    }
}
