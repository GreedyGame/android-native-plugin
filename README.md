 GreedyGame Android Native Integration Guide
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. 

Before we start the Integration lets get familiarized with the following terms.

* **Native-Units** : The non clickable textures inside your game that is dynamically branded natively at runtime.
* **Float-Units** : The clickable ad unit from greedygame. On click of this unit, Engagement window is opened which essentially allows brands to interact with the users.

You can download Android library project named, [greedy-game-agent](https://github.com/GreedyGame/android-native-plugin/releases).

| Original       | Dynamic Theme      |
| ------------- | ----------- |
| ![Sun Original Theme](screen-shots/sun.png?raw=true "Sun Original Theme" ) | ![Moon Dynamically changed Theme](screen-shots/moon.png?raw=true "Moon Dynamically changed Theme" ) |

#### Steps

* Link android library project to build path
* Put your gameprofile id in Android xml values as string

    ```xml
    <resources>
        <string name="greedy_game_profile">11111111</string>
    </resources>
    ```
    
#### **PART 1 : Initialization and Native Ads**

#### **GreedyGameAgent - Install**

Install the GreedygameAgent in splash screen activity's (  launcher activity ) onCreate() method.

##### `public static GreedyGameAgent install(Activity activity, IAgentListener agentListener);`

```java
GreedyGameAgent ggAgent = GreedyGameAgent.install(this,listener);
```
#### **GreedyGameAgent - Callbacks From IAgentListener**
A seperate section is detailing about IAgentListener in this  Readme file itself. Please go through it while designing your callbacks for GreedyGameAgent.


#### **GreedyGameAgent - init()**
Initialize GreedyGameAgent and notify it regarding the assets that you are going to use in your game.

**Step 1 :** Create an array of units that you are using inside your game like shown below. You should be adding both the **float** units and the **native** units to the array. There are two ways of using the native units as shown in example 1 and 2 respectively.

**Step 2 :** Call `init()` function of GreedyGameAgent with the string array you have created and also specify the type of download you want to use.

**Example 1 :** ( If you want to download the native units by ID )
```java
String[] units = {"unit-1522","unit-1941","float-1744","float-1743"};
ggAgent.init(units, FetchType.DOWNLOAD_BY_ID);
```

**Example 2 :** (If you want to download native units by path )
```java
String[] units = {"sun.png","moon.png","float-1744","float-1743"};
ggAgent.init(units, FetchType.DOWNLOAD_BY_PATH);
```
>**NOTE** 
>1. Float units are always passed with ID's. 
>2. For each native unit that you upload to panel.greedygame.com a particular unit-id is generated. Use those ID's while DOWNLOADING_BY_ID.
>3. Call ggAgent.init() function only inside onCreate of your  starting activity.


#### **GreedyGameAgent - other utility functions**
##### **public String activeCampaign()**
Return campaign id of currently active and running campaign on device.

##### **public String getActivePath()**
 Return path of folder, where assets of activeCampaign is stored. You can use this method to actually replace the default unit with branded texture !

> Note : getActivePath will return null if there is no active theme 

#### **GreedyAndroidWrapper - utility  functions**
##### **Get Bitmap by Resource Name**
```java
public static  Bitmap getBitmapByResName(GreedyGameAgent ggAgent, String resName) 
```
This utility function will check if a branded unit is available with the name "resName". At runtime you can replace your default texture with the branded texture using this utility function as shown below.
```java
 ImageView home = (ImageView) findViewById(R.id.player);
            Bitmap bmp2 = getBitmapByResName(GreedyGameAgent.getInstance(), "unit-1941");
            home.setImageBitmap(bmp2);
```
### PART 2 : FloatAdLayout - For Clickable Ads
**Class Overview**

Extended FrameLayout used to display FloatAd creatives.

**Initializing FloatAdLayout**
##### `FloatAdLayout(Context context)`

Constructs a new instance of FloatAdLayout. Remember to add it to the current activity context using the following code. 
```java
FloatAdLayout floatAdLayout = new FloatAdLayout(context);
FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addContentView(floatAdlayout, params);
```

**Display float unit **
```java
public void fetchHeadAd(String unit_id) throws AgentInitNotCalledException
```
Whenever you need to display a float-unit ( clickable unit ) use this function. 

* **unit_id** - Float unit id from panel.greedygame.com (e.g 'float-123')
* **AgentInitNotCalledException** - throws exception if called before calling GreedyGameAgent's init callback.
    
```java
/*** Fetching Float Ad unit ***/
floatAdlayout.fetchHeadAd("float-111");

```
**Remove float unit **
Whenever you need to hide a float-unit or if you need to load a new float unit then call the below function to remove the previous float unit from screen !
```java
/*** Fetching Float Ad unit ***/
floatAdlayout.remove();

```

### Other Utilities Methods

Set sdk into debug mode
```java
 public void setDebugCampaign(boolean b)
```

Enable debug logs
```java
 public void setDebugLog(boolean b)
```

### **Interface IAgentListener**
**Class Overview**
It is used as a callback listener and is passed as an argument for GreedyGameAgent.install() method.

**Methods inside IAgentListener**


#####**Callback - when GreedyGameAgent gets Initialized **
```java
void onInit(OnInitEvent response){
if(response == OnInitEvent.CAMPAIGN_NOT_AVAILABLE) {
	/* no campaign is available at the moment !
	 you can proceed with the game or skip to the 
	next scene */
	} else if(response ==OnInitEvent.CAMPAIGN_AVAILABLE) {
	/*In this case the download would have already begun in the background ! Either you can wait till it completes and you will get a callback in onDownload() function where you can skip to the next scene or you can continue without waiting and ads will appear as and when they are downloaded ! */
	}
```

>OnInitEvent enum contain values in
**CAMPAIGN_NOT_AVAILABLE** means no campaign is available
**CAMPAIGN_AVAILABLE** means there is an active campaign to download or already cached in device.


#####**Callback - when download of units get complete**
```java
void onDownload() {
// This callback notifies that the download of assets have been completed ! 
}
```
Called when new branded contents are downloaded so that new scene can fetch assets from getActivePath(). Called after onInit callback.

#####**Callback - when download of units fail**
```java
void onError() {
// This callback notifies that the download of assets have failed and no ads can be served in this session !
}
```
> **Important** : both `onDownload()` and `onError()` are called only after you get a callback on `onInit()`. If you get `CAMPAIGN_NOT_AVAILABLE` in `onInit()` function then no further callbacks will be supplied to `onSuccess` and `onError()`.

##### **Callback - when there are unavailable permissions**
```java
void onPermissionsUnavailable(ArrayList<String> permissions) 
```
This method needs to be used only if your game is targeting SDK version 23 or higher. This callback gives a list of permissions that are not available at runtime and is invoked after GreedyGameAgent initialization.

>**Note** : Only performs the check for 4 runtime permissions that are required by GreedyGameSDK.

Permissions that are checked :
```
Manifest.permission.ACCESS_COARSE_LOCATION
Manifest.permission.WRITE_EXTERNAL_STORAGE
Manifest.permission.GET_ACCOUNTS
Manifest.permission.READ_PHONE_STATE
```
>**Note** : The above strings itself are returned in the argument if they are not available.

#### **Sample Implementation of IAgentListener**

```java
class GGListner implements IAgentListener{
    @Override
    public void onProgress(float progress) {
        //Use this for showing progress bar
        Log.i("GreedyGame Sample", "Downloaded = "+progress+"%");
    }

    @Override
    public void onDownload() {
            isBranded = true;
    }

    @Override
    public void onInit(OnInitEvent response) {
        if(response == OnInitEvent.CAMPAIGN_AVAIALABLE ){
            //waiting for onDownload or onError callback ! 
        }else if(response == OnInitEvent.CAMPAIGN_NOT_AVAIALABLE ){
            isBranded = false;
        }
    }

    @override
    public void onPermissionsUnavailable(ArrayList<String> permissions) {

    }

    @override
    public void onError() {
		isBranded = false;
    }
}
```

#### **Manifest Requirement**
```xml
    
<application>
<!-- GreedyGame SDK's requirements start -->
   <activity
 android:name="com.greedygame.android.adhead.GGAdHeadActivity"
 android:configChanges="keyboardHidden|orientation|screenSize|screenLayout|layoutDirection"
 android:launchMode="singleTask"
 android:theme="@style/Theme.Transparent">
  </activity>


    <receiver
 android:name="com.greedygame.android.agent.GreedyRefReceiver"
 android:enabled="true"
 android:exported="true">
<intent-filter>
<action android:name="com.android.vending.INSTALL_REFERRER"/>
<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
<action android:name="com.greedygame.broadcast"/>
</intent-filter>
</receiver>
<!-- GreedyGame SDK's requirements end -->
</application>

```

#### **Special Requirements**
**On Game Exit**
If you are using the following function to exit from the game 
`System.exit(0);` or `android.os.Process.killProcess(android.os.Process.myPid());`
then you should make sure that you are calling the below function just before exiting !
`GreedyGameAgent.getInstance().onActivityPaused((Activity) context);` 
>**Note** : This is applicable only for `System.exit(0);` or `android.os.Process.killProcess(android.os.Process.myPid());` if you are using `finish();` function inside the Activity then you **should not call** the above code snippet !

#### **Proguard Settings**

If you are using Proguard add the following to your Proguard settings ! 

`-keep class com.greedygame.android.** { *;}
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}`


### External Jars
GreedyGame SDK uses Volley from Google as external jars. In the case of conflicts you can remove it from libs folder of the wrapper. 

### For Hello Tutorial, goto [andorid-native-sample](andorid-native-sample)  
### For more help please see [FAQ] (https://github.com/GreedyGame/unity-plugin/wiki/FAQs)
