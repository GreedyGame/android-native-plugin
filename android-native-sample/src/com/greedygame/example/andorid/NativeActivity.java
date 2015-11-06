package com.greedygame.example.andorid;

import android.app.Activity;
import android.os.Bundle;

import com.greedygame.android.AgentInitNotCalledException;
import com.greedygame.android.FloatAdLayout;

public class NativeActivity extends Activity {
	
	private FloatAdLayout floatAdlayout = null;
	   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.native_layout);
    	
    	/*** Fetching Float Ad unit ***/
    	floatAdlayout = (FloatAdLayout) findViewById(R.id.gg_holder_1);
    	try {
			floatAdlayout.fetchHeadAd("unit-363", false);
		} catch (AgentInitNotCalledException e) {
			e.printStackTrace();
		}
    	
	}

}
