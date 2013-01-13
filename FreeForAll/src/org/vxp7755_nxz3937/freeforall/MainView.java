package org.vxp7755_nxz3937.freeforall;

import org.vxp7755_nxz3937.freeforall.MainView;
import org.vxp7755_nxz3937.freeforall.R;
import org.vxp7755_nxz3937.freeforall.MainView.RefreshHandler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainView extends View {
	
	private final int CELL_WIDTH  = 40;
	private final int CELL_HEIGHT = 40;
	public final int GRID_WIDTH  = 10;
	public final int GRID_HEIGHT = 10;
	private final int NUM_TEAMS   = 4;
	
	public static final int PAUSE = 0;
	public static final int RUNNING = 1;
	
	private Context _context;
	private int _mode;
	private Board _board;
	private Paint[] _cellColors;
	private ControllerThread _ctrlr;
	
	
	public RefreshHandler _redrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
        	if( _mode == RUNNING )
        	{
        		MainView.this.invalidate();
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
		
		setFocusable(true);
	}
	
	@Override
	protected void onDraw( Canvas canvas )
	{
		PieceThread piece;
		Board board = _ctrlr.getBoard();
		
		// Redraw the board
		for( int h=0; h < GRID_HEIGHT; h++ )
        	for( int w=0; w < GRID_WIDTH; w++ )
        	{
        		piece = board.getCell(w, h);
        		if( piece != null )
        			paintCell( canvas, w, h, piece.getTeam() );
        		else
        			paintCell( canvas, w, h, 0 );
        	}
		
		// Update scores
		int scores[] = board.getScores();
		TextView team1 = (TextView) findViewById(R.id.team1_score);
		TextView team2 = (TextView) findViewById(R.id.team2_score);
		TextView team3 = (TextView) findViewById(R.id.team3_score);
		TextView team4 = (TextView) findViewById(R.id.team4_score);
		
		if( team1 != null)
		{
			team1.setText( scores[0] );
			team2.setText( scores[1] );
			team3.setText( scores[2] );
			team4.setText( scores[3] );
		}
	}

	public void paintCell( Canvas canvas, int x, int y, int team )
	{
		canvas.drawRect(
				x * CELL_WIDTH + x, // +x & +y to make padding around each cell
				y * CELL_HEIGHT + y, 
				(x * CELL_WIDTH) + (CELL_WIDTH -1) + x,
				(y * CELL_HEIGHT) + (CELL_HEIGHT -1) + y,
				_cellColors[team]);
	}
}
