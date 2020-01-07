package dannypiper.mazesolver.graphSolve;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class GraphSolve implements Runnable {

	private BufferedImage MazeImage;
	private List<Arc>[] IndexedAdjacencyList;
	private EntranceExit entranceExit;
	private SolveInterface solver;
	private SolveTypeEnum solveType;
	private long timeToSolve;
	private File outputFile;
	private int graphWidth, imageHeight;
	private int graphHeight, imageWidth;

	public GraphSolve(SolveTypeEnum solveType, File mazeImageFile, File outputFile) throws IOException, DimensionError {
		this.solveType = solveType;
		this.MazeImage = ImageIO.read(mazeImageFile);
		this.outputFile = outputFile;

		this.imageHeight = this.MazeImage.getHeight();
		this.imageWidth = this.MazeImage.getWidth();

		this.graphHeight = (this.imageHeight - 1) / 2;
		this.graphWidth = (this.imageWidth - 1) / 2;

		ImageToGraph imageMapper = new ImageToGraph(this.MazeImage);

		System.out.println("Mapping image to graph");
		this.IndexedAdjacencyList = imageMapper.mapImageToGraph();
		System.out.println("Mapped image to graph");

		System.out.println("Finding entrance and exit");
		this.entranceExit = imageMapper.getEntranceExit();
		System.out.println(
				"Found entrance and exit at: " + this.entranceExit.getEntrance() + "," + this.entranceExit.getExit());

		switch (this.solveType) {
		case DEPTH_FIRST:
			this.solver = new DepthFirst(this.IndexedAdjacencyList, this.entranceExit);
			break;
		case DIJKSTRAS:
			System.out.println("NOT WORKING");
			this.solver = new Dijkstras(this.IndexedAdjacencyList, this.entranceExit);
			break;
		}
	}

	public BufferedImage getOutput() {
		return MazeImage;
	}

	@Override
	public void run() {
		try {
			solveMazeAndSaveImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Arc> solve() {
		long time = System.currentTimeMillis();

		List<Arc> path = this.solver.solve();

		this.timeToSolve = System.currentTimeMillis() - time;
		return path;
	}

	public void solveMazeAndSaveImage() throws IOException {
		// Solve maze
		System.out.println("Solving");
		long time = System.currentTimeMillis();
		List<Arc> path = this.solve();
		System.out.println("Solved in " + (System.currentTimeMillis() - time));

		// Create graph from path
		System.out.println("Mapping to image");
		time = System.currentTimeMillis();

		GraphToImage graphToImage = new GraphToImage(graphHeight, graphWidth, path);
		BufferedImage pathImage = graphToImage.getImage();

		System.out.println("Mapped to image in " + (System.currentTimeMillis() - time));

		// Merge with image above
		System.out.println("Merging path and image ");
		time = System.currentTimeMillis();

		for (int x = 0; x < this.imageWidth; x++) {
			for (int y = 0; y < this.imageHeight; y++) {
				if (pathImage.getRGB(x, y) == GraphToImage.purple) {
					this.MazeImage.setRGB(x, y, GraphToImage.purple);
				}
			}
		}

		System.out.println("Merged to image in " + (System.currentTimeMillis() - time) + "\nSaving image...");

		// Save image

		ImageIO.write(this.MazeImage, "PNG", outputFile);
		System.exit(0);
	}
}
