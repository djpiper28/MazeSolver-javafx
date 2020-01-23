package dannypiper.mazesolver.graphSolve;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class DepthFirst extends SolveInterface {

	private boolean[] visited;
	private boolean finished;
	private int graphHeight, graphWidth;

	private void dumpPath() {
		try {
			ImageIO.write((new GraphToImage(graphHeight, graphWidth, super.path)).getImage(), "PNG",
					new File("DUMP/Image" + System.currentTimeMillis() + "-" + super.path.size() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DepthFirst(List<Arc>[] IndexedAdjacencyList, EntranceExit entranceExit, int graphWidth, int graphHeight) {
		super(IndexedAdjacencyList, entranceExit);
		visited = new boolean[super.IndexedAdjacencyList.length];

		// Init visited
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}

		finished = false;
		this.graphWidth = graphWidth;
		this.graphHeight = graphHeight;
	}

	private void explore(Arc arc) {
		super.path.push(arc);
		visited[arc.startingNode] = true;
		// dumpPath();

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

		if (finished) {
			return;
		}

		super.path.pop();
	}

	@Override
	public List<Arc> solve() {		
		System.out.println("Started Depth First On Adj List of Size " + super.IndexedAdjacencyList.length);
		explore(super.IndexedAdjacencyList[super.entranceExit.getEntrance()].get(0));
		System.out.println("Finished Depth First, Path Length " + super.path.size());
		// (new File("DUMP")).mkdir();
		if (!finished) {
			System.out.println("No path found");
		}
		return super.path;
	}

}
