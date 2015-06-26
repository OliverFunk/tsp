
/**
 *
 * @author Oliver Funk
 */
public class UI {

    public static void main(String[] args) {
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
        //Create a new file, defining nodes and their relative distances

//        FileUtils.genNewFile(400, 100, "nodes0.txt");
//        int gbTot = 0;
//        int distTot = 0;
//        int noOfPerfect = 0;
//        int n = 50;
//        for (int i = 0; i < n; i++) {
//            Graph g = new Graph(2, 1, 0.7f, 10, 0.7f, 5, 20);
//            Tour t = g.startACO();
//            
//            int dist = t.getTourDistance();
//            distTot += dist;
//            
//            int gb = g.getGlobalBestTourSurvival();
//            gbTot += gb;
//            
//            if(dist == 182){
//                noOfPerfect++;
//            }
//            
//            System.out.println(t);
//            System.out.println("gb " + gb);
//        }
//        System.out.println("gb avg: " + gbTot/n);
//        System.out.println("dist avg: " + distTot/n);
//        System.out.println("perfect: "+noOfPerfect);
//        System.out.println("("+gbTot/n+","+distTot/n+","+noOfPerfect+")");
//        System.out.println(g.getBestByBruteForce());
        //Ant's Tour: A->E->K->G->H->N->L->I->J->D->F->B->O->C->M->A: 182
        //TODO: understand why dafuq the totals aren't working, implment your onw stuf
        
        Graph g = new Graph(2, 1, 0.7f, 10, 0.7f, 20, 5);
        Tour t = g.startACO();
        int gb = g.getGlobalBestTourSurvival();

        System.out.println(t);
        System.out.println("gb survival " + gb);
        
        //(survival avg, dist avg, no of perfect)
        //Inintal 
        //(564, 190, 0) (600, 188, 5) (525, 190, 2) (461, 189, 3)

        //Global reduction
        //(583, 187, 18) (509, 188, 10) (510, 188, 8) (593, 188, 13) (498, 186, 22)
        //times global best was chosen
        //(523, 190, 3) (491, 189, 6) (530, 189, 3) (517,189,3) (582,188,6)
        //Global reduction and times global best was chosen
        //(577,189,12) (584,187,19) (600,188,14) (639,187,13) (580,186,20)
    }

}
