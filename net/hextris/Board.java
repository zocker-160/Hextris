package net.hextris;

/**
 * Implements the Hextris gameboard (or stone board). 
 * 
 * Supports clearing the field, setting and reading single fields.
 * Removelines supports deleting full lines and moving the rest down.
 * 
 * @author fränk
 */
public class Board 
{
	public static boolean FROM_TOP = true;
	public static boolean FROM_BOTTOM = false;
	
	protected int width;
	protected int height;
	protected int field[][];
	
	/**
	 * new board with given size
	 */
	public Board(int width,int height) 
	{
		this.width = width;
		this.height = height;
		field = new int[height][width];
	}

	/**
	 * new board with given field
	 * @param field
	 */
	public Board(int[][] field) 
	{
		this.width = field[0].length;
		this.height = field.length;
		this.field = field;
	}


	/**
	 * is the bottom line full?
	 * @param y
	 * @return
	 */
	public boolean lineFull(int y) 
	{
		for (int x = 1; x < width-1; x++) {
			if (field[y][x]==0) return false;
		}
		return true;
		
	}
	
	/**
	 * clears the bottom line
	 * @param y
	 */
	public void clearLine(int y)
	{
		for (int x = 1; x < this.width-1; x++) field[y][x]=0;
	}
	
	private void copyLine(int srcY,int destY) 
	{
		for (int x=1;x<width-1;x++) {
			field[destY][x]=field[srcY][x];
		} 
	}
	
	/**
	 * moves all lines down
	 * @param y
	 */
	public void removeLine(int y) 
	{
		for (int cY=y;cY>0;cY--) {
			copyLine(cY-1,cY);
		}
		for (int x=1;x<width-1;x++) field[0][x]=0;		
	}
	
	public int getHeight() 
	{
		return height;
	}

	public int getWidth() 
	{
		return width;
	}
	
	/**
	 * draws the play area on this board, i.e. the frame of hexagons
	 *
	 */
	public void drawPlayField() {
		for (int y=0;y<height-1;y++) {
			field[y][0] = 1;
			for (int x=1;x<width-1;x++) {
				field[y][x]=0;
			}
			field[y][width-1] = 1;
		}
		for (int x=0;x<width;x++) field[height-1][x]=1;
		
	}
	
	protected void setField(int x,int y,int color) {
		if (x>=0 && x<width && y>=0 && y<height)
			field[y][x]=color;
	}
	
	protected int getField(int x, int y) {
		if (x>=0 && x<width && y>=0 && y<height) {
			return field[y][x];
		} else {
			return 1;
		} 
	}	
	
	protected int[][] getField() 
	{
		return field;
	}

	protected void setField(int[][] is) 
	{
		field = is;
	}


	/**
	 * computes the rotated board
	 * tricky algorythm: position on the board is translated into a path of steps
	 * from the center hexagon to the rotated hexagon. thus one path component
	 * is the number of steps  that are either up/down, on a 60 degree line or 
	 * 120 degree line starting. the path is easyly rotated by shifting the 
	 * components. finally the path is translated into the new coordinate. 
	 * @param cx x-coordinate of center
	 * @param cy y-coordinate of center
	 * @param direction left or right
	 * @return a new board with the rotated field
	 */
	protected Board getFieldRotate(int cx, int cy, boolean direction)
	{
		int[][] newField = new int[this.getHeight()][this.getWidth()];
		for (int y = 0;y < this.field.length;y++) {
			for (int x = 0; x < this.field[y].length; x++) if (this.field[y][x]!=0) {
				
				//compute path
				int path[] = {0,0,0};
				int dx = x - cx;
				int dy = y - cy;
				path[0]+= dy;
				path[1]+= dx/2;
				path[2]+= dx/2;
				if (dx%2>0) path[1]+=1;
				if (dx%2<0) path[2]-=1;
				
				//rotate path
				int[] newPath ;
				if (direction) newPath = new int[] {-path[2],path[0],path[1]};
				else newPath = new int[] {path[1],path[2],-path[0]};

				//compute new coordinates one path component at a time
				int newx = cx;
				int newy = cy;
				//path[0]
				newy+=newPath[0];
				//path[1]
				if (newx%2==1 && newPath[1]%2==1) newy+=1;
				if (newx%2==0 && newPath[1]%2==-1) newy-=1;
				newx+=newPath[1];
				newy+=newPath[1]/2;
				//path[2]
				if (newx%2==0 && newPath[2]%2==1) newy-=1;
				if (newx%2==1 && newPath[2]%2==-1) newy+=1;
				newx+=newPath[2];
				newy-=newPath[2]/2;

//				System.out.println(x + "," + y + " " + 
//						path[0] + "," + path[1] + "," + path[2] + 
//						" -> " + newPath[0] + "," + newPath[1] + "," + newPath[2] + "  " + 
//						newx + "," + newy);
				if (newx<this.getWidth() && newx>=0 && newy<this.getHeight() && newy>=0) newField[newy][newx]=field[y][x];
			}
		}
		
		return new Board(newField);
	}	

	
	public int[] getSurface(boolean direction)
	{
		int[] res = new int[this.width];
		
		for (int x=0; x<this.width; x++){
			res[x]=-1;
			for (int y=direction?0:this.height-1;y!=(direction?this.height:-1);y+=direction?1:-1) {
				if (this.field[y][x]!=0) {
					res[x] = y;
					break;
				}
			}
			//System.out.print(res[x]+"|");
		}
		
		//System.out.println();
		
		return res;
	}

//	public Vector<Integer> getPlacableStones()
//	{
//		Vector<Integer> res = new Vector<Integer>();
//		
//		int[] surface = this.getSurface();
//
//		//stones
//		for (int sId = 0; sId<Stone.stones.length; sId++) {
//			
//			boolean stonePlacable = false;
//			Stone stone = new Stone(sId);
//			
//			//rotations of stone
//			for (int rot = 0; rot<6; rot++) {
//				stone.rotate(true);
//				int[] stoneSurface = stone.getSurface();
//				
//				//s-positions of stone in board
//				int posX=-3;
//				while (++posX<this.getWidth()) {
//					boolean posPlacable = true; 
//					int oldDiff = -100;
//				
//					//x-coordinates of stone
//					for (int stX = 0; stX<stone.getWidth(); stX++) {
//
//						int bx = posX + stone.getWidth() - stX; //position in board
//				
//						if (bx<0 || bx>=this.getWidth()) continue;
//						if (stoneSurface[stX]==stone.height) continue;
//						int newDiff = surface[bx]-stoneSurface[stX];
//						if (oldDiff==-100) {
//							oldDiff = newDiff;
//						} else if (newDiff!=oldDiff) {
//							posPlacable = false;
//							break;
//						}
//					}
//					
//					if (posPlacable) {
//						stonePlacable = true;
//						break;
//					}
//				}
//	
//				if (stonePlacable) break;
//			}
//			
//			if (stonePlacable) res.add(new Integer(sId));
//			System.out.print(sId + "|");
//		}
//		System.out.println(); 
//		return res;
//	}


}
