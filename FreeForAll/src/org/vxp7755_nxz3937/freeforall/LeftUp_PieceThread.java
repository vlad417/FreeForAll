package org.vxp7755_nxz3937.freeforall;

public class LeftUp_PieceThread extends PieceThread {
	
	int xMoves[] = {-1, 0};
	int yMoves[] = {0, -1};
	int move     = 0;

	LeftUp_PieceThread( int x, int y, int team )
	{
		super( x, y, team );
	}
	@Override
	protected void getNextMove() {
		++move;
		
		_x = _x + xMoves[move];
		_y = _y + yMoves[move];
	}

}
