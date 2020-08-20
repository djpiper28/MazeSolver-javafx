package dannypiper.mazesolver;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import dannypiper.mazesolver.imageSolve.mazeSolver;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Renderer implements Runnable {
	private int width;
	private int height;
	private float scale;
	private BufferedImage after;
	private AffineTransform at;
	private AffineTransformOp scaleOp;
	private Image image;

	public Renderer (float scale, int width, int height) {
		this.scale = scale;
		this.height = height;
		this.width = width;
		
		if (this.scale != 1) {
			this.at = new AffineTransform();
			this.at.scale(this.scale, this.scale);
			this.scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		}
	}
	
	public void renderFinishedScreen() {
		guiJavaFX.graphicsContext.drawImage(new Image("savingScreenImage.png"), 0, 0);
		System.out.println("Rendered finished message");
		
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();

		if (scale != 1) {
			this.after = new BufferedImage((int) Math.ceil(width * scale), (int) Math.ceil(height * scale),
					BufferedImage.TYPE_INT_RGB);
			this.after = this.scaleOp.filter(mazeSolver.mazeImage, after);

			this.image = SwingFXUtils.toFXImage(after, null);

			guiJavaFX.graphicsContext.drawImage(image, 0, 0);

		} else {
			this.image = SwingFXUtils.toFXImage(mazeSolver.mazeImage, null);

			guiJavaFX.graphicsContext.drawImage(image, 0, 0);
		}

		System.out.println("Render call - " + (System.currentTimeMillis() - time) + "ms");
	}
}
