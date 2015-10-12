package com.greedygame.example.andorid;


import com.greedygame.android.FloatAdLayout;

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
	private Button f1,f2,f3;
	private Button r1,r2,r3;
	private Activity current;
	private ImageView sun = null;
	private String themePath = null;
	private FloatAdLayout floatAdlayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		current = this;
    	setContentView(R.layout.activity_animated);
    	
    	themePath = SplashScreenActivity.ggAgent.getActivePath();
    	floatAdlayout = new FloatAdLayout(this);
		
		startButton = (Button) findViewById(R.id.start);
		f1 = (Button) findViewById(R.id.fetch1);
		f2 = (Button) findViewById(R.id.fetch2);
		f3 = (Button) findViewById(R.id.fetch3);
		r1 = (Button) findViewById(R.id.remove1);
		r2 = (Button) findViewById(R.id.remove2);
		r3 = (Button) findViewById(R.id.remove3);
		
		if(themePath != null){
			f1.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.fetchHeadAd("unit-363");
			    }
			});
			f2.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.fetchHeadAd("unit-1014");
			    }
			});
			f3.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.fetchHeadAd("unit-1015");
			    }
			});
			
			r1.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.removeHeadAd("unit-363");
			    }
			});
			r2.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.removeHeadAd("unit-1014");
			    }
			});
			r3.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	floatAdlayout.removeHeadAd("unit-1015");
			    }
			});
		}
		
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
}
