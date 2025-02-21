package net.hextris;

import java.util.Random;
import java.awt.Point;

/*
 * Created on 02.01.2004
 *
 */

/**
 * Models a stone in the Tetris game
 * 
 * A stone can be place on the Board and taken from it.
 * It can be rotated.
 * 
 * The Constant stones contains the standard hextris stones.
 * 
 * The moving of a stone is modeled inside the Tetris class as it
 * has some important effects on the game.
 * 
 * @author fränk
 */
public class Stone extends Board 
{
	public static final boolean LEFT = true;
	public static final boolean RIGHT = false;

	public static final int MOVE_DOWN = 0;
	public static final int MOVE_LEFT = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int ROTATE_LEFT = 3;
	public static final int ROTATE_RIGHT = 4;

	private int posX;
	private int posY;
	private int color;
	private Board board;
	private int[][] severities = {{0,7},{3,13},{5,16}};
	

	/**
	 * array with stones
	 */
	static int stones[][][] =
	{
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,0,1,0},
			{0,0,1,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,1,0},
			{0,0,1,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,1,0},
			{0,0,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,1,1,0,0},
			{0,0,1,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,1,0},
			{0,0,1,0,0},
			{0,0,0,0,0},
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,1,1,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,1,1,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,1,1,0,0},
			{0,1,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,0,1,1,0},
			{0,0,0,1,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,0,0},
			{0,1,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,0,0,0},
			{0,1,0,1,0},
			{0,0,1,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,0,1,0},
			{0,1,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,1,1,0},
			{0,0,1,0,0},
			{0,0,1,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,1,1,0},
			{0,1,0,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		}
	};
	
	/**
	 * creates a random stone according to severity
	 * @param board
	 * @param severity
	 */
	public Stone(Board board, int severity) 
	{
		super(5,5);
		Random r = new Random();
		this.setType(r.nextInt(severities[severity][1] - severities[severity][0]) + severities[severity][0]);
		this.setBoard(board);
		posX=0;
		posY=0;
	}
	
	/**
	 * creates the stone from a given stone
	 * @param stone
	 * @param board
	 */
	public Stone(Stone stone, Board board) 
	{
		super(5,5);
		this.setField(stone.getField());
		this.color = stone.getColor();
		this.setBoard(board);
		posX=0;
		posY=0;
	}
	
	/**
	 * creates the stone from stone index
	 * @param i
	 */
	public Stone(int i) 
	{
		super(5,5);
		this.setField(new int[5][5]);
		this.setType(i);
		posX=0;
		posY=0;
	}
	
	/**
	 * initialize this stones board-field an color
	 * @param nr
	 */
	private void setType(int nr) 
	{
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				field[y][x]=stones[nr][y][x];
			}
		}
		color = nr+2;
	}
	
	/**
	 * sets stones position on board
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y)
	{
		this.posX = x;
		this.posY = y;
	}
	
	/**
	 * position on board
	 * @return
	 */
	public Point getPosition()
	{
		return new Point(posX,posY);
	}
		
	/**
	 * places or removes a stone from the board
	 * @param place true: place false: remove
	 */	
	public void place(boolean place) 
	{
		if (board==null) return;
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				int val=field[y][x];
				if (val==0) continue;
				int bx = posX+x;
				int by = posY+y+Math.abs((posX%2)*(x%2));
				board.setField(bx,by,place?color:0);
			}
		}
	}
	
	/**
	 * tests if the stone can be placed at given position
	 * @param nX
	 * @param nY
	 * @return
	 */
	public boolean mayPlace(int nX, int nY) 
	{
		if (board==null) return false;
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				int val=field[y][x];
				if (val==0) continue;
				int bx = nX+x;
				int by = nY+y+Math.abs((nX%2)*(x%2));
				if (by>=0 && board.getField(bx,by)!=0) return false;
			}
		}
		return true;		
	}
	
	/**
	 * moves the stone in specified way
	 * synchronized, so only one thread can move the stone at a time
	 * @param type
	 * @return
	 */
	public synchronized boolean moveStone(int type)
	{
		switch (type) {
		case MOVE_DOWN: return this.moveDown();
		case MOVE_LEFT: return this.moveHorizontal(LEFT);
		case MOVE_RIGHT: return this.moveHorizontal(RIGHT);
		case ROTATE_LEFT: return this.rotate(LEFT);
		case ROTATE_RIGHT: return this.rotate(RIGHT);
		default:
			System.out.println("move type not suported");
			return false;
		}
	}
	
	/**
	 * move one position horizontally
	 * @param direction to left or right
	 */
	private boolean moveHorizontal(boolean direction)
	{	
		int diff = direction?-1:1;
		
		this.place(false);
		boolean res = this.mayPlace(this.posX+diff,this.posY);
		if (res) this.posX+=diff;
		this.place(true);
		return res;
	}
	
	/**
	 * move one position down
	 * @return
	 */
	private boolean moveDown()
	{
		this.place(false);
		boolean res = this.mayPlace(this.posX, this.posY+1);
		if (res) this.posY+=1;
		this.place(true);
		
		return res;
	}
	
	/**
	 * rotates the stone on the board if possible
	 * if stone can only be rotated by moving left or right, then it moves
	 * @param direction left or right
	 */
	private boolean rotate(boolean direction) 
	{
		boolean res = true;
		
		this.place(false);		
		int[][] oldField = this.field;
		this.field = this.getFieldRotate(2,2,direction).field;
		//check posible x-positions
		if (this.mayPlace(this.posX,this.posY)) ;
		else if (this.mayPlace(this.posX+1,this.posY)) this.posX+=1;
		else if (this.mayPlace(this.posX-1,this.posY)) this.posX-=1;
		else if (this.mayPlace(this.posX+2,this.posY)) this.posX+=2;
		else if (this.mayPlace(this.posX-2,this.posY)) this.posX-=2;
		else {
			field = oldField;
			res = true;
		}
		this.place(true);
		
		return res;
	}
	
	
	private int getColor() 
	{
		return color;
	}

	private void setBoard(Board board) 
	{
		this.board = board;
	}
	
	public int[] getBestPosition()
	{
		int[] bestPos = new int[]{-1,-1,-1,-1};
		int bestEval = -1;
		if (this.board==null) return bestPos;
		
		int[] boardSurface = this.board.getSurface(FROM_TOP);
		
		for (int hpos=-2; hpos<this.board.getWidth()-1; hpos++) {
			//horicontal positions
			Board stoneBoard = new Board(this.getField());
			for (int rots=0; rots<6; rots++) {
				//rotations
				int[] stoneSurface = stoneBoard.getSurface(FROM_BOTTOM);
				boolean valid = true;
				
				int[] diff = new int[stoneSurface.length];
				int maxDiff = -1;
				int maxY = -1;
				for (int i=0; i<diff.length; i++) {
					int boardX = i+hpos;
					if (stoneSurface[i]==-1) {
						diff[i]=-1;
					} else if (boardX<0 || i+hpos>=boardSurface.length) {
						valid=false;
						break;
					} else {
						diff[i]=5+boardSurface[boardX]-stoneSurface[i];
						if (Math.abs(hpos%2)==1 && i%2==1) diff[i]--;
						if (diff[i]>maxDiff) maxDiff=diff[i];
						if (boardSurface[boardX]>maxY) maxY=boardSurface[boardX];
					}
				}
				
				if (valid) {
					//auswerten
					int holeCount = 0;
					for (int i=0; i<diff.length; i++) if (diff[i]!=-1) holeCount+=(maxDiff-diff[i]);					
					
					//compare with best
					int eval = holeCount*100+(50-maxY);
					if (bestEval==-1 || eval<bestEval) {
						bestPos[0] = hpos;
						bestPos[1] = rots;
						bestPos[2] = holeCount;
						bestPos[3] = maxDiff;
						bestEval = eval;
					}
				}
				//rotate for next test
				stoneBoard = stoneBoard.getFieldRotate(2, 2, LEFT);
			}
			
		}
		
		
		return bestPos;
	}

}
