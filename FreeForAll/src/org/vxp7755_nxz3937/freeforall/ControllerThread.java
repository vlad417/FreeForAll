package org.vxp7755_nxz3937.freeforall;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import java.util.Random;
//import org.vxp7755_nxz3937.freeforall.R;

public class ControllerThread extends Thread {
	
	public final int MSGTYPE_MOVER   = 1;
	public final int MSGTYPE_SPAWNER = 2;
	public final int MSGTYPE_QUIT    = 3;
	
	public final int SPAWNTYPE_SYS   = 1;
	public final int SPAWNTYPE_USER  = 2;
	
	public final int DEFAULT_SPAWN_TIME = 6000;
	public final int DEFAULT_MOVE_TIME = 1000;

	private Random random = new Random();
	
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
				int[] dimensions = Board.getSize();
				int boardWidth = dimensions[0];
				int boardHeight = dimensions[1];
				
				if( msg.arg1 == SPAWNTYPE_SYS )
				{
					Log.i("BoardHandler", "System spawn request received");
					// generate 4 different coordinate sets
					int cords[][] = getUniqueCoordinates(4);
					
					for(int i = 0; i < 4; i++ ) {
						spawnPiece(cords[i][0], cords[i][1], (i+1));
					}
				}
				else // SPAWNTYPE_USER
				{
					Log.i("BoardHandler", "User spawn request received");
					// process top-left corner (team 1/red)
					spawnPiece(0,0,1);
					// process top-right corner (team 2/green)
					spawnPiece((boardWidth-1),0,2);
					// process bottom-right corner (team 3/ blue)
					spawnPiece(0,(boardHeight-1),3);
					// process bottom-left corner (team 4/yellow)
					spawnPiece((boardWidth-1),(boardHeight-1),4);
				}
			}
		};
			
		Looper.loop();
	}
	
	/**
	 * 
	 * @param team	team number of new piece to spawn (1-4)
	 * @param x		row to place new PiecesThread
	 * @param y		column to place new PieceThread
	 */
	private void spawnPiece(int x, int y, int team)
	{
		// consume PieceThread if cell is occupied
		if (board.getCell(x, y) != null) {
			board.getCell(x, y).eaten();
			board.givePoint(team);
		}
		
		// generate new PieceThread
		int pieceType = this.random.nextInt(4);
		PieceThread newPiece;
		switch(pieceType) {
			case 0:		newPiece = new LeftUp_PieceThread(x, y, team);
						break;
			case 1:		newPiece = new LeftDown_PieceThread(x, y, team);
						break;
			case 2:		newPiece = new RightUp_PieceThread(x, y, team);
						break;
			default:	newPiece = new RightDown_PieceThread(x, y, team);
						break;
		}
		
		// set new piece
		board.setCell(x, y, newPiece);
		
		// notify view of set new
		
		// tell PieceThread to run
		newPiece.run();
		
	}
	
	private int[][]getUniqueCoordinates( int numCoordinates )
	{
		
		int coordinates[][] = new int[numCoordinates][2];
		return coordinates;
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
	
	
	/**
	 * Retrieves the spawn delay used by the SpawnerThread
	 * 
	 * @return delay in ms, in between system-driven PieceThread spawns
	 */
	public int getSpawnDelay() {
		return this.DEFAULT_SPAWN_TIME;
	}
	
	
	/**
	 * Checks if the sim is currently paused
	 * 
	 * @return true if sim paused, otherwise false
	 */
	public boolean isPaused() {
		return this.paused;
	}
}