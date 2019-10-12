package danny.mazes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class mazeSolver  implements Runnable {
	public static BufferedImage img = null;
	
	public static int height;
	public static int width;
	public static short scale;
	
	private static String name;
	
	public static Byte[][] graphArray;

	public static Graphics g;
	private static Thread rTHREAD;
	private static renderer rOBJECT;
		
	public static Thread[] solversTHR;
	public static solver[] solversOBJ;
	
	public mazeSolver(String nameIn, File image) {
		loadImage(image);
		name = nameIn;
	}
	
	private void loadImage(File image) {
		//open image
		try {
		    img = ImageIO.read(image);
		} catch (IOException e) {
			System.out.println("Error with file.");
			e.printStackTrace();
			System.exit(1);
		}	
		//init
		height = img.getHeight();
		width = img.getWidth();
		//get pixels
		graphArray = convertToArray(img); //1 = white, 0 = black
	}
	
	public Byte[][] convertToArray(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		//get raw data
		Byte[][] result = new Byte[image.getWidth()][image.getHeight()];
		Raster t = image.getData();
		for(int y = 0;y<height;y++) {
			int[] temp =  new int[width];
			for(int x = 0;x<width;x++) {
				if(t.getPixels( x, y, 1, 1, temp)[0] != 0) {
					result[x][y]=1;
				} else {
					result[x][y]=0;
				}
			}	
			
 		}			
		mazeSolver.width = width;
		mazeSolver.height = height;
		return result;
	}
	
	
	
	private void intToImg(String path){
		int[][] image_ = new int[width][height];
		for(int y = 0;y<height;y++) {
			for(int x = 0;x<width;x++) {
				if(graphArray[x][y]==1) {
					image_[x][y]=1;	
				} else {
					image_[x][y]=0;
				}
			}
		}
		try {
			Raster t = img.getData();
			final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			int[] temp = new int[90];
			for (int y = 0; y < height; y++) {
			    for (int x = 0; x < width; x++) {
			    	if(image_[x][y]==1) {
			    		image.setRGB(x, y, 0xff0000);
			    	} else {
			    		if( t.getPixel(x, y, temp)[0]!=0 ) {
				    		image.setRGB(x, y, 0xffffff );				    			
			    		} else {
				    		image.setRGB(x, y, 0x000000 );			    			
			    		}
			    	}
			    }
			}
			ImageIO.write(image, "png", new File(path));
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Save error. - retrying");
			try {
				Raster t = img.getData();
				final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				int[] temp = new int[90];
				for (int y = 0; y < height; y++) {
				    for (int x = 0; x < width; x++) {
				    	if(image_[x][y]==1) {
				    		image.setRGB(x, y, 0xff0000);
				    	} else {
				    		if( t.getPixel(x, y, temp)[0]!=0 ) {
					    		image.setRGB(x, y, 0xffffff );				    			
				    		} else {
					    		image.setRGB(x, y, 0x000000 );			    			
				    		}
				    	}
				    }
				}
				ImageIO.write(image, "png", new File(path));
			} catch(Exception e1) {
				e1.printStackTrace();
				System.out.println("Save error again. - exiting");
			}
		}
	}	

	/*public static int connectionCount(int x, int y) {
		int count = 0;
		if(graphArray[x+1][y]==1) {
			count++;
		}
		if(graphArray[x-1][y]==1) {
			count++;
		}
		if(graphArray[x][y+1]==1) {
			count++;
		}
		try {
			if(graphArray[x][y-1]==1) {
				count++;
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("ODD ERROR ON CONNECTION COUNTER");
		}
		return count;
	}*/
	
	public static void renderMaze() {
		mazeSolver.rTHREAD.run();
	}

	@Override
	public void run() {
		rOBJECT = new renderer();
		rOBJECT.scale=scale;
		rOBJECT.width=width;
		rOBJECT.height=height;
		//rOBJECT.img_=img_;
		rTHREAD = new Thread(rOBJECT,"RENDERER");
		//graphArray = imageArray;
		//init
		int cores = Runtime.getRuntime().availableProcessors() / 2;
		
		if(cores<=0) {
			cores=1;
		}
		//Doesn't eat all of the CPU -kind of important
		solversOBJ = new solver[cores];
		solversTHR = new Thread[cores];
		//set threads data
		for(int i = 0;i<cores;i++) {
			solversOBJ[i] = new solver();
		}
		int segments = height / cores;
		System.out.println(cores+" Cores Detected - thread y height is "+segments
				+" running with "+Runtime.getRuntime().totalMemory()+" bytes of ram");
		int i = 0;		
		for(solver s:solversOBJ) {
			//set s
			s.YMin = segments * i;
			if(s.YMin==0) {
				s.YMin = 1;
			}
			s.YMax = s.YMin + segments - 1;
			if(i+1==cores) {
				s.YMax += width-s.YMax -1;
			}
			if(s.YMax>=height-1) {
				s.YMax=height-2;
			}
			if(s.YMin==0) {
				s.YMin=1;
			}
			//set thread and exec
			solversTHR[i] = new Thread(s,"Solver thread "+i);
			solversTHR[i].start();
			i++;
		}
		//lets the solvers get a head start - timing stuff
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//wait for all threads to finish
		boolean finished = false;
		long time = System.currentTimeMillis();
		System.out.println("started solve");
		while(!finished) {
			finished = true;
			for(solver s:solversOBJ) {
				if(!s.finished) {
					finished=false;
					break;		//optimisation
				}
			}
			try {
				Thread.sleep(50);
				renderMaze();	
				System.gc();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Final pass...");
		
		process();
		
		//
		System.out.println("Finished.");
		System.gc();
		//out
				
		renderMaze();
		
		System.out.println("Saving");
		intToImg(name);
		System.out.println("Saved");
	}
	
	private void process() {
		//final check
		int YMin=1;
		int YMax=height-2;
		boolean changedThisPass = true;
		while(changedThisPass) {	
			renderMaze();	
			changedThisPass = false;
			
			for(int y = YMin;y<YMax;y++) {
				for(int x = 1;x<mazeSolver.width-1;x++) { //will not delete the start or end
					if(mazeSolver.graphArray[x][y]==1) {
						int count = 0;
						if(mazeSolver.graphArray[x+1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x-1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x][y+1]==1) {
							count++;
						}
						try {
							if(mazeSolver.graphArray[x][y-1]==1) {
								count++;
							}
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if(count==1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = 0;	
							img.setRGB(x, y, 0xFFFFFF);
						}
					}
				}
			}
			
			if(!changedThisPass) {
				break;	//TODO: appease mark
			}
			
			changedThisPass = false;
			
			for(int y = YMax;y>YMin;y--) {
				for(int x = 1;x<mazeSolver.width-1;x++) { //will not delete the start or end
					if(mazeSolver.graphArray[x][y]==1) {
						int count = 0;
						if(mazeSolver.graphArray[x+1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x-1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x][y+1]==1) {
							count++;
						}
						try {
							if(mazeSolver.graphArray[x][y-1]==1) {
								count++;
							}
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if(count==1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = 0;	
							img.setRGB(x, y, 0xFFFFFF);
						}
					}
				}
			}

			if(!changedThisPass) {
				break;	//TODO: appease mark
			}
			
			changedThisPass = false;
			for(int y = YMin;y<YMax;y++) {
				for(int x = mazeSolver.width-2;x>0;x--) { //will not delete the start or end
					if(mazeSolver.graphArray[x][y]==1) {int count = 0;
						if(mazeSolver.graphArray[x+1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x-1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x][y+1]==1) {
							count++;
						}
						try {
							if(mazeSolver.graphArray[x][y-1]==1) {
								count++;
							}
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if(count==1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = 0;	
							img.setRGB(x, y, 0xFFFFFF);
						}
					}
				}
			}	

			if(!changedThisPass) {
				break;	//TODO: appease mark
			}
			
			changedThisPass = false;
			for(int y = YMax;y>YMin;y--) {
				for(int x = mazeSolver.width-2;x>0;x--) { //will not delete the start or end
					if(mazeSolver.graphArray[x][y]==1) {
						int count = 0;
						if(mazeSolver.graphArray[x+1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x-1][y]==1) {
							count++;
						}
						if(mazeSolver.graphArray[x][y+1]==1) {
							count++;
						}
						try {
							if(mazeSolver.graphArray[x][y-1]==1) {
								count++;
							}
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("ODD ERROR ON CONNECTION COUNTER");
						}
						if(count==1) {
							changedThisPass = true;
							mazeSolver.graphArray[x][y] = 0;	
							img.setRGB(x, y, 0xFFFFFF);
						}
					}					
				}
			}	
		}
	}
}
