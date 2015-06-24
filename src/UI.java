
/**
 *
 * @author Oliver Funk
 */
public class UI {

    public static void main(String[] args) {
        //Create a new file, defining nodes and their relative distances
        FileUtils.genNewFile(13, 100, "nodes0.txt");
        
        //Start the UI
        new UI().run();
        

//        TreeMap<Double, String> probabilitiesForNodes = new TreeMap<>();
//        double totalProb = 0d;
//
//        for (int i = 2; i <= 7; i++) {
//                //For each node that has not yet been visited
//            //calulate it's weighted probabily
//            double weightedProbability
//                    = (Math.pow(1, 1))
//                    * (Math.pow((double) 1 / (300 + i), 2));
//            totalProb += weightedProbability;
//
//            //Map the node to its probability
//            probabilitiesForNodes.put(weightedProbability, "ID: " + i);
//        }
//
//        double testTotalProb = 0d;//TODO: remove
//        for (Double d : probabilitiesForNodes.keySet()) {
//            testTotalProb += d;
//        }
//        if (testTotalProb != totalProb) {
//            System.out.println("Hmm????");
//            totalProb = testTotalProb;
//        }
    }

    private void run() {
        Graph g = new Graph(2, 1, 0.1f, 10, 0.6f, 10, 10);
        System.out.println(g.startACO());
        System.out.println("Tour survival: " + g.getGlobalBestTourSurvival());
        System.out.println(g.getBestByBruteForce());

        //TODO: understand why dafuq the totals aren't working, implment your onw stuf, make code to gen bigger data file.
    }

}
