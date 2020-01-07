package dannypiper.mazesolver.graphSolve;

import java.util.List;

public class DepthFirst extends SolveInterface {

	private boolean[] visited;

	public DepthFirst(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit) {
		super(IndexedAdjacencyList, entranceExit);
		visited = new boolean[super.IndexedAdjacencyList.length];

		// Init visited
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
	}

	private void explore(Arc arc) {
		super.path.push(arc);
		visited[arc.startingNode] = true;

		if (arc.endingNode == super.entranceExit.getExit()) {
			return;
		}

		if (!visited[arc.endingNode]) {
			for (Arc a : super.IndexedAdjacencyList[arc.endingNode]) {				
				if (!visited[a.endingNode]) {
					explore(a);
				}
			}
		}

		super.path.pop();
	}

	@Override
	public List<Arc> solve() {
		System.out.println("Started Depth First On Adj List of Size " + super.IndexedAdjacencyList.length);
		explore(super.IndexedAdjacencyList[super.entranceExit.getEntrance()].get(0));
		return super.path;
	}

}
