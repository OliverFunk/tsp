
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author Oliver Funk
 */
public class Graph {

    private final Map<String, Node> graph;
    private final Random rand;

    private Tour globalBestTour;
    private List<Tour> previousTours;

    //ACO parameters
    private int RHI; //Relative Heueristic Importace
    private int RPI; //Relative Pheremone Imporance
    private int PDR; //Phermone Degration Rate (0,1]
    private float SPP; //Standard Pheremone Placement ammout
    private int TDC; //Tour Distance Cutoff value for tours a TDCth bigger than the global best tour
    private float ECV; //Exploration Choice Value (0,1]
    private int numberOfAntPerIteration;
    private int numberOfIterations;

    public Graph() {
        this.graph = new HashMap<>();
        rand = new Random();

        globalBestTour = new Tour(null, Integer.MAX_VALUE);
        previousTours = new ArrayList<>();

        //Set ACO params and check they are valid
    }

    public Map<String, Node> getGraph() {
        return Collections.unmodifiableMap(graph);
    }

    public void insertNodesIntoGraph(String i, String j, int distFromIToJ) {
        Node Ni = graph.get(i);
        Node Nj = graph.get(j);

        if (Ni == null && Nj == null) {
            Ni = new Node(i);
            Nj = new Node(j, Ni, distFromIToJ);
            Ni.linkNode(Nj, distFromIToJ);

            graph.put(i, Ni);
            graph.put(j, Nj);
            return;
        }

        if (Ni == null) {
            Ni = new Node(i);
            graph.put(i, Ni);
        }

        if (Nj == null) {
            Nj = new Node(j);
            graph.put(j, Nj);
        }

        Ni.linkNode(Nj, distFromIToJ);
        Nj.linkNode(Ni, distFromIToJ);
    }

    public void startACO() {
        List<Ant> ants = new ArrayList<>();
        List<Node> nodeList = new ArrayList<>(graph.values());

        for (int interNo = 0; interNo < numberOfIterations; interNo++) {
            //Place ants on random nodes
            for (int i = 0; i < numberOfAntPerIteration; i++) {
                int randomIndex = rand.nextInt(nodeList.size());
                Node randomNode = nodeList.get(randomIndex);

                ants.add(new Ant(randomNode));
            }

            //Begin ACO on the graph
            doACO(ants);
        }
    }

    public void doACO(List<Ant> ants) {
        //Construct solutions
        for (Ant a : ants) {
            do {
                a.setNextNode(chooseNextNode(a));
                localUpdate(a);
                a.moveToNextNode();
            } while (a.getVisited().size() == graph.size() + 1);

            //Ant has completed its tour, 
            Tour completedTour = new Tour(a.getVisited(), a.getDistanceTravelled());

            //Check if the tour is the new global best
            if (completedTour.getTourDistance() < globalBestTour.getTourDistance()) {
                //If so, set it as the new global best
                globalBestTour = completedTour;
            } else if (completedTour.getTourDistance() == globalBestTour.getTourDistance()) {
                //If the tour was the same distance as the GB, increase the phermeone levels of the tour
                //e*1/Lgb where e is the numer of times the tour was completed by an ant
                
            } else if (completedTour.getTourDistance() >= Math.ceil(globalBestTour.getTourDistance() * (1 / TDC))) {
                //If the complted tour distance was a TDCth bigger than the glboalBest, reduce it's pheremone levels
                
            }

            //add the tour to the list of previous tours
            previousTours.add(completedTour);
        }
        
        //Run the global update method on the, so far, global best tour
        globalUpdate(globalBestTour);
    }

    private Node chooseNextNode(Ant a) {
        if (a.getVisited().size() == graph.size()) {
            //The ant has been to all nodes, and now needs to return to it's starting node
            return a.getVisited().getFirst();
        }

        Map<Node, Edge> nodesLinkedToCurrentNode = a.getCurrentNode().getLinkedNodes();

        TreeMap<Node, Double> probabilitiesForNodes = new TreeMap<>();
        double totalProb = 0d;

        for (Node n : graph.values()) {
            if (!a.getVisited().contains(n)) {
                double weightedProbability
                        = (Math.pow(nodesLinkedToCurrentNode.get(n).getPheremoneLevel(), RPI))
                        * (Math.pow((double) 1 / nodesLinkedToCurrentNode.get(n).getDistance(), RHI));

                totalProb += weightedProbability;
                probabilitiesForNodes.put(n, weightedProbability);
            }
        }

        Node nodeToReturn = null;
        if (rand.nextFloat() <= ECV) {
            //Exploration:
            //Choose the node with the best known weighted probabillity

            nodeToReturn = probabilitiesForNodes.lastKey();
        } else {
            //Biased Exploration
            //Randomly choose the next node based on the weighted probabilities
            //of the avilable nodes

            double r = rand.nextDouble();
            double cumlitiveProb = 0d;

            for (Node n : probabilitiesForNodes.keySet()) {
                cumlitiveProb += probabilitiesForNodes.get(n) / totalProb;
                if (cumlitiveProb >= r) {
                    nodeToReturn = n;
                    break;
                }
            }
        }

        return nodeToReturn;
    }

    private void localUpdate(Ant a) {
        Edge e = a.getCurrentNode().getEdgeBetweenNodes(a.getNextNode());
        e.setPheremoneLevel((1 - PDR) * a.getCurrentNode().getPhermoneLevelBetweenNodes(a.getNextNode()) + PDR * SPP);
    }

    private void globalUpdate(Tour t) {
        
    }
}
