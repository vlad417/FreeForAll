package org.vxp7755_nxz3937.freeforall;

public class Spiral_PieceThread extends PieceThread {

	int moves[][] = { {0, -1}, // down
					  {1, 0},  // right
					  {0, 1},  // up
					  {-1, 0}  // left
					};
					   
	int direction = 0; // start going down
	int TURNS_BETWEEN_INCREASE = 2;
	int timesInThisDir = 0;
	int movesToMakeInDirection = 1;
	int turns = 0;

	Spiral_PieceThread( int x, int y, int team, ControllerThread ctrlr )
	{
		super( x, y, team, ctrlr );
	}
	
	@Override
	protected void getNextMove() {
		
		
		// make move
		_x = _x + moves[direction][0];
		_y = _y + moves[direction][1];
		
		// increment times in current direction
		timesInThisDir++;
		
		// if timesInThisDir == movesInDirection, turn
		if (timesInThisDir == movesToMakeInDirection) {
			// reset move count
			timesInThisDir = 0;
			// change direction
			direction = (direction++ % 4);
			turns++;
			
			// if turns == 2, reset
			if (turns == TURNS_BETWEEN_INCREASE) {
				turns = 0;
				movesToMakeInDirection++;
			}
		}
		
		
		// Loop around board if needed
		ensureMoveOnBoard();
	}

}
