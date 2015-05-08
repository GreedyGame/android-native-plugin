Tutorial to Integrate GreedyGame SDK to native android
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. You can download [GreedyGameAgent_v5.6.1.jar](libs/GreedyGameAgent_v5.6.1.jar).

#### Profile setup

* To setup game-profile at panel.greedygame.com visit [http://www.developer.greedygame.com/i18n-panel-getting-started-guide/] 
* Use GreedyPanel.py to upload game assets to native server.

#### Steps to integrate 

1. Define and implement *com.greedygame.android.IAgentListner*. Its use to handle callback and events as
      * BUSY, loader busy right now
      * CAMPAIGN_NOT_AVAILABLE, using no campaign
      * CAMPAIGN_CACHED, campaign already cached
      * CAMPAIGN_FOUND, new campaign found to download

    For example as
    ```java
    class GG_Listner implements com.greedygame.android.IAgentListner{
        @Override
        public void onProgress(float progress) {
            //Percentage downloaded
        }

        @Override
        public void onDownload(boolean success) {
            //true on successful download
        }

        @Override
        public void onInit(OnINIT_EVENT arg1) {
            if(arg1 == OnINIT_EVENT.CAMPAIGN_FOUND){
                ggAgent.downloadByPath();
            }

            if(arg1 == OnINIT_EVENT.CAMPAIGN_CACHED || 
               arg1 == OnINIT_EVENT.CAMPAIGN_FOUND){
                isBranded = true;
            }else{
                isBranded = false;
            }

        }

        @Override
        public void onUnitClicked(boolean isClicked) {
            // TODO Auto-generated method stub
            // Handle pause and un-pause
        }	
    }
    ```

2. Define string array with file path, as
	```java
    String[] units = {"sun.png"};
    ```

3. Declare and call GreedyGameAgent object, as
	```java
	ggAgent = new GreedyGameAgent(currentActivity, new GG_Listner());
	ggAgent.init("68712536", units);
	```
    
4. Adding relative permission and service tag in manifest.xml
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

