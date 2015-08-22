package com.greedygame.example.andorid;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class AnimatedActivity extends Activity {
	
	private Button startButton;
	private Activity current;
	private ImageView sun = null;
	private String themePath = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(SplashScreenActivity.ggAgent!=null){
			SplashScreenActivity.ggAgent.setCurrentActivity(this);
			themePath = SplashScreenActivity.ggAgent.getActivePath();
		}
		
    	setContentView(R.layout.activity_animated);
		current = this;
		startButton = (Button) findViewById(R.id.start);
		setup();
	}
	
	void setup(){
		sun = (ImageView) findViewById(R.id.sun);
		final Animation sunRise = AnimationUtils.loadAnimation(current, R.anim.sun_rise);
		startButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				sun.startAnimation(sunRise);
				
		    }
		});
		
		sun = (ImageView)findViewById(R.id.sun);
		if(themePath != null){
			String p = themePath+"/sun.png";
			final Bitmap bmp = BitmapFactory.decodeFile(p);
			sun.setImageBitmap(bmp);
		}
		startButton.setText("Start");
		startButton.setEnabled(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(themePath != null && SplashScreenActivity.ggAgent!=null){
			SplashScreenActivity.ggAgent.fetchHeadAd("unit-363");
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(SplashScreenActivity.ggAgent!=null){
			SplashScreenActivity.ggAgent.removeHeadAd("unit-363");
		}
	}
	
}
