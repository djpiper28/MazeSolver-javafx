package dannypiper.mazesolver;

import java.io.File;

import dannypiper.mazesolver.imageSolve.mazeSolver;
import dannypiper.mazesolver.imageSolve.testMazeSolver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class guiJavaFX extends Application {

	private final static String font = "Lucida Console";
	public final static String version = Messages.getString("Gui.version"); //$NON-NLS-1$ \n" +

	public static int XMAX = 1920;
	public static int YMAX = 1000;

	private static File imageFile;
	private static File outputFile;
	private static Canvas canvas;

	private static VBox vBox;
	private static Scene inputScene;
	private static Scene renderScene;
	private static Stage stage;
	@SuppressWarnings("exports")
	public static GraphicsContext graphicsContext;

	private static Text fileForOpeninglabel;
	private static Text fileForSavinglabel;
	private static Button selectImageToOpenButton;
	private static Button selectImageToSaveAsbutton;
	private static Button solveButton;

	// Solve Type
	private static RadioButton imageSolve;
	private static RadioButton graphSolve;
	private static ToggleGroup solveTypeGroup;
	private static HBox typeSelectionHBox;

	private static RadioButton breadthFirst;
	private static RadioButton depthFirst;
	private static RadioButton dijkstras;
	private static ToggleGroup graphGroup;
	private static HBox graphSolveTypeHBox;

	private static RadioButton deadEndFilling;
	private static ToggleGroup imageGroup;
	private static HBox imageSolveTypeHBox;

	private final static double greyConstant = 0.20d;
	private final static double greyConstantAccent = 0.3d;

	private static void updateRadioButtons() {
		if(imageSolve.isSelected()) {
			imageSolveTypeHBox.setVisible(true);
			graphSolveTypeHBox.setVisible(false);
		} else {
			imageSolveTypeHBox.setVisible(false);
			graphSolveTypeHBox.setVisible(true);
		}
	}
	
	private static void initRadioButtons() {
		solveTypeGroup = new ToggleGroup();
		imageSolve = new RadioButton("Image Solve ");
		imageSolve.setToggleGroup(solveTypeGroup);
		imageSolve.setOnMouseClicked(T -> {
			updateRadioButtons();
		});
		
		graphSolve = new RadioButton("Graph Solve ");
		graphSolve.setToggleGroup(solveTypeGroup);
		graphSolve.setOnMouseClicked(T -> {
			updateRadioButtons();
		});
		
		imageSolve.setSelected(true);
		typeSelectionHBox = new HBox(imageSolve, graphSolve);
		typeSelectionHBox.setPadding(new Insets(5));

		graphGroup = new ToggleGroup();
		breadthFirst = new RadioButton("Breadth First ");
		breadthFirst.setToggleGroup(graphGroup);
		breadthFirst.setSelected(true);
		depthFirst = new RadioButton("Breadth First ");
		depthFirst.setToggleGroup(graphGroup);	
		dijkstras = new RadioButton("Dijkstras Algorithm ");
		dijkstras.setToggleGroup(graphGroup);
		
		graphSolveTypeHBox = new HBox(breadthFirst, depthFirst, dijkstras);
		graphSolveTypeHBox.setVisible(false);
		graphSolveTypeHBox.setPadding(new Insets(5));

		imageGroup = new ToggleGroup();
		deadEndFilling = new RadioButton("Dead End Filling ");
		deadEndFilling.setSelected(true);
		deadEndFilling.setToggleGroup(imageGroup);
		imageSolveTypeHBox = new HBox(deadEndFilling);
		imageSolveTypeHBox.setPadding(new Insets(5));
	}

	public static void main(String args[]) {
		boolean test = false;
		String path = "";
		for (String arg : args) {
			if (arg.contains("test")) {
				test = true;
			} else if (arg.contains("filepath-")) {
				path = arg.split("filepath-")[1];
			} else if (arg.contains("help") || arg.contains("h")) {
				System.out.println("Help for mazeSolver:\n" + "	- Parse 'h' or 'help' to see this help page\n"
						+ "	- Parse 'test' to run a test\n"
						+ "		- (tests all images in the current path by default)\n"
						+ "	- Parse 'filepath-<path>' to make the test run on the specified path\n"
						+ "Example of how to run a test in the testFolder\n"
						+ "	java -jar <jarFileName>.jar -Xms1G -Xss50M test filepath-testFolder\n"
						+ "Running the program with no parameters will show the GUI");
			}

		}

		if (test) {
			testMazeSolver testObject = new testMazeSolver(path);
			testObject.test();
		} else {
			launch(args);
		}
	}

	private void darkModeAccent(Region... nodes) {
		Background darkModeAccent = new Background(
				new BackgroundFill(new Color(greyConstantAccent, greyConstantAccent, greyConstantAccent, 1d),
						new CornerRadii(8d), Insets.EMPTY));

		for (Region n : nodes) {
			n.setBackground(darkModeAccent);
			n.setStyle("-fx-text-fill: white;");
		}
	}

	private void darkModeify(Labeled... nodes) {
		for (Labeled n : nodes) {
			n.setTextFill(Color.WHITE);
		}
	}

	private void darkModeify(Region... nodes) {
		Background darkMode = new Background(new BackgroundFill(new Color(greyConstant, greyConstant, greyConstant, 1d),
				CornerRadii.EMPTY, Insets.EMPTY));
		for (Region n : nodes) {
			n.setBackground(darkMode);
			n.setStyle("-fx-text-fill: white;");
		}
	}

	private void darkModeify(Text... nodes) {
		for (Text n : nodes) {
			n.setFill(Color.WHITE);
		}
	}

	private void initButtons() {
		selectImageToOpenButton = new Button("Select maze to solve.");
		selectImageToSaveAsbutton = new Button("Save output image as.");
		solveButton = new Button("Please select an image.");

		selectImageToOpenButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		solveButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		selectImageToSaveAsbutton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		solveButton.setOnAction(e -> {
			if (imageFile != null) {
				solveMaze();
			}
		});

		selectImageToOpenButton.setOnAction(e -> {
			openFileChooser();
		});

		selectImageToSaveAsbutton.setOnAction(e -> {
			openSaveFileChooser();
		});
	}

	private void initLabels() {
		fileForOpeninglabel = new Text("Please select a maze to solve.");
		fileForOpeninglabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		fileForSavinglabel = new Text("Please select a file to save as.");
		fileForSavinglabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
	}

	private void openFileChooser() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Image", "*.png"));
		try {
			File selectedFile = fileChooser.showOpenDialog(stage);
			imageFile = selectedFile;
			if (imageFile != null) {
				solveButton.setText("Solve the maze.");
				fileForOpeninglabel.setText("Solving; " + imageFile.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openSaveFileChooser() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Image", "*.png"),
				new FileChooser.ExtensionFilter("Maze Image", "*.jpeg"));
		try {
			File selectedFile = fileChooser.showSaveDialog(stage);
			if (selectedFile != null) {
				outputFile = selectedFile; // Keeps old file if dialog is closed
			}
			if (outputFile != null) {
				fileForSavinglabel.setText("Selected '" + outputFile.getName() + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void solveGUI() {
		canvas = new Canvas(mazeSolver.imageWidth, mazeSolver.imageHeight);

		ScrollPane scrollPane = new ScrollPane();

		scrollPane = new ScrollPane();
		scrollPane.setContent(canvas);

		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

		renderScene = new Scene(scrollPane, XMAX, YMAX);
		stage.setScene(renderScene);
		stage.setX(0);
		stage.setY(0);

		canvas.setOnMouseClicked(e -> {

			if (e.getClickCount() >= 2) {
				stage.setFullScreen(true);
			}

		});

		stage.setFullScreenExitHint("ESC to exit fullscreen mode" + "\nDouble click to go fullscreen"); //$NON-NLS-1$ //$NON-NLS-2$
		stage.setFullScreen(false);
	}

	private void solveMaze() {
		System.out.println("Maze Loading");

		mazeSolver mz = new mazeSolver(outputFile, imageFile);

		solveGUI();

		Thread solvingThread = new Thread(mz, "Solver Main");
		System.out.println("GUI change");

		float scalex = 1920 / mazeSolver.imageWidth;
		float scaley = 1050 / mazeSolver.imageHeight;

		float scale = 1;
		if (scalex < 1 || scaley < 1) {
			scale = 1f;
		} else if (scalex <= scaley) {
			scale = scalex;
		} else {
			scale = scaley;
		}

		mazeSolver.imageRenderScale = scale;

		System.out.println("Maze Solving, output scale is " + scale);

		graphicsContext = canvas.getGraphicsContext2D();

		solvingThread.start();
	}

	@Override
	public void start(Stage arg0) throws Exception {

		// Get primary screen and adjust canvas size for it.
		if (Screen.getPrimary() != null) {
			System.out.println(Screen.getPrimary().getBounds());
			XMAX = (int) Screen.getPrimary().getBounds().getWidth();
			YMAX = (int) Screen.getPrimary().getBounds().getHeight() - 40;
		}

		initLabels();
		initButtons();
		initRadioButtons();

		vBox = new VBox(selectImageToOpenButton, fileForOpeninglabel, selectImageToSaveAsbutton, fileForSavinglabel,
				typeSelectionHBox, graphSolveTypeHBox, imageSolveTypeHBox, solveButton);

		vBox.setPadding(new Insets(20));
		vBox.setSpacing(5);

		darkModeify(vBox, typeSelectionHBox, graphSolveTypeHBox, imageSolveTypeHBox, imageSolve, graphSolve,
				breadthFirst, depthFirst, dijkstras, deadEndFilling);

		darkModeAccent(selectImageToSaveAsbutton, selectImageToOpenButton, solveButton);

		darkModeify(fileForOpeninglabel, fileForSavinglabel);

		// Creating a scene object
		inputScene = new Scene(vBox, 450, 270);

		stage = arg0;
		// Setting title to the Stage
		String memStatus = "";
		if (Runtime.getRuntime().totalMemory() / 1024 / 1024 < 1000) {
			memStatus += "Low RAM! ";
		}
		memStatus += Runtime.getRuntime().totalMemory() / 1024 / 1024 / 1024 + "GB of RAM";
		stage.setTitle("Maze Solver v" + version + " - " + memStatus + ". Built on: " + GetBuildDate.GetBuildDate());

		// Adding scene to the stage
		stage.setScene(inputScene);

		// Displaying the contents of the stage
		stage.show();
		stage.setOnCloseRequest(e -> {
			System.exit(1); // User closed program
		});
	}

}
