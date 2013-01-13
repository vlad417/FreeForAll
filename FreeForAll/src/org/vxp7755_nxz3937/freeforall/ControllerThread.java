package org.vxp7755_nxz3937.freeforall;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
//import org.vxp7755_nxz3937.freeforall.R;

public class ControllerThread extends Thread {
	
	public final int MSGTYPE_MOVER   = 1;
	public final int MSGTYPE_SPAWNER = 2;
	public final int MSGTYPE_QUIT    = 3;
	
	public final int SPAWNTYPE_SYS   = 1;
	public final int SPAWNTYPE_USER  = 2;
	
	public final int DEFAULT_SPAWN_TIME = 6000;
	public final int DEFAULT_MOVE_TIME = 1000;

	public Handler boardHandler;
	private Board board;
	private boolean paused;
	private double speedMultiplier;
	private MainView ui;
	
	ControllerThread( MainView gui )
	{
		paused          = false;
		speedMultiplier = 1.0;
		ui              = gui;
		board           = new Board( ui.GRID_WIDTH, ui.GRID_HEIGHT );
	}
	
	@Override
	public void run()
	{
		Looper.prepare();
		
		boardHandler = new Handler(){
			@Override
			public void handleMessage( Message msg )
			{
				if( !paused )
				{
					if( msg.what == MSGTYPE_MOVER )
					{
						handleMover( (MoveObject)msg.obj );
					} 
					else if( msg.what == MSGTYPE_SPAWNER )
					{
						handleSpawner( msg );
					}
					else
					{
						Log.i("BoardHandler", "Quit request received");
						this.getLooper().quit();
					}
				}
			}
			
			
			private void handleMover( MoveObject mover )
			{
				PieceThread cell = board.getCell( mover.x, mover.y );
				
				if( cell != null ) // If cell occupied
				{
					cell.eaten();
					board.givePoint( cell.getTeam() );
				}
				
				// Update board cells
				board.setCell( mover.me.getX(), mover.me.getY(), null);
				board.setCell( mover.x, mover.y, mover.me );
				
				// Send messages to UI to update the cell moved from and moved to
				Handler uiHandler = ui.getHandler();
				Message updateMsg = uiHandler.obtainMessage();
				
				UpdateData updateOldLoc = new UpdateData( mover.me.getX(), mover.me.getY(), 0, board.getScores() );
				UpdateData updateNewLoc = new UpdateData( mover.x, mover.y, 0, board.getScores() );
				
				updateMsg.obj = updateOldLoc;
				uiHandler.sendMessage(updateMsg);
				updateMsg.obj = updateNewLoc;
				uiHandler.sendMessage(updateMsg);
			}
			
			private void handleSpawner( Message msg )
			{
				if( msg.arg1 == SPAWNTYPE_SYS )
				{
					Log.i("BoardHandler", "System spawn request received");
				}
				else // SPAWNTYPE_USER
				{
					Log.i("BoardHandler", "User spawn request received");
				}
			}
		};
		
		
		Looper.loop();
	}
	
	/** Call the UI to redraw board */
	public void drawCell()
	{
		ui._redrawHandler.sendEmptyMessage( 0 );
	}
	
	/** Get the board */
	public Board getBoard() { return board; }
	
	
	/**
	 * Decrease delay multiplier
	 */
	public void speedUp()
	{
		if (this.speedMultiplier > 0.25) {
			this.speedMultiplier -= 0.25;
		}
		Log.i("SpeedUp", String.format("SpeedMultiplier = %f",
											this.speedMultiplier));
	}
	
	
	/**
	 * Increase delay multiplier
	 */
	public void slowDown()
	{
		if (this.speedMultiplier < 2.0 ) {
			this.speedMultiplier += 0.25;
		}
		Log.i("SlowDown", String.format("SpeedMultiplier = %f",
				this.speedMultiplier));
	}
	
	/**
	 * Modify whether or not the simulation is pause
	 * 
	 * @param value true to pause the sim, otherwise false
	 */
	public void switchPause( boolean value )
	{
		this.paused = value;
		if (this.paused) {
			Log.i("switchPause", "paused = true");
		} else {
			Log.i("switchPause", "paused = false");
		}
	}
	
	/**
	 * Retrieves the move delay used by PieceThreads
	 * 
	 * @return delay in ms, which PieceThe reads should wait between moving
	 */
	public int getMoveDelay()
	{
		return (int) (this.speedMultiplier * this.DEFAULT_MOVE_TIME);
	}
}
