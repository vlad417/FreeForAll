package org.vxp7755_nxz3937.freeforall;

import android.os.Message;
import android.util.Log;

public abstract class PieceThread extends Thread {
	protected int _x;
	protected int _y;
	protected int _team;
	private boolean _alive;
	private ControllerThread _ctrlr;
	
	PieceThread( int x, int y, int team, ControllerThread ctrlr )
	{
		this._x     = x;
		this._y     = y;
		this._team  = team;
		this._alive = true;
		this._ctrlr = ctrlr;
	}
	
	/**
	 * Set the _alive boolean to false, which will cause run()'s while loop to end and the thread finish
	 */
	public void eaten()
	{
		_alive = false;
	}
	
	/**
	 * Retrieve the team number of this piece
	 * 
	 * @return team number (1-4)
	 */
	public int getTeam()
	{
		return _team;
	}
	
	/**
	 * Retrieve the x position of this piece
	 * 
	 * @return x position
	 */
	public int getX()
	{
		return _x;
	}
	
	/**
	 * Retrieve the y position of this piece
	 * 
	 * @return y position
	 */
	public int getY()
	{
		return _y;
	}
	
	@Override
	public void run()
	{
		Message boardMsg;
		while( _alive )
		{
			// Move this piece
			getNextMove();
			
			// Tell the controller I moved
			boardMsg      = _ctrlr.boardHandler.obtainMessage();
			MoveObject mv = new MoveObject( _x, _y, this );
			
			boardMsg.what = _ctrlr.MSGTYPE_MOVER;
			boardMsg.obj  = mv;
			_ctrlr.boardHandler.sendMessage( boardMsg );
			
			try {
				sleep( _ctrlr.getMoveDelay() );
			} catch (InterruptedException e) {
				Log.e( "PieceThread", "sleep interrupted" );
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Perform a specified move (only modifies this piece's internal position)
	 */
	protected abstract void getNextMove();
	
	/**
	 * If the move pushed us off the board, loop around to the other side of the board, else do nothing
	 */
	protected void ensureMoveOnBoard()
	{
		if( _ctrlr == null )
			Log.e("PieceThread", "Controller is null");
		else if( _ctrlr.getBoard() == null )
			Log.e("PieceThread", "Board is null");
		
		int boardSize[] = _ctrlr.getBoard().getSize(); 
		
		if( _x < 0 )
			_x = boardSize[1] - 1;
		else if( _x >= boardSize[1] )
			_x = 0;
		
		if( _y < 0 )
			_y = boardSize[0] - 1;
		else if( _y >= boardSize[0] )
			_y = 0;
	}
}
