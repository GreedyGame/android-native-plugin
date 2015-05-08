GreedyGame Android Native Integration Guide
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. You can download [GreedyGameAgent_v5.6.1.jar](current-sdk/GreedyGameAgent_v5.6.1.jar).

#### Steps

* Paste jar to your android project's libs folder or directly add to build path

### Documentations
#### GreedyGameAgent
**Class Overview**

Contains high-level classes encapsulating the overall GreedyGame ad flow and model.

**Public Constructors**
##### `GreedyGameAgent(Activity gameActivity, IAgentListner greedyListner)`

Constructs a new instance of GreedyGame handler.

----------

**Method**

##### `public void init(String GameId, String []Units)`
Lookup for new native campaign from server. 

* GameId - Unique game profile id from panel.greedygame.com
* Units - List of relative path of assets used in games. 
    Also register unit id can be used
    
##### `public String activeTheme()`
Return Theme id of currently active and running theme

##### `public String newTheme()`
Return Theme id of new theme from server

##### `public void download()`
 Download branded assets for new campaign by unit-ids.
 *Should be used inside IAgentListner.onInit.*
	
##### `public void downloadByPath()`
 Download branded assets for new campaign by relative path.
 *Should be used inside IAgentListner.onInit.*
	
##### `public void cancelDownload()`
 Cancel current campaign download
		
##### `public String getActivePath()`
 Return path of folder, where assets of activeTheme is stored.

----
**Floating Ad Method**

##### `public void fetchHeadAd(String unit_id)`
Fetch floating AdHead with unit-id

##### `public void fetchHeadAd(String unit_id, float x, float y)`
 Fetch floating AdHead with unit-id, at specific x, y with screen pixels
 
##### `public void removeHeadAd(String unit_id)`
Hide floating AdHead with unit-id

----
**Analytics Methods**
As name suggest, put following method inside Andorid main acitivity method.

##### `public void onStart()`
##### `public void onResume()`
##### `public void onRestart()`
##### `public void onPause()`
##### `public void onStop()`
##### `public void onDestroy()`

For example
```java
@Override
protected void onResume(){
    super.onResume();
    ggAgent.onResume();
}
```
----
**Other Utilities Methods**

##### `public String get_verison()`
Return sdk version
	
##### `public void setDebug(boolean b)`
Set sdk into debug mode
	
##### `public void useSecureConnection(boolean b)`
To use api with HTTPS


---
#### interface IAgentListner
**Class Overview**
Is is used as callback listener argument for GreedyAgent class

**Methods**
 
##### `void onInit(OnINIT_EVENT response)`
 	response value indicate
	 * BUSY = loader busy right now
	 * CAMPAIGN_NOT_AVAILABLE = using no campaign
	 * CAMPAIGN_CACHED = campaign already cached
	 * CAMPAIGN_FOUND = new campaign found to download

##### `void onDownload(boolean success)`
success true , If new branded contents are downloaded so that new scene can fetch assets from **getActivePath()**.

##### `void onUnitClicked(boolean clicked)`
 clicked true, if floating adhead unit is clicked so that game developer can manage game pause.

For example



```java
class GG_Listner implements IAgentListner{
	@Override
	public void onProgress(float progress) {
		//Use this for showing progress bar
        Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
	}
    
    @Override
	public void onDownload(boolean success) {
		if(success){
			isBranded = true;
		}
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

    	if(isBranded){
    		ggAgent.fetchHeadAd("unit-363");
    	}
    }


	@Override
	public void onUnitClicked(boolean clicked) {
		if(clicked) {
			//pause game
		}else {
			//resume/unpause game
		}
	}
```

#### Manifest Requirement

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<application>
	<service
    android:name="com.greedygame.android.BackgroundService"
    android:enabled="true" ></service>
<receiver 
	android:name="com.greedygame.android.GreedyAppReceiver" 
	android:enabled="true" 
	android:priority="100">
  <intent-filter>
    <action android:name="com.android.vending.INSTALL_REFERRER" />
    <action android:name="android.intent.action.PACKAGE_INSTALL" />
    <action android:name="android.intent.action.PACKAGE_ADDED" />
    <action android:name="android.intent.action.PACKAGE_REMOVED" />
    <action android:name="android.intent.action.PACKAGE_CHANGED" />
    <action android:name="android.intent.action.PACKAGE_FIRST_LAUNCH" />
    <action android:name="android.intent.action.PACKAGE_FULLY_REMOVED" />
    <action android:name="android.intent.action.PACKAGE_REPLACED" />
    <data android:scheme="package" />
  </intent-filter>
</receiver>
</application>
```
---
### For Hello Tutorial, goto [andorid-native-sample](andorid-native-sample)  




