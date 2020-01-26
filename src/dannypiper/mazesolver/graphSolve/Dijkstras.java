package dannypiper.mazesolver.graphSolve;

import java.util.List;

public class Dijkstras extends SolveInterface {

	private int[] tempLabel;
	private int[] finalLabel;

	public Dijkstras(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit) {
		super(IndexedAdjacencyList, entranceExit);

		this.tempLabel = new int[super.IndexedAdjacencyList.length];
		this.finalLabel = new int[super.IndexedAdjacencyList.length];

		// Init weight counters
		for (int i = 0; i < super.IndexedAdjacencyList.length; i++) {
			this.tempLabel[i] = -1;
			this.finalLabel[i] = -1;
		}

		this.tempLabel[entranceExit.getEntrance()] = 0;
		this.finalLabel[entranceExit.getEntrance()] = 0;
	}

	private List<Arc> getNeighbours(int endingNode) {
		return super.IndexedAdjacencyList[endingNode];
	}

	@Override
	public List<Arc> solve() {
		// TODO: code dijkstras
		while (!super.path.isEmpty()) {

		}
		return null;
	}

}
