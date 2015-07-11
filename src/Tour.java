
import java.util.ArrayDeque;
import java.util.Deque;

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

        String out = "";
        for (Node i : nodesInTour) {
            out += out.isEmpty() ? i.getName() : "->" + i.getName();
        }
        out += ": " + tourDistance;

        return out;
    }
}
