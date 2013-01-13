package org.vxp7755_nxz3937.freeforall;

import android.os.Message;

public abstract class PieceThread extends Thread {
	private int _x;
	private int _y;
	private int _team;
	private boolean _alive;
	private ControllerThread _ctrlr;
	
	PieceThread( int x, int y, int team )
	{
		this._x     = x;
		this._y     = y;
		this._team  = team;
		this._alive = true;
	}
	
	public void eaten()
	{
		_alive = false;
	}
	
	public int getTeam()
	{
		return _team;
	}
	
	public int getX()
	{
		return _x;
	}
	
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
			
			sleep( _ctrlr.getMoveDelay() );
		}
	}
	
	protected abstract void getNextMove();
}
