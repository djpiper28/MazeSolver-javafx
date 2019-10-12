package danny.mazes;

public class solver implements Runnable {
	public int YMin;
	public int YMax;
	public Boolean finished;	
	
	@Override
	public void run() {
		boolean changedThisPass = true;
		this.finished=false;
		while(changedThisPass) {	
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
							mazeSolver.img.setRGB(x, y, 0xFFFFFF);	
						}
					}
				}
			}

			if(!changedThisPass) {
				break;
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
							mazeSolver.img.setRGB(x, y, 0xFFFFFF);		
						}
					}
				}
			}

			if(!changedThisPass) {
				break;
			}
			changedThisPass = false;
			for(int y = YMin;y<YMax;y++) {
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
							mazeSolver.img.setRGB(x, y, 0xFFFFFF);	
						}
					}
				}
			}	

			if(!changedThisPass) {
				break;
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
							mazeSolver.img.setRGB(x, y, 0xFFFFFF);		
						}
					}					
				}
			}	
		}	
		this.finished=true;	
		System.out.println("Thread finished.");
	}
}