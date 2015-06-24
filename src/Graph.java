
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
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
    private int globalBestTourSurvival;
    private final List<Tour> previousTours;

    private Tour bestByBruteForce;

    //ACO parameters
    private final int RHI; //Relative Heueristic Importance >0
    private final int RPI; //Relative Pheremone Importance >0
    private final float PDR; //Phermone Degration Rate (0,1]
    private final float SPP; //Standard Pheremone Placement ammout 
    private final int TDC; //Tour Distance Cutoff value for tours a TDCth bigger than the global best tour >=1
    private final float ECV; //Exploration Choice Value (0,1]
    private final int API; //Ants Per Iteration >=1
    private final int ITT; //Total Iterations >=1

    public Graph(int relativeHeueristicImportance, int relativePheremoneImportance,
            float phermoneDegrationRate, int tourDistanceCutoff,
            float explorationChoiceValue, int numberOfAntPerIteration,
            int numberOfIterations) {

        this.graph = new HashMap<>();
        rand = new Random();

        //Populate the graph with ndoes
        FileUtils.loadNodesIntoGraph(this, "nodes0.txt");

        Tour firstTour = generateFirstTour();

        globalBestTour = firstTour;
        globalBestTourSurvival = 0;
        previousTours = new ArrayList<>();

        //Set ACO params and check they are valid
        boolean valid = true;
        this.RHI = relativeHeueristicImportance;
        valid = this.RHI > 0;
        this.RPI = relativePheremoneImportance;
        valid = this.RPI > 0;
        this.PDR = phermoneDegrationRate;
        valid = this.PDR > 0 && this.PDR <= 1;
        this.TDC = tourDistanceCutoff;
        valid = this.TDC >= 1;
        this.ECV = explorationChoiceValue;
        valid = this.ECV > 0 && this.ECV <= 1;
        this.API = numberOfAntPerIteration;
        valid = this.API > 0;
        this.ITT = numberOfIterations;
        valid = this.ITT > 0;

        this.SPP = graph.size() * ((float) 1 / firstTour.getTourDistance());

        this.bestByBruteForce = firstTour;
        System.out.println("First tour: " + firstTour);
    }

    public int getGlobalBestTourSurvival() {
        return globalBestTourSurvival;
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

    private Tour generateFirstTour() {
        //Construct a tour of closest neightbours
        Node n = new ArrayList<>(graph.values()).get(0);
        Ant a = new Ant(n);

        do {
            Node next = null;
            int dist = Integer.MAX_VALUE;

            //Set the next node equal to the node's closest neighbour
            for (Node linkedNode : n.getLinkedNodes().keySet()) {
                if (!a.getVisited().contains(linkedNode)) {
                    if (n.getDistBetweeNodes(linkedNode) < dist) {
                        dist = n.getDistBetweeNodes(linkedNode);
                        next = linkedNode;
                    }
                }
            }

            a.setNextNode(next);
            a.moveToNextNode();
        } while (a.getVisited().size() != graph.size());

        //Return to Beginning
        a.setNextNode(a.getVisited().getFirst());
        a.moveToNextNode();

        return new Tour(a.getVisited(), a.getDistanceTravelled());
    }

    public Tour getBestByBruteForce() {
        //Get the best tour through a brute force approach
        Node n = new ArrayList<>(graph.values()).get(0);
        Ant a = new Ant(n);

        bestByBruteForce(a);

        return bestByBruteForce;
    }

    private void bestByBruteForce(Ant a) {
        //Two path terminating special cases (basically, if the path has covered all nodes expect the target)
        if (a.getVisited().size() == graph.size()) {

            //Set the ant's next node to the beginning node and move there
            a.setNextNode(a.getVisited().getFirst());
            a.moveToNextNode();

            //Ant has completed its tour, 
            Tour completedTour = new Tour(a.getVisited(), a.getDistanceTravelled());

            //Check if the tour is the new global best
            if (completedTour.getTourDistance() < bestByBruteForce.getTourDistance()) {
                //Set the shorest path to this path if the total path distance is less
                bestByBruteForce = completedTour;
            }

            a.moveBackwards();
        } else {
            for (Node linkedNode : a.getCurrentNode().getLinkedNodes().keySet()) {
                //For each linked node that has not already been visited and that is not the target
                if (!a.getVisited().contains(linkedNode)) {
                    //Only move onto the next node if there is a chance of the path being smaller
                    if (a.getDistanceTravelled() + a.getCurrentNode().getDistBetweeNodes(linkedNode) < bestByBruteForce.getTourDistance()) {
                        a.setNextNode(linkedNode);
                        a.moveToNextNode();
                        bestByBruteForce(a);
                    }
                }
            }
        }

        a.moveBackwards();
    }

    public Tour startACO() {
        List<Node> nodeList = new ArrayList<>(graph.values());

        for (int interNo = 0; interNo < ITT; interNo++) {
            //Place ants on random nodes
            List<Ant> ants = new ArrayList<>();
            for (int i = 0; i < API; i++) {
                int randomIndex = rand.nextInt(nodeList.size());
                Node randomNode = nodeList.get(randomIndex);

                ants.add(new Ant(randomNode));
            }

            //Do ACO on the graph using the given ants
            doACO(ants);
        }

        return globalBestTour;
    }

    public void doACO(List<Ant> ants) {
        //Construct solutions
        for (Ant a : ants) {
            do {
                a.setNextNode(chooseNextNode(a));
                localUpdate(a);
                a.moveToNextNode();
            } while (a.getVisited().size() != graph.size() + 1);

            //Ant has completed its tour, 
            Tour completedTour = new Tour(a.getVisited(), a.getDistanceTravelled());

            //Check if the tour is the new global best
            if (completedTour.getTourDistance() < globalBestTour.getTourDistance()) {
                //If so, set it as the new global best
                globalBestTour = completedTour;
                globalBestTourSurvival = 1;
            } else {
                //The globalBestTour survived
                globalBestTourSurvival++;
            }

//            else if (completedTour.getTourDistance() == globalBestTour.getTourDistance()) {
//                //If the tour was the same distance as the GB, increase the phermeone levels of the tour
//                //e*1/Lgb where e is the numer of times the tour was completed by an ant
//
//            } 
//            else if (completedTour.getTourDistance() >= Math.ceil(globalBestTour.getTourDistance() * (1 / TDC))) {
//                //If the complted tour distance was a TDCth bigger than the glboalBest, reduce it's pheremone levels
//
//            }
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

        TreeMap<Double, Node> probabilitiesForNodes = new TreeMap<>();
        double totalProb = 0d;

        for (Node n : graph.values()) {
            if (!a.getVisited().contains(n)) {
                //For each node that has not yet been visited
                //calulate it's weighted probabily
                double weightedProbability
                        = (Math.pow(nodesLinkedToCurrentNode.get(n).getPheremoneLevel(), RPI))
                        * (Math.pow((double) 1 / nodesLinkedToCurrentNode.get(n).getDistance(), RHI));
                totalProb += weightedProbability;

                //Map the node to its probability
                probabilitiesForNodes.put(weightedProbability, n);
            }
        }

        double testTotalProb = 0d;//TODO: remove
        for (Double d : probabilitiesForNodes.keySet()) {
            testTotalProb += d;
        }
        if (testTotalProb != totalProb) {
//            System.out.println("Hmm????");
            totalProb = testTotalProb;
        }

        Node nodeToReturn = null;
        if (rand.nextFloat() <= ECV) {
            //Exploration:
            //Choose the node with the best known weighted probabillity

            nodeToReturn = probabilitiesForNodes.lastEntry().getValue();

        } else {
            //Biased Exploration
            //Randomly choose the next node based on the weighted probabilities
            //of the avilable nodes

            double r = rand.nextDouble();
            double cumulitiveProb = 0d;

            for (Double d : probabilitiesForNodes.keySet()) {
                cumulitiveProb += d / totalProb;//For some reason the total isn't correct, unless set :????
                if (cumulitiveProb >= r) {
                    nodeToReturn = probabilitiesForNodes.get(d);
                    break;
                }
            }
        }

        if (nodeToReturn == null) {//TODO: remove
            System.out.println("No next node set! Exiting...");
            System.exit(1);
        }

        return nodeToReturn;
    }

    private void localUpdate(Ant a) {
        Edge e = a.getCurrentNode().getEdgeBetweenNodes(a.getNextNode());
        e.setPheremoneLevel((1 - PDR) * a.getCurrentNode().getPhermoneLevelBetweenNodes(a.getNextNode()) + PDR * SPP);
    }

    private void globalUpdate(Tour t) {
        Node curNode = null;
        for (Iterator<Node> i = t.getNodesInTour().iterator(); i.hasNext();) {
            if (curNode == null) {
                curNode = i.next();
            }
            Node folNode = i.next();

            //Update the phermeone level on each edge of the tour
            Edge e = curNode.getEdgeBetweenNodes(folNode);
            e.setPheremoneLevel((1 - PDR) * curNode.getPhermoneLevelBetweenNodes(folNode) + PDR * ((float) 1 / t.getTourDistance()));

            curNode = folNode;
        }
    }
}
