
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Oliver Funk
 */
public class Tour {
    
    private final Deque<Node> nodesInTour;
    private final int tourDistance;

    public Tour(Deque<Node> nodesInTour, int tourDistance) {
        this.nodesInTour = nodesInTour;
        this.tourDistance = tourDistance;
    }

    public Deque<Node> getNodesInTour() {
        return new ArrayDeque<>(nodesInTour);
    }

    public int getTourDistance() {
        return tourDistance;
    }
    
        @Override
    public String toString() {
        if (!nodesInTour.getFirst().equals(nodesInTour.getLast())) {
            return "There was a problem with this tour, the beginning and end nodes are not the same!";
        }

        String out = "Ant's Tour: ";
        
        out += nodesInTour.stream()
                .map((n) -> n.getName())
                .collect(joining("->"));
        out += ": " + tourDistance;
        
        return out;
    }
}
