/**
 * MainActivity.java
 * 
 * @author Vladimir Pribula
 * 
 * Copyright (C) 2010 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.vxp7755_nxz3937.freeforall;

//import org.vxp7755_nxz3937.freeforall.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TextView team1 = (TextView) findViewById(R.id.team1_score);
		team1.getAlpha();
		
		// click-handlers for buttons
		View pauseButton = findViewById(R.id.pause_button);
		pauseButton.setOnClickListener(this);
		View speedUpButton = findViewById(R.id.speedup_button);
		speedUpButton.setOnClickListener(this);
		View slowDownButton = findViewById(R.id.slowdown_button);
		slowDownButton.setOnClickListener(this);
		/* View spawnButton = findViewById(R.id.spawn_button);
		spawnButton.setOnClickListener(this);*/
		
	}

	/** Called when a click event occurs */
	public void onClick(View v) {
		switch (v.getId()) {
			//case R.id.exit_button:
				
		}
	}
	
	/** Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity to start interacting with the user. */
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i("MainActivity","Gaining focus");
    }
    
    /** Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed. */
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.i("MainActivity","Losing focus");
    }
    
    /** Perform any final cleanup before an activity is destroyed. */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i("MainActivity","Losing focus");
    }
}