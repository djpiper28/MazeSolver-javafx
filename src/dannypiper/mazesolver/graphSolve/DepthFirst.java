package dannypiper.mazesolver.graphSolve;

import java.util.List;
import java.util.Queue;

public class DepthFirst extends SolveInterface {

	private boolean[] visited;
	private boolean finished;

	public DepthFirst(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit) {
		super(IndexedAdjacencyList, entranceExit);
		visited = new boolean[super.IndexedAdjacencyList.length];

		// Init visited
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		
		finished = false;
	}

	private void explore(Arc arc) {
		super.path.push(arc);
		visited[arc.startingNode] = true;

		if (arc.endingNode == super.entranceExit.getExit()) {
			System.out.println("Arc: " + arc.toString());
			System.out.println("Exit: " + super.entranceExit.getExit());
			finished = true;
			return;
		}
		
		for (Arc a : super.IndexedAdjacencyList[arc.endingNode]) {		
			if (!visited[a.endingNode] && !finished) {
				explore(a);
			}
		}
		
		if(finished) {
			return;
		}

		super.path.pop();
	}

	@Override
	public List<Arc> solve() {
		System.out.println("Started Depth First On Adj List of Size " + super.IndexedAdjacencyList.length);
		explore(super.IndexedAdjacencyList[super.entranceExit.getEntrance()].get(0));
		System.out.println("Finished Depth First, Path Length " + super.path.size());
		return super.path;
	}

}
