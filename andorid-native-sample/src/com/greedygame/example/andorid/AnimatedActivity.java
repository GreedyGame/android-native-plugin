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
import com.greedygame.android.GreedyGameAgent.OnINIT_EVENT;

public class AnimatedActivity extends Activity {
	
	Button startButton;
	Activity current;
	GreedyGameAgent ggAgent;
	boolean isBranded = false;

	class GG_Listner implements com.greedygame.android.IAgentListner{

		@Override
		public void onProgress(float progress) {
			Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
		}

		@Override
		public void onDownload(boolean success) {
			if(success){
				isBranded = true;
			}
		}


		@Override
		public void onUnitClicked(boolean isPause) {
			// Handle pause and un-pause
			Log.i("GreedyGame Sample", "isPause = "+isPause);
		}

		@Override
		public void onInit(OnINIT_EVENT response) {
			if(response == OnINIT_EVENT.CAMPAIGN_FOUND){
				ggAgent.downloadByPath();
			}
			
			if(	response == OnINIT_EVENT.CAMPAIGN_CACHED || 
				response == OnINIT_EVENT.CAMPAIGN_FOUND){
				isBranded = true;
			}else{
				isBranded = false;
			}
			
			sun = (ImageView)findViewById(R.id.sun);
			if(isBranded){
				String p = ggAgent.getActivePath()+"/sun.png";
				final Bitmap bmp = BitmapFactory.decodeFile(p);
				sun.setImageBitmap(bmp);
				
				ggAgent.fetchHeadAd("unit-363");
			}
			startButton.setEnabled(true);
		}
		
	}
	
	ImageView sun = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_animated);
		current = this;
		
		sun = (ImageView) findViewById(R.id.sun);
		final Animation sunRise = AnimationUtils.loadAnimation(current, R.anim.sun_rise);
		
		startButton = (Button) findViewById(R.id.start);
		startButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				sun.startAnimation(sunRise);
		    }
		});
		
		startButton.setEnabled(false);
		
		ggAgent = new GreedyGameAgent(this, new GG_Listner());
		ggAgent.setDebug(true);
		
		String[] units = {"sun.png"};
		ggAgent.init("68712536", units);
		
	}
}
