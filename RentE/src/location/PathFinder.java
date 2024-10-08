package location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The PathFinder class is responsible for finding the shortest path 
 * between two locations on a grid using an algorithm.
 */
public class PathFinder {
    private Location start;
    private Location end;
    private Boolean isWide;

    private static final int GRID_SIZE = 20; // Size of the grid
    private static final int[] DX = {-1, 1, 0, 0}; // Direction vectors for x
    private static final int[] DY = {0, 0, -1, 1}; // Direction vectors for y
    
    /**
     * Constructs a PathFinder with the specified start and end locations.
     *
     * @param start the starting location
     * @param end the ending location
     */
    public PathFinder(Location start, Location end) {
        this.start = start;
        this.end = end;
        this.isWide = false;
    }

    /**
     * Calculates the shortest path from the start location to the end location using Dijkstra's algorithm.
     *
     * @return an array of Locations representing the path from start to end,
     *         or null if no path is found
     * @throws OutOfRadiusException if the start or end location is out of the valid radius
     */
    public Location[] getPathDijkstra() throws OutOfRadiusException {
        int[][] distances = new int[GRID_SIZE][GRID_SIZE];
        Location[][] predecessors = new Location[GRID_SIZE][GRID_SIZE];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distances[start.getX()][start.getY()] = 0;

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            Location currentPoint = currentNode.getPoint();

            if (currentPoint.getX() == end.getX() && currentPoint.getY() == end.getY()) {
                return reconstructPath(predecessors, start, end);
            }

            for (int i = 0; i < 4; i++) {
                int newX = currentPoint.getX() + DX[i];
                int newY = currentPoint.getY() + DY[i];
                if (isValid(newX, newY) && distances[newX][newY] > currentNode.getDistance() + 1) {
                    distances[newX][newY] = currentNode.getDistance() + 1;
                    predecessors[newX][newY] = currentPoint;
                    queue.add(new Node(new Location(newX, newY), distances[newX][newY]));
                }
            }
        }
        return null; // If no path is found
    }

    /**
     * Checks if the given coordinates are valid within the grid.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are valid, false otherwise
     */
    private static boolean isValid(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    /**
     * Reconstructs the path from the predecessors array.
     *
     * @param predecessors a 2D array of Locations representing the predecessor of each Location
     * @param start the starting location
     * @param end the ending location
     * @return an array of Locations representing the reconstructed path
     */
    private Location[] reconstructPath(Location[][] predecessors, Location start, Location end) {
        List<Location> path = new ArrayList<>();
        for (Location at = end; at != null; at = predecessors[at.getX()][at.getY()]) {
            if (at.getX() < 5 || at.getX() > 14 || at.getY() < 5 || at.getY() > 14) {
                this.isWide = true;
            }
            path.add(at);
        }
        Collections.reverse(path);
        return path.toArray(new Location[0]);
    }

    /**
     * Checks if the path is wide.
     *
     * @return true if the path is wide, false otherwise
     */
    public Boolean isWide() {
        return this.isWide;
    }
}
