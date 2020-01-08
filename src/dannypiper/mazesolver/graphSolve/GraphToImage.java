package dannypiper.mazesolver.graphSolve;

import java.awt.image.BufferedImage;
import java.util.List;

public class GraphToImage {

	public static final int pathColour = 0x00FF00;
	private static final int black = 0x000000;
	private int graphWidth, graphHeight, imageWidth, imageHeight;
	private BufferedImage image;
	private List<Arc> arcs;

	// Instantiate object then get the image, the constructor maps the graph to the
	// image
	public GraphToImage(int graphHeight, int graphWidth, List<Arc> arcs) {
		// Get input data
		this.arcs = arcs;
		this.graphHeight = graphHeight;
		this.graphWidth = graphWidth;

		// Init vars
		this.imageHeight = this.graphHeight * 2 + 1;
		this.imageWidth = this.graphWidth * 2 + 1;

		this.image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_RGB);
		// Init image
		initBlankImage();
		mapGraphToImage();
	}

	private void drawArc(final int StartingNode, final int EndingNode) {
		final int x1 = ((StartingNode % graphWidth) * 2) + 1;
		final int y1 = ((StartingNode / graphWidth) * 2) + 1;

		final int x2 = ((EndingNode % graphWidth) * 2) + 1;
		final int y2 = ((EndingNode / graphWidth) * 2) + 1;

		final int x3 = (x1 + x2) / 2;
		final int y3 = (y1 + y2) / 2;

		image.setRGB(x1, y1, GraphToImage.pathColour);
		image.setRGB(x2, y2, GraphToImage.pathColour);
		image.setRGB(x3, y3, GraphToImage.pathColour);
	}

	public BufferedImage getImage() {
		return this.image;
	}

	private void initBlankImage() {
		for (int x = 0; x < this.imageWidth; x++) {
			for (int y = 0; y < this.imageHeight; y++) {
				this.image.setRGB(x, y, GraphToImage.black);
			}
		}
	}

	private void mapGraphToImage() {
		for (Arc arc : this.arcs) {
			this.drawArc(arc.startingNode, arc.endingNode);
		}
	}

}
