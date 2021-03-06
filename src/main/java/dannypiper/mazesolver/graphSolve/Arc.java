package dannypiper.mazesolver.graphSolve;

public class Arc {
	public int startingNode;
	public int endingNode;

	public Arc(final int startingNode, final int endingNode) {
		this.startingNode = startingNode;
		this.endingNode = endingNode;

		assert (startingNode > 0 || endingNode > 0);
	}

	@Override
	public String toString() {
		return "Arc [startingNode=" + startingNode + ", endingNode=" + endingNode + "]";
	}
}
