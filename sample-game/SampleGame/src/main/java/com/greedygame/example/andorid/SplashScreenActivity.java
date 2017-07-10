package com.greedygame.example.andorid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignProgressListener;
import com.greedygame.android.core.campaign.CampaignStateListener;


public class SplashScreenActivity extends Activity {

	protected static GreedyGameAgent ggAgent;
	private final String[] units = {"sun.png","float-701","float-713"};
	private TextView loadingView = null;
	private float downloadProgress = 0;
	private Runnable updateProgress = null;
	private Runnable updateText = null;
	private ProgressBar progressBar;
	
	 
	class StateListener implements CampaignStateListener {


		@Override
		public void onFound() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAvailable() {
			// TODO Auto-generated method stub
			runOnUiThread(updateText);
		}

		@Override
		public void onError(String s) {

		}

		@Override
		public void onUnavailable() {
			// TODO Auto-generated method stub
			runOnUiThread(updateText);
		}

}


	class ProgressListener implements CampaignProgressListener {
		@Override
		public void onProgress(int progress) {
			Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
			downloadProgress = progress;
			runOnUiThread(updateProgress);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.splash_layout);
		progressBar = (ProgressBar) findViewById(R.id.progressbar2);
		  
		 
		loadingView = (TextView) findViewById(R.id.loadingView);
		updateProgress = new Runnable() 
		{
			@Override
			public void run() 
			{
				progressBar.setProgress((int)downloadProgress);
				loadingView.setText("Loading... ["+(int)downloadProgress+"% ]");
			}
		};
		
		updateText = new Runnable() 
		{
			@Override
			public void run() 
			{
				
				loadingView.setText("Finished Loading");
			}
		};
		
		GreedyGameAgent.init(this);
		GreedyGameAgent.setCampaignProgressListener(new ProgressListener());
		GreedyGameAgent.setCampaignStateListener(new StateListener());

		
		final Activity thisActivity = this;
		Button b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(thisActivity, AnimatedActivity.class);
				startActivity(intent);
	        }
	    });

		
	}
	

}
