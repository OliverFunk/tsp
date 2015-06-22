
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Oliver Funk
 */
public class Ant {

    private Node currentNode;
    private Node nextNode;

    private final Deque<Node> visited;
    private int distanceTravelled;

    public Ant(Node currentNode) {
        this.currentNode = currentNode;
        this.nextNode = null;
        this.visited = new LinkedList<>();
        visited.add(currentNode);
        distanceTravelled = 0;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public Deque<Node> getVisited() {
        return new ArrayDeque<>(visited);
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }
    
    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public void moveToNextNode(){
        distanceTravelled += currentNode.getDistBetweeNodes(nextNode);
        
        currentNode = nextNode;
        nextNode = null;

        visited.add(currentNode);
    }

    @Override
    public String toString() {
        if (!visited.getFirst().equals(visited.getLast())) {
            return "There was a problem with this ant's tour, the beginning and end nodes are not the same!";
        }

        String out = "Ant's Tour: ";
        int totalDist = 0;

        Node curNode = null;
        for (Iterator<Node> i = visited.iterator(); i.hasNext();) {
            if (curNode == null) {
                curNode = i.next();
            }
            Node folNode = i.next();

            out += curNode.getName() + "->";
            totalDist += curNode.getDistBetweeNodes(folNode);

            curNode = folNode;
        }
        
        String message = (totalDist == distanceTravelled) ? "The distances added up :)" : "The distances did not add up, wtf \\:?/";
        System.out.println(message);
        
        out += visited.getFirst().getName() + ": " + totalDist;//The beginning and end nodes are the same
        return out;
    }
}
