package dannypiper.mazesolver.imageSolve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class testMazeSolver {

	private String path;
	private File pathFolder;
	private Queue<File> files;
	private PrintWriter csvWriter;

	public testMazeSolver(String pathOfFiles) {
		this.path = pathOfFiles;
		this.files = new LinkedList<File>();
		this.pathFolder = new File(path);

		assert (this.pathFolder.isDirectory());

		this.listFilesForFolder(this.pathFolder);
		try {
			csvWriter = new PrintWriter(new File("Output for " + pathOfFiles + ".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(13);
		}

		File newFolder = new File(path + "_OUTPUT");
		newFolder.mkdir();
	}

	public String getPath() {
		return path;
	}

	// Is recursively getting all subFolders and adding searching them. Files are
	// added to the stack and are assumed to be image files (this is for testing so
	// I can assume that)
	public void listFilesForFolder(final File folder) {
		System.out.println("Scanning " + folder.getAbsolutePath());
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				files.add(fileEntry);
			}
		}

	}

	public void setPath(String path) {
		this.path = path;
	}

	public void test() {
		int n = 1;
		int size = files.size();

		String dataToWrite = "area,time";
		System.out.println(dataToWrite);
		csvWriter.println(dataToWrite);

		while (!files.isEmpty()) {
			System.out.println("[TEST]: Loading Maze " + n + " of " + size);
			File imageFile = files.remove();

			mazeSolver mz = new mazeSolver(new File(path + "_OUTPUT/" + "Output" + imageFile.getName()), imageFile);
			mazeSolver.test = true;

			int width = mazeSolver.imageWidth;
			int height = mazeSolver.imageHeight;
			int area = width * width + height * height;

			System.out.println("Solving maze...");
			long time = System.currentTimeMillis();
			mz.run();

			time = System.currentTimeMillis() - time;

			dataToWrite = area + "," + time;
			System.out.println("[TEST]:" + dataToWrite);
			csvWriter.println(dataToWrite);

			n++;
		}

		System.out.println("Finished");
		csvWriter.close();
		System.exit(0);
	}
}
