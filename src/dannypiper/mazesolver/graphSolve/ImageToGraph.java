package dannypiper.mazesolver.graphSolve;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

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
					exit = graphWidth  - 1 + graphWidth * (y - 1) / 2;
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
					entrance = (x - 1) / 2 + graphHeight * graphWidth;
					exitOrEntrance = true;
				} else {
					exit = (x - 1) / 2 + graphHeight * graphWidth;
				}
			}
		}

		assert (entrance != -1 || exit != -1);

		return new EntranceExit(entrance, exit);
	}

	public List<Arc>[] mapImageToGraph() {
		@SuppressWarnings("unchecked")
		List<Arc>[] IndexedAdjList = new List[((image.getWidth() - 1) / 2) * ((image.getHeight() - 1) / 2)];

		int width = image.getWidth();

		int graphWidth = (width - 1) / 2;

		for (int i = 0; i < IndexedAdjList.length; i++) {
			IndexedAdjList[i] = new LinkedList<Arc>();
		}

		for (int x = 1; x < image.getWidth(); x += 2) {
			for (int y = 1; y < image.getHeight(); y += 2) {
				// Scan for paths from each node
				if (image.getRGB(x - 1, y) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2
									, (x - 3) / 2 + graphWidth * (y - 1) / 2));
				}
				if (image.getRGB(x + 1, y) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2
									, (x + 1) / 2 + graphWidth * (y - 1) / 2));
				}
				if (image.getRGB(x, y - 1) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2
									, (x - 1) / 2 + graphWidth * (y - 3) / 2));
				}
				if (image.getRGB(x, y + 1) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2
									, (x - 1) / 2 + graphWidth * (y + 1) / 2));
				}
			}
		}

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
