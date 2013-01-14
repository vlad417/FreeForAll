package org.vxp7755_nxz3937.freeforall;

import android.os.Message;
import android.util.Log;

public class SpawnerThread extends Thread {

	private ControllerThread _ctrlr;
	
	SpawnerThread( ControllerThread parent )
	{
		_ctrlr = parent;
	}
	
	@Override
	public void run()
	{
		Message spawnMsg = _ctrlr.boardHandler.obtainMessage();
		spawnMsg.what    = _ctrlr.MSGTYPE_SPAWNER;
		spawnMsg.arg1    = _ctrlr.SPAWNTYPE_SYS;
		
		_ctrlr.boardHandler.sendMessage( spawnMsg );
		
		while( !_ctrlr.isPaused() )
		{
			try
			{
				sleep( _ctrlr.getSpawnDelay() );
			} catch ( InterruptedException ie )
			{
				Log.e( "SpawnerThread", "Interrupted sleep" );
				ie.printStackTrace();
			}
		}
	}
}
