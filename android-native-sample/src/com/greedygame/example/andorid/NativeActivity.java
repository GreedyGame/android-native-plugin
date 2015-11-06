package com.greedygame.example.andorid;

import com.greedygame.android.AgentInitNotCalledException;
import com.greedygame.android.FloatAdLayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class NativeActivity extends Activity {
	
	private FrameLayout gg_holder;
	private FloatAdLayout floatAdlayout = null;
	   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.native_layout);
    	
    	gg_holder = (FrameLayout) findViewById(R.id.gg_holder);
    	/*** Fetching Float Ad unit ***/
    	floatAdlayout = new FloatAdLayout(this);
    	
    	gg_holder.removeAllViews();
    	gg_holder.addView(floatAdlayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 140));
    	try {
			floatAdlayout.fetchHeadAd("unit-363", true);
		} catch (AgentInitNotCalledException e) {
			e.printStackTrace();
		}
    	
	}

}
