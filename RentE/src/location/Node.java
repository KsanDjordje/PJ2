package location;

/**
 * Represents a node in a pathfinding algorithm, containing a location and its associated distance.
 */
public class Node implements Comparable<Node> {

    private Location point;  // The location represented by this node
    private int distance;     // The distance associated with this node

    /**
     * Creates a Node with a specified location and distance.
     *
     * @param point   the location of the node
     * @param distance the distance associated with the node
     */
    public Node(Location point, int distance) {
        this.point = point;
        this.distance = distance;
    }

    /**
     * Compares this node with another node for ordering based on distance.
     *
     * @param other the node to be compared
     * @return a negative integer, zero, or a positive integer as this node is less than, equal to,
     *         or greater than the specified node
     */
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }

    /**
     * Gets the location of the node.
     *
     * @return the location of this node
     */
    public Location getPoint() {
        return point;
    }

    /**
     * Gets the distance associated with the node.
     *
     * @return the distance of this node
     */
    public int getDistance() {
        return distance;
    }
}
