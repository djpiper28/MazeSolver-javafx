package dannypiper.mazesolver.graphSolve;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageToGraph {

	private int white;
	private BufferedImage image;

	public ImageToGraph(BufferedImage inputImage) throws DimensionError {
		if (validateDimension(inputImage.getWidth()) && validateDimension(inputImage.getHeight())) {
			this.image = inputImage;
			this.white = this.image.getRGB(1, 1);
		} else {
			System.out.println("Dimension error.");
			throw new DimensionError();
		}
	}

	public EntranceExit getEntranceExit() {
		int entrance = -1;
		int exit = -1;

		int width = image.getWidth();
		int height = image.getHeight();
		int graphWidth = (width - 1) / 2;
		int graphHeight = (height - 1) / 2;

		boolean exitOrEntrance = false;

		for (int y = 1; (y < height) && (entrance == -1 || exit == -1); y += 2) {
			// y, x = 0
			if (image.getRGB(0, y) == white) {
				if (!exitOrEntrance) {
					entrance = graphWidth * (y - 1) / 2;
					exitOrEntrance = true;
				} else {
					exit = graphWidth * (y - 1) / 2;
				}
			}

			// y, x = max
			if (image.getRGB(image.getWidth() - 1, y) == white) {
				if (!exitOrEntrance) {
					entrance = graphWidth - 1 + graphWidth * (y - 1) / 2;
					exitOrEntrance = true;
				} else {
					exit = graphWidth - 1 + graphWidth * (y - 1) / 2;
				}
			}
		}

		for (int x = 1; (x < height) && (entrance == -1 || exit == -1); x += 2) {
			// y = 0, x
			if (image.getRGB(x, 0) == white) {
				if (!exitOrEntrance) {
					entrance = (x - 1) / 2;
					exitOrEntrance = true;
				} else {
					exit = (x - 1) / 2;
				}
			}

			// y = max, x
			if (image.getRGB(x, image.getHeight() - 1) == white) {
				if (!exitOrEntrance) {
					entrance = (x - 1) / 2 + (graphHeight - 1) * graphWidth;
					exitOrEntrance = true;
				} else {
					exit = (x - 1) / 2 + (graphHeight - 1) * graphWidth;
				}
			}
		}

		assert (entrance != -1 || exit != -1);

		return new EntranceExit(entrance, exit, graphWidth);
	}

	public List<Arc>[] mapImageToGraph() {
		@SuppressWarnings("unchecked")
		List<Arc>[] IndexedAdjList = new List[((image.getWidth() - 1) / 2) * ((image.getHeight() - 1) / 2)];

		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		int graphWidth = (imageWidth - 1) / 2;
		int graphHeight = (imageHeight - 1) / 2;

		for (int i = 0; i < IndexedAdjList.length; i++) {
			IndexedAdjList[i] = new LinkedList<Arc>();
		}

		for (int x = 1; x < imageWidth; x += 2) {
			for (int y = 1; y < imageHeight; y += 2) {
				// Scan for paths from each node
				int coord =(x - 1) / 2 + graphWidth * (y - 1) / 2;
				if (image.getRGB(x - 1, y) == white && x > 1) {
					IndexedAdjList[coord].add(
							new Arc(coord, coord - 1));
				}
				if (image.getRGB(x + 1, y) == white && x < imageWidth - 2) {
					// -1 to be 0 bound, -1 to check it is not on the first path-filled line so -2
					IndexedAdjList[coord].add(
							new Arc(coord, coord + 1));
				}
				if (image.getRGB(x, y - 1) == white && y > 1) {
					IndexedAdjList[coord].add(
							new Arc(coord, coord - graphWidth));
				}
				if (image.getRGB(x, y + 1) == white && y < imageHeight - 2) {
					// -1 to be 0 bound, -1 to check it is not on the last path-filled line so - 2
					IndexedAdjList[coord].add(
							new Arc(coord, coord + graphWidth));
				}
			}
		}
		
		/*List<Arc> test = new LinkedList<Arc>();
		for(List<Arc> arcs : IndexedAdjList) {
			test.addAll(arcs);
		}
		try {
			ImageIO.write((new GraphToImage(graphHeight, graphWidth, test)).getImage(), "PNG"
					, new File("DUMP/TEST"+System.currentTimeMillis()+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		int a = 0;
		for(int i = 0;i < IndexedAdjList.length; i++) {
			a+=IndexedAdjList[i].size();
		}
		System.out.println("Arcs found: "+a/2);

		return IndexedAdjList;
	}

	private boolean validateDimension(int dimension) {

		// Must be odd
		if (dimension % 2 == 0) {
			return false;
		}

		// Must be > 1
		if (dimension <= 1) {
			return false;
		}

		return true;
	}
}
