package com.greedygame.example.andorid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.agent.GreedyGameAgent.FetchType;
import com.greedygame.android.agent.GreedyGameAgent.OnInitEvent;
import com.greedygame.android.agent.IAgentListener;


public class SplashScreenActivity extends Activity {

	protected static GreedyGameAgent ggAgent;
	private final String[] units = {"sun.png","unit-12345","float-701","float-713","float-10000"};
	private TextView loadingView = null;
	private float downloadProgress = 0;
	private Runnable updateProgress = null;
	private Runnable updateText = null;
	private ProgressBar progressBar;
	
	 
	class GG_Listener implements IAgentListener{

		@Override
		public void onProgress(float progress) {
			Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
			downloadProgress = progress;
			runOnUiThread(updateProgress);
		}

		@Override
		public void onDownload() {
			runOnUiThread(updateText);
		}



		@Override
		public void onInit(OnInitEvent response) {
			Log.i("GreedyGame Sample", "response = "+response);
			if(response == OnInitEvent.CAMPAIGN_NOT_AVAILABLE || response == OnInitEvent.CAMPAIGN_AVAILABLE
				){
				runOnUiThread(updateText);
			}
		}

		

		@Override
		public void onError() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onPermissionsUnavailable(ArrayList<String> arg0) {
			// TODO Auto-generated method stub
			
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
		
		ggAgent = GreedyGameAgent.install(this, new GG_Listener());
		ggAgent.setDebug(false);
		ggAgent.init(units, FetchType.DOWNLOAD_BY_PATH);
		
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
