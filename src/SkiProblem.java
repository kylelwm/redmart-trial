import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class SkiProblem {

	public static void main(String[] args) {
		// Read input map
		Scanner sc = new Scanner(System.in);
		int mapSizeX = sc.nextInt();
		int mapSizeY = sc.nextInt();
		int[][] map = new int[mapSizeX][mapSizeY];
		for(int i = 0; i < mapSizeX; i++) {
			for(int j = 0; j < mapSizeY; j++) {
				map[i][j] = sc.nextInt();
			}
		}
		sc.close();
		
		// Construct DAG
		Graph dag = constructDAG(map, mapSizeX, mapSizeY);
		
		int[] topologicalOrdering = dag.topologicalSort();
		
		int dist[] = new int[dag.numOfVertices];
		int steepness[] = new int[dag.numOfVertices];
		
		int maxDist = Integer.MIN_VALUE;
		int maxSteepness = Integer.MIN_VALUE;
		// Find longest path
		for(int i = 0; i < topologicalOrdering.length; i++) {
			int currentVertex = topologicalOrdering[i];
			for(Integer child: dag.adjList[currentVertex]) {
				
				int steepLevel = map[currentVertex/mapSizeY][currentVertex%mapSizeY] - map[child/mapSizeY][child%mapSizeY];
				
				if(dist[child] < dist[currentVertex] + 1) {
					dist[child] = dist[currentVertex] + 1;
					steepness[child] += steepLevel;
					if(dist[child] > maxDist) {
						// Keep track of the largest distance
						maxDist = dist[child];
						// Tie break if required
						
					} else if(dist[child] == maxDist) {
						if(maxSteepness < steepness[child]) {
							maxSteepness = steepness[child];
						}
					}
				}
			}
		}
		
		System.out.println("Max distance: "  + (maxDist + 1) + ", Max steepness: " + maxSteepness);
		
	}
	
	private static Graph constructDAG(int[][] map, int mapSizeX, int mapSizeY) {
		int numOfVertices = mapSizeX * mapSizeY;
		Graph dag = new Graph(numOfVertices);
		
		// For each vertex
		for(int i = 0; i < mapSizeX; i++) {
			for(int j = 0; j < mapSizeY; j++) {
				int currentVerticeHeight = map[i][j];
				int currentVerticeId = i * mapSizeX + j;
				int comparedVerticeId;
				
				//Check north south east west
				if(i - 1 >= 0 && i - 1 < mapSizeX && currentVerticeHeight > map[i-1][j]) {
					comparedVerticeId = (i - 1) * mapSizeX + j;
					dag.addEdge(currentVerticeId, comparedVerticeId);
				}
				if(i + 1 >= 0 && i + 1 < mapSizeX && currentVerticeHeight > map[i+1][j]) {
					comparedVerticeId = (i + 1) * mapSizeX + j;
					dag.addEdge(currentVerticeId, comparedVerticeId);
				}
				if(j + 1 >= 0 && j + 1 < mapSizeY && currentVerticeHeight > map[i][j+1]) {
					comparedVerticeId = i * mapSizeX + j + 1;
					dag.addEdge(currentVerticeId, comparedVerticeId);
				}
				if(j - 1 >= 0 && j - 1 < mapSizeY && currentVerticeHeight > map[i][j-1]) {
					comparedVerticeId = i * mapSizeX + j - 1;
					dag.addEdge(currentVerticeId, comparedVerticeId);
				}
			}
		}
		
		return dag;
	}
}


class Graph {
	public int numOfVertices;
	public LinkedList<Integer> adjList[];
 
    //Constructor
    Graph(int numOfVertices) {
    	this.numOfVertices = numOfVertices;
        this.adjList = new LinkedList[this.numOfVertices];
        for (int i = 0; i < numOfVertices; i++)
            this.adjList[i] = new LinkedList<Integer>();
    }
 
    // Function to add an edge into the graph
    void addEdge(int u,int v) {
    	adjList[u].add(v); 
    }
    
    // A recursive function used by topologicalSort
    void topologicalSortUtil(int v, boolean visited[], Stack<Integer> stack) {
        // Mark the current node as visited.
        visited[v] = true;
        Integer i;
 
        // Recur for all the vertices adjacent to this
        // vertex
        Iterator<Integer> it = adjList[v].iterator();
        while (it.hasNext())
        {
            i = it.next();
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);
        }
 
        // Push current vertex to stack which stores result
        stack.push(new Integer(v));
    }
 
    // The function to do Topological Sort. It uses
    // recursive topologicalSortUtil()
	int[] topologicalSort() {
        Stack<Integer> stack = new Stack<Integer>();
        // Mark all the vertices as not visited
        boolean visited[] = new boolean[numOfVertices];
        for (int i = 0; i < numOfVertices; i++)
            visited[i] = false;
 
        // Call the recursive helper function to store
        // Topological Sort starting from all vertices
        // one by one
        for (int i = 0; i < numOfVertices; i++)
            if (visited[i] == false) {
                topologicalSortUtil(i, visited, stack);           	
            }
 
        // Return an integer array of ordering
        int topologicalOrdering[] = new int[this.numOfVertices];
        int i = 0;
        while (stack.empty()==false) {
        	topologicalOrdering[i] = stack.pop();
        	i++;
        }
        return topologicalOrdering;
    }
}
