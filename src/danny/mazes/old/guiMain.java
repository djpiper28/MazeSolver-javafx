package danny.mazes.old;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import danny.mazes.mazeSolver;

public class guiMain {
	
	public static JFrame jFrame; 	
	public static JFileChooser fileChooser;
	public static JButton button;
	public static File file;
	public static JPanel canv;
	
	private static ActionListener Listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			guiMain.file = guiMain.getImageFile();
			System.out.println("Starting");
			guiMain.solveMaze();
		}
	};
	
	private static FileFilter fileFilter = new FileFilter() {
		
		@Override
		public String getDescription() {
			return "Select an image that is a maze (png, jpg or jpeg).";
		}
		
		@Override
		public boolean accept(File f) {
			if(f.isFile()) {
				return f.getName().contains(".png");
			} 			
			return f.isDirectory();			
		}
	};
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + " MB freed. Program closed.");
			}
		});
		boolean Finished = false;
		if(args.length>0) {
			for(String arg: args) {
				if(arg.contains(".png") && arg.contains("maze-")) {
					try {
						Finished &= true;
						System.out.println("Solving maze...");
						guiMain.file = new File(arg.split("maze-")[1]);
						guiMain.solveMaze();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		} 
		if (!Finished){
			try {
				System.out.println(Runtime.getRuntime().maxMemory()/1024/1024 + " MB RAM max, java versoin: ");
			} catch (Exception e) {
				e.printStackTrace();
			}
						
			System.out.println("Creating GUI...");
			
			EventQueue.invokeLater(new Runnable() {				
				@Override
				public void run() {
					try {
						createGUI();
					} catch(Exception e) {
						e.printStackTrace();
					}
					System.out.println("GUI created...");
					
				}
			});
		}
	}
	
	private static void createGUI() {
		jFrame = new JFrame("Maze Solver 2.8.0 - for Java SE 12 & Above");
		button = new JButton("Select Image."); 
		fileChooser = new JFileChooser(); 
		canv = new JPanel(true);
		
		jFrame.setBackground(Color.DARK_GRAY);
		jFrame.setFocusable(true);
		jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		button.addActionListener(Listener);
		
		jFrame.add(canv);
		canv.add(button);
		
		canv.setSize(1920, 1050);
		canv.setVisible(true);
		button.setVisible(true);
		jFrame.setSize(1920, 1050);
		button.setSize(1920, 1050);
		
		canv.setVisible(true);
		button.setVisible(true);
		jFrame.setVisible(true);
			
	}	

	
	
	private static File getImageFile() {
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fileChooser.setFileFilter(fileFilter);
		int returnVal = fileChooser.showOpenDialog(jFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			button.setText("Loading file..");
			System.out.println("Opening: " + file.getName() + ".");
			return file;
	   	} else {
	      	System.out.println("Open command cancelled by user.");
			button.setText("Try again...");
	   	}
		return null; 
	}
	
	private static void solveMaze() {
		System.out.println("Maze Loading");
		
		mazeSolver mz = new mazeSolver("mazeOutput" 
				+/*Pseudo random set of numbers*/ (System.currentTimeMillis()^System.nanoTime()) 
				+ ".png", file);
			
		Thread t = new Thread(mz, "Solver");
		System.out.println("GUI change");
		
		short scale = 1;
		if(mazeSolver.imageWidth*scale<500 && mazeSolver.imageHeight*scale<500) {			
			while(mazeSolver.imageWidth*scale<700 && mazeSolver.imageHeight*scale<700) {
				scale++;
			}
		}
		
		mazeSolver.imageRenderScale = scale;		

		jFrame.setResizable(false);
		jFrame.setSize(1920, 1050);
		//displayWindow.remove(button);
		canv.remove(button);
		canv.setSize(1920, 1050);
		//displayWindow.add(canv);			
		
		System.out.println("Maze Solving, output scale is " +scale);	
		
		long startTime = System.currentTimeMillis();
		t.run();
		long endTime = System.currentTimeMillis();
		
		System.out.println("Maze Solved in "+(endTime-startTime)+"ms, outputting in console");
		System.out.println("");	
		
		mazeSolver.renderMaze();	
		button.setText("RENERED.");
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		jFrame.add(new PopupMenu("Finished."));
		System.exit(0);
	}
	
}
