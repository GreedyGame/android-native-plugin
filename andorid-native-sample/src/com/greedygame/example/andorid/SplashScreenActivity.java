package com.greedygame.example.andorid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.greedygame.android.GreedyGameAgent;
import com.greedygame.android.GreedyGameAgent.FETCH_TYPE;
import com.greedygame.android.GreedyGameAgent.OnINIT_EVENT;

public class SplashScreenActivity extends Activity {

	protected static GreedyGameAgent ggAgent;
	private final String[] units = {"sun.png"};
	private TextView loadingView = null;
	private float downloadProgress = 0;
	private Runnable updateProgress = null;
	class GG_Listner implements com.greedygame.android.IAgentListner{

		@Override
		public void onProgress(float progress) {
			Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
			downloadProgress = progress;
			runOnUiThread(updateProgress);
		}

		@Override
		public void onDownload(boolean success) {
			loadingView.setText("Loaded");
		}


		@Override
		public void onUnitClicked(boolean isPause) {
			// Handle pause and un-pause
			Log.i("GreedyGame Sample", "isPause = "+isPause);
		}

		@Override
		public void onInit(OnINIT_EVENT response) {
			if(response == OnINIT_EVENT.CAMPAIGN_NOT_AVAILABLE || response == OnINIT_EVENT.CAMPAIGN_CACHED){
				loadingView.setText("Loaded");
			}
		}
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.splash_layout);
    	
    	loadingView = (TextView) findViewById(R.id.loadingView);
		updateProgress = new Runnable() {
		     @Override
		     public void run() {
		    	 loadingView.setText("Loading... ["+downloadProgress+"% ]");
		    }
		};
		
		ggAgent = new GreedyGameAgent(this, new GG_Listner());
		ggAgent.setCurrentActivity(this);
		ggAgent.setDebug(true);
		ggAgent.init("68712536", units, FETCH_TYPE.DOWNLOAD_BY_PATH);

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	launch();
            }
        });
	}
	
	private void launch(){
		Log.i("Demo", "launch");
		
		Intent intent = new Intent(this, AnimatedActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ggAgent.onDestroy();
	}
}
