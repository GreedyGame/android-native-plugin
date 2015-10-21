GreedyGame Android Native Integration Guide
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. You can download Android library project named, [greedy-game-agent](greedy-game-agent).

| Original       | Dynamic Theme      |
| ------------- | ----------- |
| ![Sun Original Theme](screen-shots/sun.png?raw=true "Sun Original Theme" ) | ![Moon Dynamically changed Theme](screen-shots/moon.png?raw=true "Moon Dynamically changed Theme" ) |

#### Steps

* Link android library project to build path

### Documentations
#### GreedyGameAgent
**Class Overview**

Contains high-level classes encapsulating the overall GreedyGame ad flow and model.

**Public Constructors**
##### `GreedyGameAgent(Activity gameActivity, IAgentListner greedyListner)`

Constructs a new instance of GreedyGame handler.

----------

**Method**

##### `public void init(String GameId, String []Units, FETCH_TYPE)`
Lookup for new native campaign from server. 

* GameId - Unique game profile id from panel.greedygame.com
* Units - List of relative path of assets used in games. 
    Also register unit id can be used
* FETCH_TYPE - it can be FETCH_TYPE.DOWNLOAD_BY_PATH or FETCH_TYPE.DOWNLOAD_BY_ID, to fetch units by relative path or u_id
    
##### `public String activeTheme()`
Return Theme id of currently active and running theme

##### `public String newTheme()`
Return Theme id of new theme from server
    
##### `public void cancelDownload()`
 Cancel current campaign download
        
##### `public String getActivePath()`
 Return path of folder, where assets of activeTheme is stored.

----

#### FloatAdLayout - For Floating ads
**Class Overview**

Extended FrameLayout used to display FloatAd creatives

**Public Constructors**
##### `FloatAdLayout(Context context)`

Constructs a new instance of FloatAdLayout.

----------

**Method**

##### `public void fetchHeadAd(String unit_id) throws AgentInitNotCalledException`
Fetch floating AdHead unit and add view to current context. 

* unit_id - Float unit id from panel.greedygame.com (e.g 'f-123')
* AgentInitNotCalledException - throws exception if called before calling GreedyGameAgent's init callback.
    

##### `public void fetchHeadAd(String unit_id, int diX, int diY) throws AgentInitNotCalledException`
Fetch floating AdHead unit and add view to current context. 

* unit_id - Float unit id from panel.greedygame.com (e.g 'f-123')
* diX, diY - Adjust dip cordinates in screen. 0,0 stands for top left.
* AgentInitNotCalledException - throws exception if called before calling GreedyGameAgent's init callback.
 
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
##### `public void onCustomEvent(String eventName)`

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
        if(    response == OnINIT_EVENT.CAMPAIGN_CACHED || 
            response == OnINIT_EVENT.CAMPAIGN_FOUND){
            isBranded = true;
        }else{
            isBranded = false;
        }
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
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<application>
    <activity
        android:name="com.greedygame.android.AdHeadActivity"
        android:theme="@style/Theme.Transparent" >
    </activity>
    
    <service
        android:name="com.greedygame.android.GreedyBackgroundService"
        android:enabled="true" ></service>

    <receiver 
        android:name="com.greedygame.android.GreedyAppReceiver" 
        android:enabled="true" 
        android:priority="100">
        <intent-filter>
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

    <receiver 
        android:name="com.greedygame.android.GreedyRefReceiver" 
        android:enabled="true" 
        android:priority="100">
          <intent-filter>
            <action android:name="com.android.vending.INSTALL_REFERRER" />
          </intent-filter>
    </receiver>
</application>
```
---
### Some helper functions
To fetch drawable from android res

```java
public static Bitmap getBitmapByResid(GreedyGameAgent ggAgent, int resid){ 
    if(GreedyGameAgent.gameActivity == null){
        return null;
    }

    String resName = GreedyGameAgent.gameActivity.getApplicationContext().getResources().getResourceEntryName(resid);
    File file = new File(ggAgent.getActivePath() + "/" + resName+".png");
    if(file.exists()){
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    return null;
}
```

---
### For Hello Tutorial, goto [andorid-native-sample](andorid-native-sample)  

