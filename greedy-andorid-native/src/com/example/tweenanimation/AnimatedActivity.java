package com.example.tweenanimation;


import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.greedygame.android.GreedyGameAgent;

public class AnimatedActivity extends Activity {
	
	Button startButton;
	Activity current;
	GreedyGameAgent ggAgent;
	private Builder _notiBuilder = null;
	private NotificationManager _notificationManager = null;
	private int _notiId;
	boolean isNew = false;

	class GG_Listner implements com.greedygame.android.IAgentListner{

		@Override
		public void onProgress(float progress) {
			if(_notiBuilder != null && _notificationManager != null) {
				_notiBuilder.setProgress(100, (int) progress, false)
							.setOngoing(true)						
							.setContentInfo( (int)progress+"%");
	            _notificationManager.notify(_notiId, _notiBuilder.build());
			}
		}

		@Override
		public void onDownload(boolean success) {
			if(success){
				if(_notiBuilder != null){
					_notiBuilder.setContentText("Download complete")
		        		.setProgress(0,0,false)
		        		.setOngoing(false)
		        		.setSmallIcon(android.R.drawable.stat_sys_download_done);
				}
			}else{
				if(_notiBuilder != null){
					_notiBuilder.setContentText("Network error occur while downloading. Touch to resume.")
					.setOngoing(false)
	        		.setProgress(0,0,false);
				}
			}

			if(_notificationManager != null){
				_notificationManager.notify(_notiId, _notiBuilder.build());
			}
		}

		@Override
		public void onInit(int arg1) {
			if(arg1 == 1){
				ggAgent.downloadByPath();
			}
			
			if(arg1 >= 0 || arg1 <= 2){
				isNew = true;
				if(arg1 >= 0 || arg1 <= 1){
					ggAgent.fetchHeadAd("unit-363");
				}
			}else{
				isNew = false;
			}
			
			
			sun = (ImageView)findViewById(R.id.sun);
			if(isNew){
				String p = ggAgent.getActivePath()+"/sun.png";
				final Bitmap bmp = BitmapFactory.decodeFile(p);
				sun.setImageBitmap(bmp);
			}
			
			startButton.setEnabled(true);
			
		}

		@Override
		public void onUnitClicked(boolean isPause) {
			// TODO Auto-generated method stub
			// Handle pause and un-pause
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
