package org.vxp7755_nxz3937.freeforall;

public class LeftDown_PieceThread extends PieceThread {
	
	int xMoves[] = {-1, 0};
	int yMoves[] = {0, 1};
	int move     = 0;


	LeftDown_PieceThread( int id, int x, int y, int team, ControllerThread ctrlr )
	{
		super( id, x, y, team, ctrlr );
	}
	
	@Override
	protected void getNextMove() {
		move = (++move) % 2;

		_x = _x + xMoves[move];
		_y = _y + yMoves[move];
		
		// Loop around board if needed
		ensureMoveOnBoard();
	}

}
