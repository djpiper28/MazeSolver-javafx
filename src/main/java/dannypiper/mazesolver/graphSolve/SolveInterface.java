package dannypiper.mazesolver.graphSolve;

import java.util.LinkedList;
import java.util.List;

public abstract class SolveInterface {

	protected List<Arc>[] IndexedAdjacencyList;
	protected EntranceExit entranceExit;
	protected LinkedList<Arc> path;

	public SolveInterface(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit) {
		this.path = new LinkedList<Arc>();
		this.IndexedAdjacencyList = IndexedAdjacencyList;
		this.entranceExit = entranceExit;

		assert (IndexedAdjacencyList.length > 0);
	}

	public abstract List<Arc> solve();
	// The method is abstract meaning that it cannot be called without being
	// overridden
	// similar to virtual in lazarus

}
