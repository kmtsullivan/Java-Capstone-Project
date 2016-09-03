package graph;

import java.util.ArrayList;

/*
 * listOfPaths defines an ArrayList of paths from one vertex to another.
 * A path is just an ArrayList of vertices.  I gave listOfPaths its own
 * class for readability (to avoid having a 2D array of ArrayLists of ArrayLists!).
 */

public class listOfPaths {
	ArrayList<Path> list;
	
	public listOfPaths() {
		list = new ArrayList<Path>();
	}
	
	public void add(Path p) {
		list.add(p);
	}
	public ArrayList<Path> get_list() {
		return list;
	}
	
	public void print() {
		for (Path path : get_list()) {
			for (Integer v:path.get_path()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
	}
}

