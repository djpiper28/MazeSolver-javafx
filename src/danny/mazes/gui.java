package danny.mazes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

public class gui {
	public static JFrame displayWindow = new JFrame("Maze Solver 2.8.0"); 
	public static JTextField filenameInput = new JTextField("your Image Name.png");
	
	public static final JFileChooser fc = new JFileChooser(); 
	public static final JButton button = new JButton("Select Image"); 
	public static File file;
	public static JScrollPane scrollPane;
	public static ImageIcon image;
	
	public static void main(String[] args) {
		initRender();
	}
	
	public static void initRender() {
		button.addActionListener(new Listener());
		
		displayWindow.setSize(500, 300);
		displayWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		displayWindow.add(button);		
		displayWindow.add(filenameInput);	
		displayWindow.setVisible(true);
	}
	
	public static FileFilter fileFilter = new FileFilter() {
		
		@Override
		public String getDescription() {
			return "Select an image that is a maze (png, jpg or jpeg).";
		}
		
		@Override
		public boolean accept(File f) {
			if(f.isFile()) {
				String[] temp = f.getName().split(".");
				switch(temp[temp.length-1].toLowerCase()) {
				case "png":
					return true;
				case "jpg":
					return true;
				case "jpeg":
					return true;
				}
				return false;
			} 			
			return true;			
		}
	};
	
	public static File getImageFile() {
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fc.setFileFilter(fileFilter);
		int returnVal = fc.showOpenDialog(displayWindow);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			button.setText("Loading file..");
			System.out.println("Opening: " + file.getName() + ".");
			return file;
	   	} else {
	      	System.out.println("Open command cancelled by user.");
			button.setText("Try again...");
	   	}
		return null; 
	}
	
	public static void solveMaze() {
		System.out.println("Maze Loading");
		String txt = "";
		
		if(filenameInput.getText().length()<=0) {
			txt = "You forgot to name your output file.png";
		} else {
			for(String c: filenameInput.getText().toLowerCase().split("")) {
				if(!"0123456789.-qwertyuiopasdfghjklzxcvbnm".contains(c)) {
					txt += c;
				}
			}
		}			
		
		mazeSolver mz = new mazeSolver(txt, file);
			
		Thread t = new Thread(mz, "Solver");
		System.out.println("GUI change");
		
		short scale = 1;
		if(mazeSolver.width*scale<500 && mazeSolver.height*scale<500) {			
			while(mazeSolver.width*scale<700 && mazeSolver.height*scale<700) {
				scale++;
			}
		}
		
		mazeSolver.scale = scale;		

		displayWindow.setResizable(false);
		displayWindow.setSize(1920, 1050);
		displayWindow.remove(button);
		displayWindow.remove(filenameInput);
		
		
		image = new ImageIcon(file.getAbsolutePath());
		scrollPane = new JScrollPane(new JLabel(image),
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		scrollPane.setVisible(true);
		displayWindow.add(scrollPane);
		System.out.println("Maze Solving, output scale is " +scale);	
		
		long startTime = System.currentTimeMillis();
		t.run();
		long endTime = System.currentTimeMillis();
		
		System.out.println("Maze Solved in "+(endTime-startTime)+"ms, outputting in console");
		System.out.println("");	
		
		mz.renderMaze();	
		button.setText("RENERED.");
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
class Listener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		gui.file = gui.getImageFile();
		System.out.println("Starting");
		gui.solveMaze();
	}
	
}
