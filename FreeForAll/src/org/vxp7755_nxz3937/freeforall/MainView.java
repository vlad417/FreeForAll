package org.vxp7755_nxz3937.freeforall;

import org.vxp7755_nxz3937.freeforall.MainView;
import org.vxp7755_nxz3937.freeforall.R;
import org.vxp7755_nxz3937.freeforall.MainView.RefreshHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainView extends View {
	
	private final int CELL_WIDTH  = 40;
	private final int CELL_HEIGHT = 40;
	public final int GRID_WIDTH  = 10;
	public final int GRID_HEIGHT = 10;
	public final int NUM_TEAMS   = 4;
	
	public static final int PAUSE = 0;
	public static final int RUNNING = 1;
	
	private Context _context;
	private int _mode;
	private Board _board;
	private Paint[] _cellColors;
	public ControllerThread _ctrlr;
	private boolean _drawing;
	
	
	public RefreshHandler _redrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
        	Log.i("RefreshHandler", "Received");
        	if( _mode == RUNNING )
        	{
        		Log.i("RefreshHandler", "Invalidating");
        		invalidate();
        	}
        }
    };

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this._context = context;
        
        _cellColors = new Paint[NUM_TEAMS+1];
		for(int c=0; c <= NUM_TEAMS; c++ )
			_cellColors[c] = new Paint();
        
		_cellColors[0].setColor(getResources().getColor(R.color.emptyCell));
		_cellColors[1].setColor(getResources().getColor(R.color.team1));
		_cellColors[2].setColor(getResources().getColor(R.color.team2));
		_cellColors[3].setColor(getResources().getColor(R.color.team3));
		_cellColors[4].setColor(getResources().getColor(R.color.team4));
        
		_ctrlr = new ControllerThread( this );
		//_ctrlr.start();
		
		this.setOnClickListener( new OnClickListener(){
			public void onClick( View v )
			{
				Message msg = _ctrlr.boardHandler.obtainMessage( _ctrlr.MSGTYPE_SPAWNER, _ctrlr.SPAWNTYPE_USER, 0 );
				_ctrlr.boardHandler.sendMessage( msg );
			}
		} );
		
		_mode = RUNNING;
		setFocusable(true);
	}
	
	/*
	@Override
	protected void onWindowVisibilityChanged( int visibility )
	{
		if( visibility == VISIBLE )
		{
			Log.i("MainView", "View is starting controller");
			_ctrlr.start();
			invalidate();
		}
	}
	*/
	
	@Override
	protected void onDraw( Canvas canvas )
	{
		PieceThread piece;
		Board board = _ctrlr.getBoard();
		
		Log.i( "MainView", "Redrawing");
		
		// Redraw the board
		for( int h=0; h < GRID_HEIGHT; h++ )
        	for( int w=0; w < GRID_WIDTH; w++ )
        	{
        		piece = board.getCell(w, h);
        		if( piece != null )
        			paintCell( canvas, w, h, piece.getTeam(), piece.getID() );
        		else
        			paintCell( canvas, w, h, 0, -1 );
        	}
		
		// Update scores
		int scores[] = board.getScores();
		Activity host = (Activity) getContext();
		TextView team1 = (TextView) host.findViewById(R.id.team1_score);
		TextView team2 = (TextView) host.findViewById(R.id.team2_score);
		TextView team3 = (TextView) host.findViewById(R.id.team3_score);
		TextView team4 = (TextView) host.findViewById(R.id.team4_score);
		
		if( team1 != null)
		{
			Log.i("MainView.onDraw", String.format("Updating scores [%s,%s,%s,%s]", 
					String.format("%d", scores[0]),
					String.format("%d", scores[1]),
					String.format("%d", scores[2]),
					String.format("%d", scores[3])));
			team1.setText( String.format("%d", scores[0]) );
			team2.setText( String.format("%d", scores[1]) );
			team3.setText( String.format("%d", scores[2]) );
			team4.setText( String.format("%d", scores[3]) );
			/*
			team1.invalidate();
			team2.invalidate();
			team3.invalidate();
			team4.invalidate();*/
		} else {
			Log.e("MainView.onDraw", "Could not find team_score views");
			
		}
		
		_drawing = false;
		
	}

	public void paintCell( Canvas canvas, int x, int y, int team, int id )
	{
		canvas.drawRect(
				x * CELL_WIDTH + x, // +x & +y to make padding around each cell
				y * CELL_HEIGHT + y, 
				(x * CELL_WIDTH) + (CELL_WIDTH -1) + x,
				(y * CELL_HEIGHT) + (CELL_HEIGHT -1) + y,
				_cellColors[team]);
		
		if( id >= 0 )
		{
			canvas.drawText(String.format("%d", id), x * CELL_WIDTH + x + (CELL_WIDTH/2), y * CELL_HEIGHT + y + (CELL_HEIGHT/2), _cellColors[0]);
		}
	}
	
	public void quitLooper()
	{
		Looper.myLooper().quit();
	}
	
	/** Returns true if the board is in the middle of being drawn */
	public boolean isDrawing() { return _drawing; }
	
	/**
	 * Set drawing to true.  Used if a message to redraw the board has been sent
	 */
	public void setDrawing() { _drawing = true; }
	
	/**
	 * Start the controller.  Should be called once the screen is unlocked
	 */
	public void startController() { 
		Log.i("MainView", "View is starting controller");
		_ctrlr.start();
		invalidate();
	}
	
	public double getMoveSpeed() {
		return _ctrlr.getMoveMultiplier();
	}
	
	public double getSpawnSpeed() {
		return _ctrlr.getSpawnMultiplier();
	}
}
