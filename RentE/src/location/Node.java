package main;

public class Node implements Comparable<Node>{

	Location point;
    int distance;
    Node(Location point, int distance) {
        this.point = point;
        this.distance = distance;
    }
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }
	

}
