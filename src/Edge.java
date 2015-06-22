
/**
 *
 * @author Oliver Funk
 */
public class Edge {

    private int distance;
    private float pheremoneLevel;
    
    private final float DEFAULT_PHEREMONE_LEVEL = 1f;

    public Edge(int distance, float pheremoneLevel) {
        this.distance = distance;
        this.pheremoneLevel = pheremoneLevel;
    }

    public Edge(int distance) {
        this.distance = distance;
        this.pheremoneLevel = DEFAULT_PHEREMONE_LEVEL;
    }
    
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public float getPheremoneLevel() {
        return pheremoneLevel;
    }

    public void setPheremoneLevel(float pheremoneLevel) {
        this.pheremoneLevel = pheremoneLevel;
    }
}
