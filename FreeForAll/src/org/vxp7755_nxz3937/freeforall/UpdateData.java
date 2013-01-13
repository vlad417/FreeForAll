package org.vxp7755_nxz3937.freeforall;

public class UpdateData {
	private int x;
	private int y;
	private int team;
	private int scores[];
	
	UpdateData( int ix, int iy, int iteam, int[] iscores )
	{
		x      = ix;
		y      = iy;
		team   = iteam;
		scores = iscores;
	}
	
	public int getX()        { return x; }
	public int getY()        { return y; }
	public int getTeam()     { return team; }
	public int[] getScores() { return scores; }
}
