package dannypiper.mazesolver.graphSolve;

import java.util.List;

public class Dijkstras extends SolveInterface {

	private int[] tempWeight;
	private int[] finalWeight;

	public Dijkstras(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit) {
		super(IndexedAdjacencyList, entranceExit);

		this.tempWeight = new int[super.IndexedAdjacencyList.length];
		this.finalWeight = new int[super.IndexedAdjacencyList.length];

		// Init weight counters
		for (int i = 0; i < super.IndexedAdjacencyList.length; i++) {
			this.tempWeight[i] = -1;
			this.finalWeight[i] = -1;
		}

		this.tempWeight[entranceExit.getEntrance()] = 0;
		this.finalWeight[entranceExit.getEntrance()] = 0;
	}

	@Override
	public List<Arc> solve() {
		// TODO: code dijkstras
		return null;
	}

}
