package dannypiper.mazesolver.imageSolve;

public class solver implements Runnable {
	public int YMin;
	public int YMax;
	public Boolean finished;

	public solver() {
		this.finished = false;
	}
	
	private int count(int x, int y) {
		int count = 0;
		if (mazeSolver.graphArray[x + 1][y]) {
			count++;
		}
		if (mazeSolver.graphArray[x - 1][y]) {
			count++;
		}
		if (mazeSolver.graphArray[x][y + 1]) {
			count++;
		}
		if (mazeSolver.graphArray[x][y - 1]) {
			count++;
		}
		return count;
	}

	@Override
	public void run() {
		boolean changedThisPass = true;
		while (changedThisPass) {
			changedThisPass = false;

			for (int y = YMin; y < YMax; y++) {
				for (int x = 1; x < mazeSolver.imageWidth - 2; x++) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
						if (count(x, y) == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeSolver.mazeImage.setRGB(x, y, mazeSolver.path);
						}
					}
				}
			}

			changedThisPass = false;
			for (int y = YMax; y > YMin; y--) {
				for (int x = 1; x < mazeSolver.imageWidth - 2; x++) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
						if (count(x, y) == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeSolver.mazeImage.setRGB(x, y, mazeSolver.path);
						}
					}
				}
			}

			changedThisPass = false;
			for (int y = YMin; y < YMax; y++) {
				for (int x = mazeSolver.imageWidth - 2; x > 0; x--) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
						if (count(x, y) == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeSolver.mazeImage.setRGB(x, y, mazeSolver.path);
						}
					}
				}
			}

			changedThisPass = false;
			for (int y = YMax; y > YMin; y--) {
				for (int x = mazeSolver.imageWidth - 2; x > 0; x--) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
						if (count(x, y) == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeSolver.mazeImage.setRGB(x, y, mazeSolver.path);
						}
					}
				}
			}
		}
		this.finished = true;
		System.out.println("Thread finished.");
	}
}