package org.vxp7755_nxz3937.freeforall;

public abstract class PieceThread extends Thread {
	private int x;
	private int y;
	private int team;
	private boolean alive;
	
	PieceThread( int x, int y, int team )
	{
		this.x     = x;
		this.y     = y;
		this.team  = team;
		this.alive = true;
	}
	
	public void setLocation( int x, int y )
	{
		
	}
	
	public void eaten()
	{
		alive = false;
	}
	
	public int getTeam()
	{
		return team;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	@Override
	public void run()
	{
		
	}
	
	protected abstract void getNextMove();
}
