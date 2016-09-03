package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.io.PrintWriter;

/*
 * This file was originally provided as a stub by the instructors.
 * Some of the methods were defined by the instructors and
 * others I added.
 */
public interface Graph {
    /* Creates a vertex with the given number. */
    public void addVertex(int num);
    
    /* Creates an edge from the first vertex to the second. */
    public void addEdge(int from, int to);
    
    /* Removes an existing edge from the first vertex to the second. */
    public void removeEdge(int from, int to);
 
    /* Returns the vertices connected to the 'from' vertex. */
	public HashSet<Integer> getEdges(int from);

	/* Returns true if (from, to) is an edge existing in the graph. */
	public boolean isAnEdge(int from, int to);

    /* Returns one vertex in the edge with maximum betweenness -- or one of them if a tie */
    public int getEdgeWithMaxBetweennessFrom();
 
    /*  Returns the other vertex in the edge with maximum betweenness -- or one of them if a tie.
    	Always properly paired with the vertex returned by get...BetweennessFrom() */
    public int getEdgeWithMaxBetweennessTo();

    /* Finds the egonet centered at a given node. */
    public Graph getEgonet(int center);

    /* Returns all SCCs in a directed graph. Recall that the warm up
     * assignment assumes all Graphs are directed, and we will only 
     * test on directed graphs. */
    public List<Graph> getSCCs();
    
    /* Return the graph's connections in a readable format. 
     * The keys in this HashMap are the vertices in the graph.
     * The values are the nodes that are reachable via a directed
     * edge from the corresponding key. 
	 * The returned representation ignores edge weights and 
	 * multi-edges.  */
    
    public Integer[][] allPairsShortestPaths();
    
    //public List<Integer> shortest_path(Integer i, Integer j, Integer k);
    
    //public void printCount(int array_size, PrintWriter out);
    public void printCount(int array_size);
    
    public void printShortestPaths(int array_size);
    
    public void calculateBetweenness(int array_size);
    
    public void printBetweenness(int array_size);
    
    public HashMap<Integer, HashSet<Integer>> exportGraph();
} 
