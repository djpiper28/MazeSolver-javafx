package danny.mazes;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class renderer implements Runnable {
	public Byte[][] graphArray;
	public int width;
	public int height;
	public short scale;
	public BufferedImage img_;
	
	@Override
	public void run() {
		System.out.println("Render call");
		Byte[][] pxls=mazeSolver.graphArray;
		int[][] image_ = new int[this.width][this.height];
		final int height = this.height;
		final int width = this.width;
		for(int y = 0;y<height;y++) {
			for(int x = 0;x<width;x++) {
				if(pxls[x][y]==1) {
					image_[x][y]=1;	
				} else {
					image_[x][y]=0;
				}
			}
		}
		Raster t = mazeSolver.img.getData();
		BufferedImage image = new BufferedImage(mazeSolver.width, mazeSolver.height
				, BufferedImage.TYPE_INT_RGB);
		/*int[] temp = new int[90];
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
		}*/
		gui.image.setImage(image);
		//gui.canv.getGraphics().drawImage( image.getScaledInstance( 
		//		  mazeSolver.width * mazeSolver.scale,
		//		  mazeSolver.height * mazeSolver.scale,
		//          Image.SCALE_SMOOTH) ,0 ,0 , gui.canv);
		System.gc();
	}
}
