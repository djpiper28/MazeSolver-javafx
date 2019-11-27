package danny.mazes;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class guiJavaFX extends Application { 

	private final static String font = "Lucida Console";
	public final static String version = "2.9.0";
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
	
	private final static double greyConstant = 0.20d;
	private final static double greyConstantAccent = 0.3d;
		
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
		
		mazeSolver mz = new mazeSolver(outputFile, imageFile);
		
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
	
	private void openSaveFileChooser() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("Maze Image", "*.png"),
			     new FileChooser.ExtensionFilter("Maze Image", "*.jpeg")
			 );
		try {
			File selectedFile = fileChooser.showSaveDialog(stage);
			if(selectedFile != null) {
				outputFile = selectedFile;	//Keeps old file if dialog is closed
			}
			if(outputFile!=null) {
				fileForSavinglabel.setText("Selected '"+outputFile.getName()+"'");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
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
				fileForOpeninglabel.setText("Solving; "+imageFile.getName());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void initLabels() {
		fileForOpeninglabel = new Text("Please select a maze to solve."); 
		fileForOpeninglabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		fileForSavinglabel = new Text("Please select a file to save as."); 
		fileForSavinglabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
	}
	
	private void initButtons() {
        selectImageToOpenButton = new Button("Select maze to solve.");
        selectImageToSaveAsbutton = new Button("Save output image as.");
        solveButton = new Button("Please select an image.");

		selectImageToOpenButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		solveButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		selectImageToSaveAsbutton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		solveButton.setOnAction(e -> {
			if(imageFile!=null) {
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
	
	private void darkModeify(Region ...nodes) {
		Background darkMode = new Background(new BackgroundFill(new Color(greyConstant, greyConstant, greyConstant ,1d)
				, CornerRadii.EMPTY, Insets.EMPTY));
		for(Region n:nodes) {
			n.setBackground(darkMode);
			n.setStyle("-fx-text-fill: white;");
		}
	}
	
	private void darkModeify(Labeled ...nodes) {
		for(Labeled n:nodes) {
			n.setTextFill(Color.WHITE);
		}
	}
	
	private void darkModeify(Text ...nodes) {
		for(Text n:nodes) {
			n.setFill(Color.WHITE);
		}
	}
	
	private void darkModeAccent(Region ...nodes) {
		Background darkModeAccent = new Background(new BackgroundFill(new Color(greyConstantAccent, greyConstantAccent
			, greyConstantAccent ,1d), new CornerRadii(8d), Insets.EMPTY));
	
		for(Region n:nodes) {
			n.setBackground(darkModeAccent);
			n.setStyle("-fx-text-fill: white;");
		}
	}
	
	@Override     
	public void start(Stage arg0) throws Exception {
		
		initLabels();
		initButtons();		

        vBox = new VBox(
        		selectImageToOpenButton,
        		fileForOpeninglabel,
        		selectImageToSaveAsbutton,
        		fileForSavinglabel,
        		solveButton);
        
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(5);
	    
        darkModeify(vBox);
        
        darkModeAccent(selectImageToSaveAsbutton,
        	selectImageToOpenButton,
    		solveButton);
        
        darkModeify(fileForOpeninglabel,
			fileForSavinglabel);
        
		//Creating a scene object 
		inputScene = new Scene(vBox, 300, 200);
		
		stage = arg0;
		//Setting title to the Stage 		
		String memStatus = "";
		if(Runtime.getRuntime().totalMemory()/1024/1024<1000) {
			memStatus += "Low RAM! ";
		}
		memStatus+=Runtime.getRuntime().totalMemory()/1024/1024/1024+"GB of RAM.";
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
