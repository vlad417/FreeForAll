package org.vxp7755_nxz3937.freeforall;

public class MoveObject {
	public int x;
	public int y;
	public PieceThread me;
	
	MoveObject( int x, int y, PieceThread piece )
	{
		this.x  = x;
		this.y  = y;
		this.me = piece;
	}
}
