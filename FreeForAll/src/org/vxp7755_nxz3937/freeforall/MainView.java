package org.vxp7755_nxz3937.freeforall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class MainView extends View {
	
	private final int GRID_WIDTH  = 10;
	private final int GRID_HEIGHT = 10;
	
	private Context _context;

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this._context = context;
        
        for( int h=0; h < GRID_HEIGHT; h++ )
        	for( int w=0; w < GRID_WIDTH; w++ )
        	{
        		// Populate grid
        	}
	}

}
