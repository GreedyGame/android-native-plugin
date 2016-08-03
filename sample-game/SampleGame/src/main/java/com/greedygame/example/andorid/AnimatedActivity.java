package com.greedygame.example.andorid;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.greedygame.android.adhead.FloatUnitLayout;
import com.greedygame.android.exceptions.AgentInitNotCalledException;

public class AnimatedActivity extends Activity {
   
    private String themePath = null;
    private FloatUnitLayout floatUnitLayout = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_animated);
    	
    	themePath = SplashScreenActivity.ggAgent.getCampaignPath();
    	
    	/*** Fetching Float Ad unit ***/
    	floatUnitLayout = new FloatUnitLayout(this);
    	Log.i("GreedyGame Sample", "activePath "+themePath);
		
    	try {
    		floatUnitLayout.fetchFloatUnit("float-701", true);
    		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			((Activity) this).addContentView(floatUnitLayout, params);
		} catch (AgentInitNotCalledException e) {
			e.printStackTrace();
		}
    	
    	/*** Changing Native Ad units ***/
    	ImageView sun =  (ImageView) findViewById(R.id.sun);
    	
    	Bitmap bmp = getBitmapFromFileName("sun.png");
    	if(bmp != null){
    		sun.setImageBitmap(bmp);
    	}
	}

	private Bitmap getBitmapFromFileName(String s) {
		String file = SplashScreenActivity.ggAgent.getNativeUnitPathByName(s);
		Bitmap bitmap ;
		if(file!=null) {
			bitmap = BitmapFactory.decodeFile(file);
		} else {
			bitmap = null;
		}
		return bitmap;

	}

	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		floatUnitLayout.removeCurrentFloatUnit();
	}
	
	
	@Override
	public void onPause(){
		super.onPause();
	
	}


}
