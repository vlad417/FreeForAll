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
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	public ControllerThread _ctrlr;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// set view to main.xml
		setContentView(R.layout.main);
		
		// click-handlers for buttons
		View pauseButton = findViewById(R.id.pause_button);
		pauseButton.setOnClickListener(this);
		View moveSpeedUpButton = findViewById(R.id.move_speedup_button);
		moveSpeedUpButton.setOnClickListener(this);
		View moveSlowDownButton = findViewById(R.id.move_slowdown_button);
		moveSlowDownButton.setOnClickListener(this);
		View spawnSpeedUpButton = findViewById(R.id.spawn_speedup_button);
		spawnSpeedUpButton.setOnClickListener(this);
		View spawnSlowDownButton = findViewById(R.id.spawn_slowdown_button);
		spawnSlowDownButton.setOnClickListener(this);
		
		// get reference to controller.
		MainView v = (MainView) getWindow().getDecorView().
				findViewById(R.id.mainView);
		_ctrlr = v._ctrlr;
		
	}

	/** Called when a click event occurs */
	public void onClick(View v) {
		double speed;
		switch (v.getId()) {
			//move_speedup action
			case R.id.move_speedup_button:	
				_ctrlr.moveSpeedUp();
				speed = ((MainView) findViewById(R.id.mainView)).getMoveSpeed();
				((TextView) findViewById(R.id.move_value)).setText(String.format("%d%%", (int)(100/speed)));
				break;
				//move_slowdown action
			case R.id.move_slowdown_button:	
				_ctrlr.moveSlowDown();
				speed = ((MainView) findViewById(R.id.mainView)).getMoveSpeed();
				((TextView) findViewById(R.id.move_value)).setText(String.format("%d%%", (int)(100/speed)));
				break;
			//spawn_speedup action
			case R.id.spawn_speedup_button:	
				_ctrlr.spawnSpeedUp();
				speed = ((MainView) findViewById(R.id.mainView)).getSpawnSpeed();
				((TextView) findViewById(R.id.spawn_value)).setText(String.format("%d%%", (int)(100/speed)));
				break;
			//spawn_slowdown action
			case R.id.spawn_slowdown_button: 
				_ctrlr.spawnSlowDown();
				speed = ((MainView) findViewById(R.id.mainView)).getSpawnSpeed();
				((TextView) findViewById(R.id.spawn_value)).setText(String.format("%d%%", (int)(100/speed)));
				break;
			// pause action
			case R.id.pause_button:		CheckBox box = (CheckBox) findViewById(
											R.id.pause_button);
										_ctrlr.switchPause(box.isChecked());
										break;
		}
	}
	
	/** Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity to start interacting with the user. */
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i("MainActivity","Gaining focus");
    	((MainView) findViewById(R.id.mainView)).startController();
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