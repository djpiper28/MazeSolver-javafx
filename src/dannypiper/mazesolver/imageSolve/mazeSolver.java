package dannypiper.mazesolver.imageSolve;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dannypiper.mazesolver.renderer;
import dannypiper.mazesolver.renderless;
import javafx.scene.image.ImageView;

public class mazeSolver implements Runnable {

	public static BufferedImage mazeImage = null;
	public static boolean test;

	public static int imageHeight;
	public static int imageWidth;
	public static float imageRenderScale;

	private static File outputFile;

	public static boolean[][] graphArray;

	public static final int path = 0xFFFFFF;
	public static final int exitPath = 0xFF00FF;

	public static ImageView imageView;
	private static Thread renderThread;
	private static renderer renderObject;

	public static Thread[] solversTHR;
	public static solver[] solversOBJ;

	public static void renderMaze() {
		mazeSolver.renderThread.run();
	}

	public mazeSolver(File outputFile, File image) {
		loadImage(image);
		test = false;
		mazeSolver.outputFile = outputFile;
	}

	private void convertToArray(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		graphArray = new boolean[width][height];
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// get raw data
		int zero = mazeImage.getRGB(0, 0);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (mazeImage.getRGB(x, y) != zero) {
					graphArray[x][y] = true;
					temp.setRGB(x, y, exitPath);
				} else {
					graphArray[x][y] = false;
					temp.setRGB(x, y, 0);
				}
			}
		}
		imageWidth = width;
		imageHeight = height;
		mazeImage = temp;

		imageHeight = mazeImage.getHeight();
		imageWidth = mazeImage.getWidth();
	}

	private void loadImage(File image) {
		// open image
		try {
			mazeImage = ImageIO.read(image);
		} catch (IOException e) {
			System.out.println("Error with file.");
			e.printStackTrace();
			System.exit(13);
		}
		// init
		imageHeight = mazeImage.getHeight();
		imageWidth = mazeImage.getWidth();
		// get pixels
		convertToArray(mazeImage); // 1 = white, 0 = black
	}

	private void process() {
		// final check
		int YMin = 1;
		int YMax = imageHeight - 2;
		boolean changedThisPass = true;
		while (changedThisPass) {
			renderMaze();
			changedThisPass = false;

			for (int y = YMin; y < YMax; y++) {
				for (int x = 1; x < mazeSolver.imageWidth - 2; x++) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
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
						if (count == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeImage.setRGB(x, y, path);
						}
					}
				}
			}

			changedThisPass = false;

			for (int y = YMax; y > YMin; y--) {
				for (int x = mazeSolver.imageWidth - 2; x > 1; x--) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
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
						try {
							if (mazeSolver.graphArray[x][y - 1]) {
								count++;
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if (count == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeImage.setRGB(x, y, path);
						}
					}
				}
			}

			changedThisPass = false;
			for (int y = YMin; y < YMax; y++) {
				for (int x = mazeSolver.imageWidth - 2; x > 0; x--) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
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
						try {
							if (mazeSolver.graphArray[x][y - 1]) {
								count++;
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if (count == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeImage.setRGB(x, y, path);
						}
					}
				}
			}

			changedThisPass = false;
			for (int y = YMax; y > YMin; y--) {
				for (int x = mazeSolver.imageWidth - 2; x > 0; x--) { // will not delete the start or end
					if (mazeSolver.graphArray[x][y]) {
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
						try {
							if (mazeSolver.graphArray[x][y - 1]) {
								count++;
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if (count == 1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = false;
							mazeImage.setRGB(x, y, path);
						}
					}
				}
			}
		}
	}

	@Override
	public void run() {
		mazeImage.setAccelerationPriority(1);
		if (test) {
			renderObject = new renderless();
		} else {
			renderObject = new renderer();
		}
		renderObject.scale = imageRenderScale;
		renderObject.width = imageWidth;
		renderObject.height = imageHeight;

		renderThread = new Thread(renderObject, "RENDERER");

		// init
		int cores = Runtime.getRuntime().availableProcessors() / 2;

		if (cores <= 0) {
			cores = 1;
		}
		// Doesn't eat all of the CPU -kind of important
		solversOBJ = new solver[cores];
		solversTHR = new Thread[cores];
		// set threads data
		for (int i = 0; i < cores; i++) {
			solversOBJ[i] = new solver();
		}
		// get and set segments
		int segments = imageHeight / cores;

		System.out.println("Image dimensions: (" + imageHeight + ", " + imageWidth + ")");
		System.out.println(cores + " Cores Detected - thread y height is " + segments + " running with "
				+ (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB of ram");
		int i = 0;
		for (solver s : solversOBJ) {
			// set s
			s.YMin = segments * i;
			if (s.YMin == 0) {
				s.YMin = 1;
			}
			s.YMax = s.YMin + segments - 1;
			if (i + 1 == cores) {
				s.YMax = imageHeight - 2;
			}

			// init thread and execute
			solversTHR[i] = new Thread(s, "Solver thread: " + i + " of " + cores);
			solversTHR[i].start();
			i++;
		}
		// lets the solvers get a head start - timing stuff
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// wait for all threads to finish
		boolean finished = false;
		long time = System.currentTimeMillis();
		System.out.println("started solve");
		while (!finished) {
			long renderTime = System.currentTimeMillis();
			finished = true;
			for (solver s : solversOBJ) {
				if (!s.finished) {
					finished = false;
				}
			}
			try {
				renderMaze();
				long x = 16 - System.currentTimeMillis() - renderTime;
				if (x > 0) {
					Thread.sleep(x);
				}
				System.gc();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Final pass...");

		process();

		//
		System.out.println("Finished after " + (System.currentTimeMillis() - time) + "ms.");
		System.gc();
		// out

		renderMaze();

		System.out.println("Saving");
		saveFile(mazeSolver.outputFile);
		System.out.println("Saved");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!test) {
			// Exit as we are done!
			renderObject.renderFinishedScreen();
			System.out.println("Rednered");
		}
	}

	private void saveFile(File fileToSaveAs) {
		try {
			ImageIO.write(mazeImage, "png", fileToSaveAs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
