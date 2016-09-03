package graph;

import java.util.ArrayList;

/*
 * The Path class implements a single path from v1 to v2.
 */

public class Path {
	ArrayList<Integer> path;
	
	// null constructor
	
	public Path() {
		path = new ArrayList<Integer>();
	}
	
	// constructor with list of vertices
	public Path(ArrayList<Integer> list) {
		path = list;
	}

	// add next vertex to path
	public void add(Integer v) {
		path.add(v);
	}
	
	public ArrayList<Integer> get_path() {
		return path;
	}
	
	public void print() {
		for (Integer v:get_path()) {
			System.out.print(v + " ");
		}
		System.out.println();
	}
	
	/*
	 *  Connect path from v to w, to path from w to x,
	 *  to make path from v to x through w
	 */
	
	
	public Path concatenate(Path path) {
		
		ArrayList<Integer> first_path = this.get_path();
		ArrayList<Integer> second_path = path.get_path();
		ArrayList<Integer> long_path = new ArrayList<Integer>();
						
		long_path.addAll(first_path.subList(0, first_path.size()-1));
		long_path.addAll(second_path);
				
		// make long_path into Path
		Path newPath = new Path(long_path);
		return newPath;
	}
	
}


