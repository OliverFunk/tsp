
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Oliver Funk
 */
public class Node {

    private String name;
    private Map<Node, Edge> linkedNodes;

    public Node(String name) {
        this.name = name;
        this.linkedNodes = new HashMap<>();
    }
    
    public Node (String name, Node linkedNode, int distanceToLinkedNode){
        this.name = name;
        this.linkedNodes = new HashMap<>();
        
        linkedNodes.put(linkedNode, new Edge(distanceToLinkedNode));
    }

    public String getName() {
        return name;
    }
    
    public void linkNode(Node linkedNode, int distanceToLinkedNode){
        linkedNodes.put(linkedNode, new Edge(distanceToLinkedNode));
    }
    
    public Map<Node, Edge> getLinkedNodes(){
        return Collections.unmodifiableMap(linkedNodes);
    }
    
    //Convieniance methods
    public Edge getEdgeBetweenNodes(Node lookupNode){
        return linkedNodes.get(lookupNode);
    }
    public float getPhermoneLevelBetweenNodes(Node lookupNode){
        return linkedNodes.get(lookupNode).getPheremoneLevel();
    }
    public int getDistBetweeNodes(Node lookupNode){
        return linkedNodes.get(lookupNode).getDistance();
    }
}
