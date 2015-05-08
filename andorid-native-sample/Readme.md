Tutorial to Integrate GreedyGame SDK to native android
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. You can download [GreedyGameAgent_v5.6.1.jar](libs/GreedyGameAgent_v5.6.1.jar).

#### Profile setup

* To setup game-profile at panel.greedygame.com visit [http://www.developer.greedygame.com/i18n-panel-getting-started-guide/] 
* Use GreedyPanel.py to upload game assets to native server.

#### Steps to integrate 

1. **Define and implement com.greedygame.android.IAgentListner**
Its use to handle callback and events as
	 * BUSY = loader busy right now
	 * CAMPAIGN_NOT_AVAILABLE = using no campaign
	 * CAMPAIGN_CACHED = campaign already cached
	 * CAMPAIGN_FOUND = new campaign found to download

For example as

```
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
		public void onInit(OnINIT_EVENT arg1) {

			if(arg1 == OnINIT_EVENT.CAMPAIGN_FOUND){
				ggAgent.downloadByPath();
			}
			
			if(arg1 == OnINIT_EVENT.CAMPAIGN_CACHED || arg1 == OnINIT_EVENT.CAMPAIGN_FOUND){
				isNew = true;
				ggAgent.fetchHeadAd("unit-363");
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
```

2. Define string array with file path, as
	`String[] units = {"sun.png"};`

3. Declare and call GreedyGameAgent object, as
	```
	ggAgent = new GreedyGameAgent(currentActivity, new GG_Listner());
	ggAgent.init("68712536", units);
	```
