package dannypiper.mazesolver;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class renderer implements Runnable {
	public int width;
	public int height;
	public float scale;

	@Override
	public void run() {
		long time = System.currentTimeMillis();

		if (scale != 1) {
			BufferedImage after = new BufferedImage((int) Math.ceil(width * scale), (int) Math.ceil(height * scale),
					BufferedImage.TYPE_INT_RGB);
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			after = scaleOp.filter(mazeSolver.mazeImage, after);

			Image image = SwingFXUtils.toFXImage(after, null);

			guiJavaFX.graphicsContext.drawImage(image, 0, 0);

			image = null;
			after = null;
		} else {
			Image image = SwingFXUtils.toFXImage(mazeSolver.mazeImage, null);

			guiJavaFX.graphicsContext.drawImage(image, 0, 0);

			image = null;
		}

		System.out.println("Render call - " + (System.currentTimeMillis() - time) + "ms");
		System.gc();
	}
}
