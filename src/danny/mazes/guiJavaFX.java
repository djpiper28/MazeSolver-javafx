package danny.mazes;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class guiJavaFX extends Application { 

	private final static String font = "Lucida Console";
	public final static String version = "2.8.2";
	private static File imageFile;
	private static Canvas canvas; 

	private static TextField filenameField;
	private static VBox vBox;
	private static Scene inputScene;  
	private static Scene renderScene;       
	private static Stage stage;
	public static GraphicsContext graphicsContext;
	
	private static Text labelText; 
	private static Text fileSelectedText;
	private static Button selectImageButton;
	private static Button solveButton;
		
	private void solveGUI() {
		canvas = new Canvas(1920, 1050);
		
		renderScene = new Scene(new Group(canvas), 1920, 1050);
		stage.setScene(renderScene); 
		stage.setResizable(false);
		stage.setX(0);
		stage.setY(0);	
		stage.setFullScreen(true); //Fullscreen!
	}
	
	private void solveMaze() {		
		System.out.println("Maze Loading");
				
		solveGUI();
		
		mazeSolver mz = new mazeSolver(filenameField.getText(), imageFile);
		
		Thread solvingThread = new Thread(mz, "Solver Main");
		System.out.println("GUI change");
		
		float scalex = 1920 / mazeSolver.imageWidth;
		float scaley = 1050 / mazeSolver.imageHeight;
		
		float scale = 1;
		if(scalex < 1 || scaley < 1) {
			scale = 1f;
		} else if(scalex <= scaley) {
			scale = scalex;
		} else {
			scale = scaley;
		}
		
		mazeSolver.imageRenderScale = scale;		
		
		System.out.println("Maze Solving, output scale is " +scale);	
		
		graphicsContext = canvas.getGraphicsContext2D();
		
		solvingThread.start();
	}
	
	private void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("Maze Image", "*.png")
			 );
		try {
			File selectedFile = fileChooser.showOpenDialog(stage);
			imageFile = selectedFile;
			if(imageFile!=null) {
				solveButton.setText("Solve the maze.");
				fileSelectedText.setText("Selected '"+imageFile.getName()+"'");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void initFilenameField() {
        filenameField = new TextField("SampleName.png");
        filenameField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
	}
	
	private void initLabels() {
		labelText = new Text("File name for output:"); 
		fileSelectedText = new Text("No file selected."); 
		labelText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		fileSelectedText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
	}
	
	private void initButtons() {
        selectImageButton = new Button("Select Image File:");
        solveButton = new Button("Please select an image.");
        

		selectImageButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		solveButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		solveButton.setOnAction(e -> {
			if(!filenameField.getText().contains(".png")) {
				filenameField.setText(filenameField.getText()+".png");
			}
			if(imageFile!=null) {
				solveMaze();
			}
		});
		
        selectImageButton.setOnAction(e -> {
        	openFileChooser();
        });
	}
	
	@Override     
	public void start(Stage arg0) throws Exception {
		
		initFilenameField();
		initLabels();
		initButtons();		

        vBox = new VBox(labelText,
        		filenameField,
        		selectImageButton,
        		fileSelectedText,
        		solveButton);
        
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(5);
	       			               
		//Creating a scene object 
		inputScene = new Scene(vBox, 300, 200);
		
		stage = arg0;
		//Setting title to the Stage 		
		String memStatus = "";
		if(Runtime.getRuntime().totalMemory()/1024/1024<1500) {
			memStatus += "Low RAM! ";
		}
		memStatus+=Runtime.getRuntime().totalMemory()/1024/1024/1024+"GB RAM Default.";
		stage.setTitle("Maze Solver v"+version+" - "+memStatus); 
	         
		//Adding scene to the stage 
		stage.setScene(inputScene); 
	         
		//Displaying the contents of the stage 
		stage.show();  
		stage.setOnCloseRequest(e -> {
			System.exit(1); //User closed program
		});
	}         
		  
	public static void main(String args[]){   
		launch(args);      
	}
		
}
