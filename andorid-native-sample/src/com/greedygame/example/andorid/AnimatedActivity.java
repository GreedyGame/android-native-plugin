package com.greedygame.example.andorid;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.greedygame.android.GreedyGameAgent;
import com.greedygame.android.GreedyGameAgent.FETCH_TYPE;
import com.greedygame.android.GreedyGameAgent.OnINIT_EVENT;

public class AnimatedActivity extends Activity {
	
	private Button startButton;
	private Activity current;
	private GreedyGameAgent ggAgent;
	private float downloadProgress = 0;
	private ImageView sun = null;
	private Runnable updateProgress = null;

	class GG_Listner implements com.greedygame.android.IAgentListner{

		@Override
		public void onProgress(float progress) {
			downloadProgress = progress;
			Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
			runOnUiThread(updateProgress);
		}

		@Override
		public void onDownload(boolean success) {
			setup(success);
		}


		@Override
		public void onUnitClicked(boolean isPause) {
			// Handle pause and un-pause
			Log.i("GreedyGame Sample", "isPause = "+isPause);
		}

		@Override
		public void onInit(OnINIT_EVENT response) {
			if(	response == OnINIT_EVENT.CAMPAIGN_CACHED){
				setup(true);
			}
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_animated);
		current = this;
		
		
		startButton = (Button) findViewById(R.id.start);
		startButton.setText("Loading...");
		startButton.setEnabled(false);
		
		updateProgress = new Runnable() {
		     @Override
		     public void run() {
				startButton.setText("Loading... ["+downloadProgress+"% ]");
		    }
		};
		
		ggAgent = new GreedyGameAgent(this, new GG_Listner());
		ggAgent.setDebug(true);
		
		String[] units = {"sun.png"};
		ggAgent.init("68712536", units, FETCH_TYPE.DOWNLOAD_BY_PATH);
		
	}
	
	void setup(boolean isBranded){
		
		sun = (ImageView) findViewById(R.id.sun);
		final Animation sunRise = AnimationUtils.loadAnimation(current, R.anim.sun_rise);
		startButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				sun.startAnimation(sunRise);
		    }
		});
		
		sun = (ImageView)findViewById(R.id.sun);
		if(isBranded){
			String p = ggAgent.getActivePath()+"/sun.png";
			final Bitmap bmp = BitmapFactory.decodeFile(p);
			sun.setImageBitmap(bmp);
			
			ggAgent.fetchHeadAd("unit-363");
		}
		startButton.setText("Start");
		startButton.setEnabled(true);
		
		
	}
}
