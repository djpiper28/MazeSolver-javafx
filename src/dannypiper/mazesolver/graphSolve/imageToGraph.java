package dannypiper.mazesolver.graphSolve;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class imageToGraph {

	private static final int white = 0xFFFFFF;
	private BufferedImage image;

	public imageToGraph(BufferedImage inputImage) throws DimensionError {
		if (validateDimension(inputImage.getWidth()) && validateDimension(inputImage.getHeight())) {
			this.image = inputImage;
		} else {
			throw new DimensionError();
		}
	}

	public EntranceExit getEntranceExit() {
		Arc entrance = null;
		Arc exit = null;

		int width = image.getWidth();
		int height = image.getHeight();
		int graphWidth = (width - 1) / 2;
		int heightWidth = (height - 1) / 2;

		boolean exitOrEntrance = false;

		for (int y = 1; (y < height) && (!exitOrEntrance); y += 2) {
			// y, x = 0
			if (image.getRGB(0, y) == white) {
				if (!exitOrEntrance) {
					entrance = new Arc(width * (y - 1) / 2, 1 + width * (y - 1) / 2);
				} else {
					exit = new Arc(width * (y - 1) / 2, 1 + width * (y - 1) / 2);
				}
			}

			// y, x = max
			if (image.getRGB(0, y) == white) {
				if (!exitOrEntrance) {
					entrance = new Arc(graphWidth + width * (y - 1) / 2, graphWidth + 1 + width * (y - 1) / 2);
					exitOrEntrance = true;
				} else {
					exit = new Arc(graphWidth + width * (y - 1) / 2, graphWidth + 1 + width * (y - 1) / 2);
				}
			}
		}

		// y = 0, x
		// y = max, x

		for (int x = 1; (x < height) && (!exitOrEntrance); x += 2) {
			// y, x = 0
			if (!exitOrEntrance) {
				entrance = new Arc((x - 1) / 2 + width * heightWidth, (x - 1) / 2 + 1 + width * heightWidth);
				exitOrEntrance = true;
			} else {
				exit = new Arc((x - 1) / 2 + width * heightWidth, (x - 1) / 2 + 1 + width * heightWidth);
			}

			// y, x = max
			if (!exitOrEntrance) {
				entrance = new Arc(graphWidth + width * heightWidth, (x - 1) / 2 + 1 + width * heightWidth);
				exitOrEntrance = true;
			} else {
				exit = new Arc(graphWidth + width * heightWidth, (x - 1) / 2 + 1 + width * heightWidth);
			}
		}

		assert (entrance != null && exit != null);

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
				if (image.getRGB(x - 1, y) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2, (x - 2) / 2 + graphWidth * (y - 1) / 2));
				}
				if (image.getRGB(x + 1, y) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2]
							.add(new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2, (x) / 2 + graphWidth * (y - 1) / 2));
				}
				if (image.getRGB(x, y - 1) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2].add(
							new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2, (x - 1) / 2 + graphWidth * (y - 2) / 2));
				}
				if (image.getRGB(x, y + 1) == white) {
					IndexedAdjList[(x - 1) / 2 + graphWidth * (y - 1) / 2]
							.add(new Arc((x - 1) / 2 + graphWidth * (y - 1) / 2, (x - 1) / 2 + graphWidth * (y) / 2));
				}
			}
		}

		return IndexedAdjList;
	}

	private boolean validateDimension(int dimension) {

		// Must be odd
		if (dimension % 2 == 1) {
			return false;
		}

		// Must be > 1
		if (dimension <= 1) {
			return false;
		}

		return true;
	}
}
