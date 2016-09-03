package graph.grader;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import graph.CapGraph;
import graph.Graph;
//import graph.listOfPaths;
//import graph.nextEntry;
import util.GraphLoader;
import java.util.List;
import java.util.HashSet;

public class AllPairsShortestPaths {

	String feedback;
	
	@Test
	public void test() {
		try {
			
			/* DO NOT USE "data/facebook_ucsd.txt" -- IT IS TOO LARGE FOR THIS ALGORITHM
			 * WITH MY COMPUTER AND THE JAVA HEAP !!!
			 * Update together: 
			 * 1. data filename 
			 * 2. output filename
			 * 3. array_size.
			 */
			
			Graph bigGraph = new CapGraph();
			GraphLoader.loadGraph(bigGraph, "data/facebook_1000.txt");
			//GraphLoader.loadGraph(bigGraph, "data/smaller_test_graph");
			//GraphLoader.loadGraph(bigGraph, "data/extended_small_graph.txt");
			//GraphLoader.loadGraph(bigGraph, "data/toy_graph.txt");
			//int array_size = 2000;
			int array_size = 1000;
			//int array_size = 15;  // small
			//int array_size = 8;     // toy
			//int array_size = 6;   // extended smaller
			//int array_size = 4;   //smaller
        
			PrintWriter out;
			out = new PrintWriter(new FileWriter("data/APSP_answers/APSP_1000.txt"));
		      
			//out.println("Export graph:");
			//out.println(bigGraph.exportGraph());
			
			//System.out.println("Original graph:");
			out.println("Original graph:");

			printGraphInfo(bigGraph, out, array_size);
	        
	        /* Changes */
			
			int from = bigGraph.getEdgeWithMaxBetweennessFrom();
			int to = bigGraph.getEdgeWithMaxBetweennessTo();

			HashSet<Integer> from_neighbors = bigGraph.getEdges(from);
			HashSet<Integer> to_neighbors = bigGraph.getEdges(to);
			
			//System.out.println("Looking for backup edges");
			int[]pair1 = new int[2]; 
			boolean pair1_assigned = false;
			int[]pair2 = new int[2];
			boolean pair2_assigned = false;
			
			for (int a: from_neighbors) {
				for (int b: to_neighbors) {
					if (!bigGraph.isAnEdge(a, b) &&  (a != b) &&
							(a != from) && (a != to) && (b != from) && (b != to) &&
							(!pair2_assigned)) {
						System.out.println("Candidate edge "+a+" "+b);
						if (!pair1_assigned) {
							System.out.println("Assigned to pair 1");
							pair1[0] = a;
							pair1[1] = b;
							pair1_assigned = true;
							break;
						}
						else if (!pair2_assigned) {
							if ((a != pair1[0]) && (a != pair1[1]) && (b != pair1[0]) && (b != pair1[1])) {
								pair2[0] = a;
								pair2[1] = b;
								System.out.println("Assigned to pair 2");
								pair2_assigned = true;
								break;
							}
						}
					}
				}
			}

			
			System.out.println("Removing edge with highest betweenness: "+from+" "+to);
			//System.out.println("Removing edge with highest betweenness: "+from+" "+to);
			
			bigGraph.removeEdge(from, to);
			bigGraph.removeEdge(to, from);
			
			printGraphInfo(bigGraph, out, array_size);

			System.out.println("Adding it back in, and adding some edges between its neighbors");
	        System.out.println("Adding an edge: ("+pair1[0]+"  "+pair1[1]+") & ("+pair1[1]+" "+pair1[0]+"):");
	        System.out.println("Adding an edge: ("+pair2[0]+"  "+pair2[1]+") & ("+pair2[1]+" "+pair2[0]+"):");
	        
	        //out.println("Adding it back in, and adding some edges between its neighbors");
	        //out.println("Adding an edge: ("+pair1[0]+"  "+pair1[1]+") & ("+pair1[1]+" "+pair1[0]+"):");
	        //out.println("Adding an edge: ("+pair2[0]+"  "+pair2[1]+") & ("+pair2[1]+" "+pair2[0]+"):");

	        bigGraph.addEdge(from, to);
	        bigGraph.addEdge(to, from);
	        
	        bigGraph.addEdge(pair1[0], pair1[1]);
	        bigGraph.addEdge(pair1[1], pair1[0]);
	        
	        bigGraph.addEdge(pair2[0], pair2[1]);
	        bigGraph.addEdge(pair2[1], pair2[0]);
	        
	        printGraphInfo(bigGraph, out, array_size);
	        

	        System.out.println("Removing that original max betweenness edge again :");
	        //out.println("Removing that original max betweenness edge again :");

	        bigGraph.removeEdge(from, to);
	        bigGraph.removeEdge(to, from);
	        
	        printGraphInfo(bigGraph, out, array_size);
        
			out.close();
        
		}
		catch (Exception e) {
			feedback = "An error occurred during runtime.\n" + feedback + "\nError during runtime: " + e;
			e.printStackTrace();
		
		}
	}
	
	private void printGraphInfo(Graph bigGraph, PrintWriter out, int array_size) {
		Integer[][] dist = bigGraph.allPairsShortestPaths();
		
	  //out.println("Distance between all pairs");
	 
		//System.out.println("Distance between all pairs");
		//for (int i= 0; i < array_size; i++) {
		//	for (int j = 0; j < array_size; j++) {
		//		if (dist[i][j] == Integer.MAX_VALUE) {
		//			out.print("MAX ");
		//			//System.out.print("MAX ");
		//		}
		//		else {
		//			out.print(dist[i][j] + " ");
		//			//System.out.print(dist[i][j] + " ");
		//		}
		//	}
		//	out.println();   
			//System.out.println();
		//}
		//System.out.println();
		//out.println();
		
		
		/* print count of shortest paths */
		bigGraph.printCount(array_size);
		
		
		bigGraph.printShortestPaths(array_size);
		
		bigGraph.calculateBetweenness(array_size);			
		bigGraph.printBetweenness(array_size);
		
        List<Graph> sccList = bigGraph.getSCCs();
        //System.out.println("Number of SCCs = " + sccList.size());
        out.println("Number of SCCs = " + sccList.size());
        
        for (Graph scc: sccList) {
        	//System.out.print("SCC: " + scc.exportGraph());
        	//System.out.println();
        	out.print("SCC: " + scc.exportGraph());
        	out.println();
        }
        //System.out.println();
        out.println();
        
	}
}
