package com.greedygame.example.andorid;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignStateListener;

public class AnimatedActivity extends Activity {


	ImageView sun;
	Button buttonRefresh,buttonForced,buttonUII;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_animated);

		GreedyGameAgent.init(this);
		sun =  (ImageView) findViewById(R.id.sun);

		GreedyGameAgent.setCampaignStateListener(new CampaignStateListener() {
			@Override
			public void onFound() {

			}

			@Override
			public void onUnavailable() {

			}

			@Override
			public void onAvailable() {
				Toast.makeText(getApplication(),"sample available",Toast.LENGTH_SHORT).show();
				changeTexture();
			}

			@Override
			public void onError(String error){

			}

		});


		buttonRefresh = (Button) findViewById(R.id.click_refresh);
		buttonRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GreedyGameAgent.startEventRefresh();
			}
		});

		buttonForced = (Button) findViewById(R.id.click_exit);
		buttonForced.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GreedyGameAgent.forcedExit();
			}
		});

		buttonUII = (Button) findViewById(R.id.click_uii);
		buttonUII.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GreedyGameAgent.Float.showUII("float-2014");
			}
		});

		changeTexture();
	}

	public void changeTexture() {
		String file = GreedyGameAgent.Native.getPath("unit-3342");
		Bitmap bitmap ;
		if(file!=null) {
			bitmap = BitmapFactory.decodeFile(file);
			sun.setImageBitmap(bitmap);
		} else {
			bitmap = null;
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		/*** Fetching Float Ad unit ***/
		GreedyGameAgent.Float.show(this,"float-2014");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	@Override
	public void onPause(){
		super.onPause();
		/*** Fetching Float Ad unit ***/
		GreedyGameAgent.Float.remove("float-2014");
	
	}


}
