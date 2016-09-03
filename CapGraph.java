/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Katherine Sullivan.
 * 
 * This code was written for various assignments in the Coursera Specialization:
 * "Java Programming: Object-Oriented Design of Data Structures."
 * It began as a stub file provided by the instructors.
 * 
 */
public class CapGraph implements Graph {

	/* (non-Javadoc)
	 * The CapGraph class implements an unweighted undirected graph.
	 */
	
	HashSet<Integer> vertices;
	HashMap<Integer, HashSet<Integer>> edges;
	
	// 2D arrays to store info about vertex pairs
	
	Integer[][] dist;
	nextEntry[][] next;
	Integer[][] count;
	listOfPaths[][] shortest_paths_array;
	
	/*
	 *  "Betweenness" is a measure of how often an edge is traversed in
	 *  shortest paths between all vertex pairs.  An edge with high
	 *  "betweenness" is critical to connections within the graph
	 *  and removing it may divide a connected graph into multiple
	 *  strongly connected components.
	 */
	
	double[][] betweenness;
	int max_betweenness_from = 0;  // edge with maximum betweenness
	int max_betweenness_to = 0;	   // its reverse will also have max betweenness
	
	int min_vertex; 
	int max_vertex;
	
	/* Constructor */	
	public CapGraph() {
		vertices = new HashSet<Integer>();
		edges = new HashMap<Integer, HashSet<Integer>>();		

		min_vertex = Integer.MAX_VALUE;
		max_vertex = Integer.MIN_VALUE;
	}
	
	private HashSet<Integer> getVertices() {
		return vertices;
	}
	
	public HashMap<Integer, HashSet<Integer>> getEdges() {
		return edges;
	}
	
	public int getNumVertices() {
		return vertices.size();
	}
	
	public int getMinVertex() {
		return min_vertex;
	}
	
	public int getMaxVertex() {
		return max_vertex;
	}
	
	public int getEdgeWithMaxBetweennessFrom() {
		return max_betweenness_from;
	}
	
	public int getEdgeWithMaxBetweennessTo() {
		return max_betweenness_to;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 * 
	 * Adds a new vertex with the given number.
	 * Does not add vertices of negative value or that already exist in graph.
	 * Initializes edges to empty HashSet.
	 * Updates min_vertex and max_vertex if necessary
	 * 
	 */
	
	@Override
	public void addVertex(int num) {
		if (!vertices.contains(num) && (num >= 0)) {
			vertices.add(num);
			edges.put(num, new HashSet<Integer>());
			if (num > max_vertex) {
				max_vertex = num;
			}
			if (num < min_vertex) {
				min_vertex = num;
			}
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 * 
	 * Adds an edge to the graph, only if both 'to' and 'from' are
	 * existing vertices in graph.  If either is not present, does nothing.
	 * First checks if 'from' already has an entry in the hash set of edges.  If so,
	 * adds 'to' to that entry.
	 * If 'from' isn't yet a key in the hash set of edges, creates a new 
	 * set of edges, adds 'to' to it, and adds it to the hash set with 'from' as
	 * its key.
	 * 
	 */
	
	@Override
	public void addEdge(int from, int to) {

		/* check that from and to exist in graph */
		if ((vertices.contains(from)) && (vertices.contains(to))) {
			if (edges.containsKey(from)) {
				/* add new edge to existing vertex entry */
				HashSet<Integer> edge_set = edges.get(from);
				edge_set.add(to);
			}
			else {
				/* add vertex entry and one edge */
				HashSet<Integer> edge_set = new HashSet<Integer>();
				edge_set.add(to);
				edges.put(from, edge_set);
			}
		}
		
	}

	/*
	 * Remove an edge from the graph.  Edges are one directional.
	 * Remember to remove them in matched pairs for an undirected graph.
	 */
	@Override
	public void removeEdge(int from, int to) {
		System.out.println("Removing edge "+from+" "+to);
		if ((vertices.contains(from)) && (vertices.contains(to))) {
			if (edges.containsKey(from)) {
				(edges.get(from)).remove(to);
			}
		}		
	}
	
	/*  
	 * Return edges belonging to a vertex. 
	 * Returns null if vertex does not exist in graph. 
	 * Returns empty set if vertex exists but has no edges.
	 * 
	 * 
	 */	
	@Override
	public HashSet<Integer> getEdges(int from) {
		if (vertices.contains(from)) {
			return(edges.get(from));
		}
		else return null;
	}

	
	/* 
	 * Return true if there exists an edge from 'from' to 'to';
	 * false otherwise.
	 */
	@Override
	public boolean isAnEdge(int from, int to){
		if (vertices.contains(from)) {
			if (getEdges(from).contains(to)) {
				return true;
			}
		}
		return false;
	}
	
	/* 
	 * Print count of shortest paths all pairs -- for debugging
	 */
	
	public void printCount(int array_size) { //, PrintWriter out) {
		int max_count = 0;
		int max_from = 0;
		int max_to = 0;
		
		//System.out.println("Number of shortest paths between all pairs");
		//out.println("Number of shortest paths between all pairs");
		for (int i= 0; i < array_size; i++) {
			for (int j = 0; j < array_size; j++) {
		//		if (count[i][j] == Integer.MAX_VALUE) {
		//			//out.print("MAX ");
		//			System.out.print("MAX ");
		//		}
		//		else {
					//out.print(count[i][j] + " ");
		//			System.out.print(count[i][j] + " ");
					if (count[i][j] > max_count) {
						max_count = count[i][j];
						max_from = i;
						max_to = j;
					}
		//		}
			}
		//	//out.println();
		//	System.out.println();
		}
		//out.println("Max number of shortest paths = " + max_count + 
		//		" from " + max_from + " to " + max_to);   
		System.out.println("Max number of shortest paths = " + max_count + 
						" from " + max_from + " to " + max_to);   
		System.out.println();
	}

	/* 
	 * Print betweenness value of each edge
	 * For debugging
	 */
	
	public void printBetweenness(int array_size) { //, PrintWriter out) {
		double max_betweenness = 0;
		
		System.out.println("Betweenness of each edge");
		//out.println("Betweenness of each edge");
		
		for (int i= 0; i < array_size; i++) {
			for (int j = 0; j < array_size; j++) {				
				//System.out.format("%.2f ", betweenness[i][j]);
				if (betweenness[i][j] > max_betweenness) {
					max_betweenness = betweenness[i][j];
					max_betweenness_from = i;
					max_betweenness_to = j;
				}
			}
			//out.println();
			//System.out.println();
		}
		//out.println("Max betweenness = " + max_betweenness + 
		//		" from " + max_from + " to " + max_to);   
		System.out.println("Max betweenness = " + max_betweenness + 
						" from " + max_betweenness_from + " to " + max_betweenness_to);   
		System.out.println();
	}

	
	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 * 
	 * Done for a previous assignment.
	 * 
	 * An egonet is the single 'center' vertex provided as an argument,
	 * and all of its immediate neighbors, as well as all edges that 
	 * exist in the larger graph, between members of the egonet.
	 * 
	 * It does not include any edges from a member of the egonet
	 * to non-egonet vertices.
	 */
	
	@Override
	public Graph getEgonet(int center) {
		
		Graph newGraph = new CapGraph();
		
		if (!vertices.contains(center)) {
			/* If the vertex center is not present 
			 * in the original graph, this method should return an empty Graph.
			 */
			return newGraph;
		}
		else {
			newGraph.addVertex(center);
			HashSet<Integer> friends = getEdges(center);
			for (Integer v : friends) {
				/* add each friend to egonet */
				newGraph.addVertex(v);
			}
			CapGraph cGraph = (CapGraph)newGraph;
			for (Integer from : cGraph.getVertices()) {	
				/* go through each friend's edges in larger graph */
				/* add to egonet if both to & from are in newGraph (center or friends) */
				for (Integer to : getEdges(from)) { /* edges in larger graph */
					if (cGraph.getVertices().contains(to)) {
						newGraph.addEdge(from, to);
					}
				}
			}
			return newGraph;
		}
	}
	
	/*
	 * Returns the transpose (reverse) of the input graph.
	 * Used to detect strongly connected components.
	 * 
	 */
	
	CapGraph transpose() {
		CapGraph Grev = new CapGraph();
		
		/* must add all vertices first -- otherwise addEdges will get confused */
		
		for (int v : getVertices()) {		
			Grev.addVertex(v);
		}
		
		/* now that Grev has all its vertices, we can add the edges */
		
		for (int v : getVertices()) {
			for (int to : getEdges(v)) {
				Grev.addEdge(to, v);
			}
		}
				
		return Grev;
	}

	/* 
	 * Outer function to setup a depth first search of a graph 
	 */
	
	Stack<Integer> dfs(CapGraph g, Stack<Integer> vertex_stack, 
			HashSet<Integer> leaders, List<Graph> graphList) {

		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		Integer v;
		while (!vertex_stack.isEmpty()) {
			v = vertex_stack.pop();
			if (!visited.contains(v)) {
				// 2nd call: each v is the root of a SCC
				CapGraph newGraph = new CapGraph();
				leaders.add(v);
				newGraph.addVertex(v);
				
				dfs_visit(g, v, visited, finished, newGraph);		
				graphList.add(newGraph);
			}
		}
				
		return finished;
	}

	/* 
	 * Recursively called function that does the core work of depth first search 
	 */
	
	void dfs_visit(CapGraph g, Integer v, 
			HashSet<Integer> visited, Stack<Integer> finished, CapGraph newGraph) {

		visited.add(v);
		for (Integer n: g.getEdges(v)) {
			if (!visited.contains(n)) {
				newGraph.addVertex(n);
				newGraph.addEdge(n, v); // need transpose
				dfs_visit(g, n, visited, finished, newGraph);
			}
		}
				
		finished.add(v);		
	}


	
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 * Get strongly connected components of a graph using Kosaraju's algorithm:
	 * 		1. Perform a DFS of G and number the vertices in order of completion of the recursive calls.
	 * 		2. Construct a new directed graph Grev by reversing the direction of every arc in G.
	 * 		3. Perform a DFS on Grev, starting the search from the highest numbered vertex according to the 
	 * 		   numbering assigned at step 1. If the DFS does not reach all vertices, start the next DFS 
	 * 		   from the highest numbered remaining vertex.
	 * 		4. Each tree in the resulting spanning forest is a strong component of G.
	 * 
	 */
	@Override
	public List<Graph> getSCCs() {
		//HashSet<Integer> visited = new HashSet<Integer>();
		HashSet<Integer> leaders = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		List<Graph> SCCs = new ArrayList<Graph>();
		Stack<Integer> vertex_stack = new Stack<Integer>();	
		
		/* put vertices into a stack */		
		/* order does not matter */
		for (Integer v: getVertices()) {
			vertex_stack.push(v);
		}		
		
		/* first call: dfs on original graph, vertices in any order */
		finished = dfs(this, vertex_stack, leaders, SCCs); 
		
		CapGraph grev = transpose();
		
		/* second call: dfs on transposed graph, 
		vertices in reverse order of finish */
		leaders.clear();
		SCCs = new ArrayList<Graph>();
		dfs(grev, finished, leaders, SCCs);
		
		return SCCs;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return getEdges();
	}
	
	/*
	 * AllPairsShortestPaths uses the Floyd-Warshall algorithm to determine the
	 * length and number of ALL shortest paths between each pair of vertices in a CapGraph.
	 * It returns a 2D array of listOfPaths, which contains all of the shortest
	 * paths from i to j.
	 * 
	 * My ultimate goal is to find betweenness of edges, which involves knowing what 
	 * fraction of shortest paths between pair (i,j) use a particular edge.
	 */
	
	public Integer[][] allPairsShortestPaths() {
		
		/* 
		 * Create and initialize to MAX (for "no edge") 2D array of shortest path lengths.
		 */
				
		int max_vertex = getMaxVertex();
		
		dist = new Integer[max_vertex+1][max_vertex+1];	  // shortest distance between pair (i, j)
		next = new nextEntry[max_vertex+1][max_vertex+1]; // for reconstructing path(s) between i & j
		count = new Integer[max_vertex+1][max_vertex+1];  // number of equal shortest paths between i & j	

		for (Integer v= 0; v <= max_vertex; v++)  {
			for (Integer w = 0; w <= max_vertex; w++) {				
					dist[v][w] = Integer.MAX_VALUE;   
					next[v][w] = new nextEntry();
					count[v][w] = 0;
			}			
		}
		        
		/* Then separately set each existing self edge to zero, after
		 * checking that a vertex of that number exists in the graph.
		 * This accounts for the fact that there are integers along 0 .. max_vertex
		 * that do not correspond to a vertex in the graph.  Those integers
		 * keep a distance of MAX_VALUE on the diagonal.
		 */
		
		for (Integer v = 0; v <= max_vertex; v++)  {		
			if (getVertices().contains(v)) {
				dist[v][v] = 0;
				count[v][v] = 0;
			}
		}
		       
		/* Now that array is initialized to infinity (or zero for self edges),
		 * fill it in with a distance of 1 for each edge (v,w) in the graph.
		 * This is an unweighted graph, so each edge has a weight of 1.
		 * 
		 * Have to iterate through edges in order, can't use the HashSet
		 * returned by getEdges() since the order of iteration through a HashSet
		 * is not guaranteed.
		 */
		
		for (Integer v = 0; v <= max_vertex; v++) {
			for (Integer w = 0; w <= max_vertex; w++) {
				if (isAnEdge(v,w)) {
					dist[v][w] = 1;
					next[v][w].addNextEntry(w);
					count[v][w] = 1;
				}
			}
		}
		
		
		/* Now comes the decision making part of the algorithm.  Dynamically build up 
		 * shortest path lengths for each pair of edges, by computing shortest_path(i,j,k)
		 * for all (i,j) pairs for increasing values of k.  For each new value of k, 
		 * s_p(i,j,k+1) = min(s_p(i,j,k), (s_p(i,k+1,k) + s_p(k+1,j,k)))
		 * This part is O(v^3)
		
		 * MAX_VALUE + 1 = very large negative number!  Check for MAX_VALUE before
		 * doing arithmetic with it.
		 */
		
		for (Integer k = 0; k <= max_vertex; k++) {
			for (Integer i = 0; i <= max_vertex; i++) {
				for (Integer j = 0; j <= max_vertex; j++) {		
					if ((dist[i][k] != Integer.MAX_VALUE) &&
						 (dist[k][j] != Integer.MAX_VALUE)) {
						if (dist[i][j] > (dist[i][k] + dist[k][j])) {
							/* 
							 * Going through k makes a shorter path
							 */
							dist[i][j] = dist[i][k] + dist[k][j];
							next[i][j].clearNext();
							for (Integer newnext : next[i][k].getNextVertices()){
								if (newnext != null) {
									next[i][j].addNextEntry(newnext);
								}
							}
							count[i][j] = count[i][k] * count[k][j]; 
						}	
						else  if ((dist[i][j] == (dist[i][k] + dist[k][j])) &&
								(k != i) && (k != j)) { // not either pair the same
							/*
							 * Going through k makes a path that is equally short
							 */
							for (Integer newnext : next[i][k].getNextVertices()) {
								if (newnext != null) {
									next[i][j].addNextEntry(newnext);
								}
							}
							count[i][j] += count[i][k] * count[k][j]; 
						}
					}				
				}
			}
		}	
		return dist;
	}
	
	


	
	/* 
	 * Prints all shortest paths between each pair of vertices i and j.  If 
	 * there are multiple, equally short, paths between i and j, prints all of them.
	 * Calls getShortestPaths() for each i & j.
	 * (non-Javadoc)
	 * @see graph.Graph#printShortestPaths(int)
	 */
	
	public void printShortestPaths(int array_size) {
		
		shortest_paths_array = new listOfPaths[array_size][array_size];
		
		listOfPaths shortest_Paths = new listOfPaths();
		//System.out.println("All shortest paths for each pair: array size = " + array_size);
		
		for (Integer i= 0; i < array_size; i++) {
			for (Integer j = 0; j < array_size; j++) {	
				if (count[i][j] > 0) {
					shortest_Paths = getShortestPaths(i, j, i,j, array_size);
					shortest_paths_array[i][j] = shortest_Paths;
					//shortest_Paths.print();
				}
			}
		}
	}	

	
	/*
	 * Implements the base case of the recursive function getShortestPaths().
	 * If there is only one shortest path between two vertices i and j, follow the
	 * entries in the next array to reconstruct it.
	 * 
	 * This function could also be used to reconstruct the shortest path
	 * between i and j, if it is acceptable to return any shortest path.
	 */
	
	public Path getSingleShortestPath(Integer i, Integer j, int array_size) {
		
		Path mypath = new Path();
				
		if (next[i][j] == null) {
			return mypath;
		}
		
		int u = i;
		int v = j;

		mypath.add(u);
		
		while (u != v) {
			if ((Integer)u == null) {
				throw new NullPointerException();
			}
			else {
				ArrayList<Integer> next_array = next[u][v].getNextVertices();
				Integer next = next_array.get(0);
				u = next;
				mypath.add(u);
			}
		}
		return mypath;		
	}


	/* 
	 * Returns a list of ALL shortest paths between i and j.  If there is only one
	 * path, calls getSingleShortestPath().  If there are multiple paths, 
	 * this function calls itself recursively to follow each possible 'next' vertex
	 * in the path to the j.
	 */

	
	public listOfPaths getShortestPaths(Integer orig_i, Integer orig_j, Integer i, Integer j, int array_size) {

		listOfPaths  allPaths = new listOfPaths();
		listOfPaths  paths_I_K = new listOfPaths();
		listOfPaths  paths_K_J = new listOfPaths();
		Path mypath = new Path();

		if (count[i][j] == 0) { // no path at all
			allPaths.add(mypath);
		}
		else if (count[i][j] == 1) { 
			// only one path, but that doesn't mean it's an edge 
			mypath = getSingleShortestPath(i, j, array_size); 
			allPaths.add(mypath);	
		}					
		else { 
			// has multiple shortest paths					
			Integer num = next[i][j].getNumberOfNextVertices(); 

			for (int n = 0; n < num; n++) {
				Integer k = next[i][j].getNextVertices(n); // use getNextVertices()
				if (k != null) {
					paths_I_K = getShortestPaths(orig_i, orig_j, i, k, array_size); 
					paths_K_J = getShortestPaths(orig_i, orig_j, k, j, array_size);
					for (Path path1 : paths_I_K.get_list()) { 
						for (Path path2 : paths_K_J.get_list()) {
							mypath = path1.concatenate(path2);
							allPaths.add(mypath);
						}
					}
				}
			}
		}
		
		
		return allPaths;
	}	

	/*
	 * The "betweenness of an edge" represents the amount of "flow" that travels 
	 * through that edge in a graph during a traversal of all shortest paths.
	 * It is a measure of how critical an edge is to connections within the graph.
	 * 
	 * First, initialize the betweenness array with zeroes.
	 * 
	 * Then, traverse all shortest paths between each pair of vertices (orig_i, orig_j).
	 * There may be multiple, equal length, shortest paths between (orig_i, orig_j)!
	 * For each edge (i,j) along each shortest path, add a betweenness score of
	 * 1/count[orig_i][orig_j].  
	 * 
	 * So, if a pair of vertices (orig_i, orig_j) only has one shortest path between
	 * them, each edge in that path gets 1 added to its betweenness score by that
	 * path.  But if (orig_i, orig_j) has multiple shortest paths, and (i,j) is along
	 * only half of those paths, then 1/2 gets added to (i,j)'s betweenness score 
	 * by that original pair.
	 * 
	 * (non-Javadoc)
	 * @see graph.Graph#calculateBetweenness(int)
	 */
	
	public void calculateBetweenness(int array_size) {
		betweenness = new double[array_size][array_size];

		// initialize betweenness
		for (Integer i= 0; i < array_size; i++) {
			for (Integer j = 0; j < array_size; j++) {	
				betweenness[i][j] = 0;
			}
		}
		
		for (Integer i= 0; i < array_size; i++) {
			for (Integer j = 0; j < array_size; j++) {	
				if (count[i][j] > 0) {					
					listOfPaths pathlist = shortest_paths_array[i][j];
					for (Path path :pathlist.get_list())  {
						add_betweenness(path, i, j);
					}
				}	
			}
		}
	}

	/*
	 * Adds to the "betweenness" score of each vertex in the path passed in. 
	 * That path is a shortest path between vertices orig_i and orig_j.
	 * (There may be one, or more than one shortest path between them).
	 * 
	 * Traverse the path and add some betweenness for each edge.  See
	 * the comments above calculateBetweenness() for an explanation of 
	 * "betweenness".
	 */
	
	private void add_betweenness(Path path, Integer orig_i, Integer orig_j) {
		ArrayList<Integer> pathlist = path.get_path(); // or get by path entry
		//path.print();
		for (int k= 0; k < pathlist.size()-1; k++) {
			double newval = (double)(1.0/count[orig_i][orig_j]);
			int i = pathlist.get(k);
			int j = pathlist.get(k+1);
			betweenness[i][j] += newval; 
		}
	}
}
        

