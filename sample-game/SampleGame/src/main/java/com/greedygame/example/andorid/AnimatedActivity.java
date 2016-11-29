package com.greedygame.example.andorid;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import com.greedygame.android.agent.GreedyGameAgent;

public class AnimatedActivity extends Activity {
   

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_animated);
    	

    	/*** Changing Native Ad units ***/
    	ImageView sun =  (ImageView) findViewById(R.id.sun);
    	
    	Bitmap bmp = getBitmapFromFileName("sun.png");
    	if(bmp != null){
    		sun.setImageBitmap(bmp);
    	}
	}

	private Bitmap getBitmapFromFileName(String s) {
		String file = GreedyGameAgent.Native.getPath(s);
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
		/*** Fetching Float Ad unit ***/
		GreedyGameAgent.Float.show(this,"float-701");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	@Override
	public void onPause(){
		super.onPause();
		/*** Fetching Float Ad unit ***/
		GreedyGameAgent.Float.remove("float-701");
	
	}


}
