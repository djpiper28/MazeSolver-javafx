package dannypiper.mazesolver.graphSolve;

public class EntranceExit {

	private int exitNode;
	private int entranceNode;

	public EntranceExit(int entrance, int exit) {
		setEntrance(entrance);
		setExit(exit);
	}

	public int getEntrance() {
		return entranceNode;
	}

	public int getExit() {
		return exitNode;
	}

	public void setEntrance(int entrance) {
		this.entranceNode = entrance;
	}

	public void setExit(int exit) {
		this.exitNode = exit;
	}

}
