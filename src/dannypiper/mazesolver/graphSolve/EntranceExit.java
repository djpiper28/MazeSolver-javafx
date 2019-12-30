package dannypiper.mazesolver.graphSolve;

public class EntranceExit {

	private Arc exit;
	private Arc entrance;

	public EntranceExit(Arc entrance, Arc exit) {
		setEntrance(entrance);
		setExit(exit);
	}

	public Arc getEntrance() {
		return entrance;
	}

	public Arc getExit() {
		return exit;
	}

	public void setEntrance(Arc entrance) {
		this.entrance = entrance;
	}

	public void setExit(Arc exit) {
		this.exit = exit;
	}

}
