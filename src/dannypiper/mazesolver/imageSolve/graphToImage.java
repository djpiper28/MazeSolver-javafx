package dannypiper.mazesolver.imageSolve;

import java.awt.image.BufferedImage;

public class graphToImage {

	private static final int white = 0xFFFFFF;
	private BufferedImage mazeImage;
	private int width;
	private int height;

	public void drawArc(final int adjMatX, final int adjMatY) {

		final int x1 = ((adjMatX % width) * 2) + 1;
		final int y1 = ((adjMatX / width) * 2) + 1;

		final int x2 = ((adjMatY % width) * 2) + 1;
		final int y2 = ((adjMatY / width) * 2) + 1;

		final int x3 = (x1 + x2) / 2;
		final int y3 = (y1 + y2) / 2;

		mazeImage.setRGB(x1, y1, white);
		mazeImage.setRGB(x2, y2, white);
		mazeImage.setRGB(x3, y3, white);
	}
}
