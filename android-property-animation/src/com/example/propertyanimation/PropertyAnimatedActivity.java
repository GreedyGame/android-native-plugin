package com.example.propertyanimation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.ImageView;

import com.greedygame.android.GreedyGameAgent;
import com.greedygame.android.IAgentListner;


/**
 * This is demo code to accompany the Mobiletuts+ tutorial:
 * Android SDK: Creating a Simple Property Animation
 * 
 * Sue Smith
 * - Originally written February 2013
 */


//Game-ID 68712536

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PropertyAnimatedActivity extends Activity {
	
	GreedyGameAgent ggAgent;
	private Builder _notiBuilder = null;
	private NotificationManager _notificationManager = null;
	private int _notiId;
	
	boolean isNew = false;
	
	
	class GG_Listner implements IAgentListner{

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
					ggAgent.fetchHeadAd("unit-355");
				}
			}else{
				isNew = false;
			}
			

			
			Log.i("DummyGame", "isNew "+ isNew+ ",r "+arg1);
			startAnimation();

		}

		@Override
		public void onUnitClicked(boolean isPause) {
			// TODO Auto-generated method stub
			// Handle pause and un-pause
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_animated);
		
		ggAgent = new GreedyGameAgent(this, new GG_Listner());
		ggAgent.setDebug(true);
		
		String[] units = {"cloud.png", "steering_wheel.png", "sun.png"};
		ggAgent.init("68712536", units);


		
	}
	
	void startAnimation(){
		//wheel animation
		ImageView wheel = (ImageView)findViewById(R.id.wheel);
		//load the wheel animation
		AnimatorSet wheelSet = (AnimatorSet) 
				AnimatorInflater.loadAnimator(this, R.animator.wheel_spin);
		//set the view as target
		wheelSet.setTarget(wheel);
		//start the animation
		wheelSet.start();

		//get the sun view
		ImageView sun = (ImageView)findViewById(R.id.sun);
		if(isNew){
			String p = ggAgent.getActivePath()+"/sun.png";
			final Bitmap bmp = BitmapFactory.decodeFile(p);
			sun.setImageBitmap(bmp);
		}
		
		
		//load the sun movement animation
		AnimatorSet sunSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_swing);
		//set the view as target
		sunSet.setTarget(sun);
		//start the animation
		sunSet.start();

		//darken sky
		ValueAnimator skyAnim = ObjectAnimator.ofInt
				(findViewById(R.id.car_layout), "backgroundColor",
						Color.rgb(0x66, 0xcc, 0xff), Color.rgb(0x00, 0x66, 0x99));
		skyAnim.setDuration(3000);
		skyAnim.setRepeatCount(ValueAnimator.INFINITE);
		skyAnim.setRepeatMode(ValueAnimator.REVERSE);
		skyAnim.setEvaluator(new ArgbEvaluator());
		skyAnim.start();

		//move clouds
		ObjectAnimator cloudAnim = 
				ObjectAnimator.ofFloat(findViewById(R.id.cloud1), "x", -350);
		cloudAnim.setDuration(3000);
		cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
		cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
		cloudAnim.start();
		//other cloud
		ObjectAnimator cloudAnim2 = ObjectAnimator.ofFloat(findViewById(R.id.cloud2), "x", -300);
		cloudAnim2.setDuration(3000);
		cloudAnim2.setRepeatCount(ValueAnimator.INFINITE);
		cloudAnim2.setRepeatMode(ValueAnimator.REVERSE);
		cloudAnim2.start();
	}

}
