package org.vxp7755.freeforall;

public class Board {
	
	private PieceThread boardCells[][];
	private int scores[] = {0,0,0,0};
	
	Board()
	{
		// init boardCells
	}
	
	public PieceThread getCell( int x, int y )
	{
		return null;
	}
	
	public void setCell( int x, int y, PieceThread piece )
	{
		
	}
	
	public void givePoint( int team )
	{
		
	}
	
	public int[] getScores() { return scores; }
}
