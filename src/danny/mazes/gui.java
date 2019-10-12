package danny.mazes;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

public class gui {
	public static JFrame displayWindow = new JFrame("Maze Solver 2.7.1"); 
	public static final JFileChooser fc = new JFileChooser(); 
	public static final JButton button = new JButton("Select Image"); 
	public static File file;
	public static JScrollPane jsp;
	public static ImageIcon image;
	
	public static void main(String[] args) {
		initRender();
	}
	
	public static void initRender() {
		button.addActionListener(new Listener());
		button.setSize(100, 100);
		
		displayWindow.setSize(1920, 1050);
		displayWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		displayWindow.add(button);		
		displayWindow.setVisible(true);
	}
	
	public static FileFilter ff = new FileFilter() {
		
		@Override
		public String getDescription() {
			return "Select an image that is a maze (png, jpg or jpeg).";
		}
		
		@Override
		public boolean accept(File f) {
			try {
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
			} catch(Exception e) {
				return true;
			}
		}
	};
	
	public static File getImageFile() {
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fc.setFileFilter(ff);
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
	
	public static void maze() {
		System.out.println("Maze Loading");
		mazeSolver mz = new mazeSolver();
		
		mz.loadImage(file);		
		Thread t = new Thread(mz, "Solver");
		System.out.println("GUI change");
		
		short scale = 1;
		if(mz.width*scale<500 && mz.height*scale<500) {			
			while(mz.width*scale<700 && mz.height*scale<700) {
				scale++;
			}
		}
		
		mz.scale = scale;

		displayWindow.setResizable(false);
		displayWindow.remove(button);
		
		
		image = new ImageIcon(file.getAbsolutePath());
		jsp = new JScrollPane(new JLabel(image),
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVisible(true);
		displayWindow.add(jsp);
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
		gui.maze();
	}
	
}
