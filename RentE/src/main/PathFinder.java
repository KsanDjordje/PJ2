package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class PathFinder {
	private Location start;
	private Location end;
	private Boolean isWide;
	
	
	private static final int GRID_SIZE = 20;
    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};
	
	public PathFinder(Location start, Location end) {
		this.start = start;
		this.end = end;
		this.isWide = false;
	}
	

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
            Location currentPoint = currentNode.point;

            if (currentPoint.getX() == end.getX() && currentPoint.getY() == end.getY()) {
                return reconstructPath(predecessors, start, end);
            }

            for (int i = 0; i < 4; i++) {
                int newX = currentPoint.getX() + DX[i];
                int newY = currentPoint.getY() + DY[i];
                if (isValid(newX, newY) && distances[newX][newY] > currentNode.distance + 1) {
                    distances[newX][newY] = currentNode.distance + 1;
                    predecessors[newX][newY] = currentPoint;
                    queue.add(new Node(new Location(newX, newY), distances[newX][newY]));
                }
            }
        }
        return null; // If no path is found
    }

    private static boolean isValid(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    private Location[] reconstructPath(Location[][] predecessors, Location start, Location end) {
        List<Location> path = new ArrayList<>();
        for (Location at = end; at != null; at = predecessors[at.getX()][at.getY()]) {
        	if(at.getX() < 5 || at.getX() > 14 || at.getY() < 5 || at.getY() > 14) {
        		this.isWide = true;
        	}
            path.add(at);
        }
        Collections.reverse(path);
        return path.toArray(new Location[0]);
    }
    
    public Boolean isWide() {
    	return this.isWide;
    }
	
}
