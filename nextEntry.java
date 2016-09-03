package graph;

import java.util.ArrayList;

/*
 * The nextEntry class is used to reconstruct a path or paths from one vertex to another.
 * nextVertices holds the list of POSSIBLE next vertices for reconstructing a path,
 * when there are MULTIPLE shortest paths.
 */

public class nextEntry {
	ArrayList<Integer> nextVertices;
	

	/* constructor */
	public nextEntry() {
		nextVertices = new ArrayList<Integer>();
 	}
	
	public Integer getNumberOfNextVertices() {
		return nextVertices.size();
	}
	
	public boolean addNextEntry(Integer next) {

		if (!nextVertices.contains(next)) {
			nextVertices.add(next);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ArrayList<Integer> getNextVertices() {
		return nextVertices;
	}
	
	public Integer getNextVertices(int n) {
		return nextVertices.get(n);
	}
	
	
	public void clearNext() {
		nextVertices.clear();
	}
	
}
