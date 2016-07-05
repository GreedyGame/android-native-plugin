GreedyGame Android's SDK Reference
---------------------
 * [Introduction](#introduction)
 * [Requirements](#requirements)
 * [Integration](#integration)
 * [Documentation](#documentation)
    * [GreedyGameAgent](#greedygameagent)
    * [interface IAgentListener](#interface-iagentlistener)
    * [FloatUnitLayout](#floatunitlayout)
 * [FAQ](#faq)

 
# Introduction
Before we get started with the detailed reference, let’s brush through the definitions of some important terms that we’ll see referenced regularly. [Here at greedygame.github.io] (http://greedygame.github.io/)

# Requirements
* Android API Version: 14
* Gradle 1.6 or lastest

# Integration
If you have gone through the definitions of important key words. To make the rest of the integration an absolute breeze for you, we’ve set up an integration wizard on your [publisher panel](http://publisher.greedygame.com).

Once you’ve logged in, on the top of your page and select **SDK Integration Wizard** and we’ll walk you through the integration from the comfort of your own publisher panel.

![PublisherPanel's top menu](http://greedygame.github.io/images/wizard.png "SDK Integration Wizard")


# Documentation
### GreedyGameAgent
#### Class Overview
Contains high-level classes encapsulating the overall GreedyGame ad flow and model.
#### Public Constructor
##### `GreedyGameAgent(Activity gameActivity, IAgentListner greedyListner)`
Constructs a new instance of GreedyGame handler.
Install the GreedygameAgent in the activity

#### Public Methods
##### `public static GreedyGameAgent install(Activity activity, IAgentListener agentListener)`
Create and return singleton instance of GreedyGame class.

##### `public void init()`
Lookup for an active campaign from the server.

##### `public String getNativeUnitPathById(String unit_id)`
Return path of the [nativeunit](http://greedygame.github.io/#nativeunits), with unit_id, being used in current campaign or null.

##### `public String getNativeUnitPathByName(String name)`
Return path of the [nativeunit](http://greedygame.github.io/#nativeunits), with name, being used in current campaign or null.

##### `public String getFloatUnitPathById(String unit_id)`
Return path of the [floatunit](http://greedygame.github.io/#floatunits), with unit_id, being used in current campaign or null.

##### `public String[] getAdUnitIds()`
Return array of all adunit's id used in the game

##### `public String[] getNativeUnitIds()`
Return array of all [nativeunit](http://greedygame.github.io/#nativeunits)'s id used in the game

##### `public String[] getNativeUnitNames()`
Return array of all [nativeunit](http://greedygame.github.io/#nativeunits)'s name used in the game

##### `public String[] getFloatUnitIds()`
Return array of all [floatunit](http://greedygame.github.io/#floatunits)'s id used in the game

##### `public void showEngagementWindow(string unit_id)`
Open [engagement window](http://greedygame.github.io/#engagementwindow) attached with provided floatunit

##### `public String getCampaignPath()`
Return path of folder, where assets of current campaign is stored.

##### `public String getVersion()`
Return version of SDK

##### `public void setDebugCampaign()`
Request SDK to fetch the dummy campaign from server

##### `public void setDebugLog()`
Control display of debug of SDK 

##### `public void forcedExit()`
Call this method if game is being exit as
* `System.exit(0);` or
* `android.os.Process.killProcess(android.os.Process.myPid());`

**Note**: if you are using `finish();` function inside the Activity then you **should not call** the above code snippet

----
### interface IAgentListener
#### Class Overview
It is used as a callback listener argument for GreedyGameAgent class

#### Public Methods
##### `void onAvailable()`
When a new campaign is available and ready to use for the next scene.

##### `void onUnavailable()`
When no campaign is available

##### `void onProgress(int progress)`
Gives progress of campaign being downloaded as an integer.

##### `void onPermissionsUnavailable(ArrayList permissions)`
Gives a list of permission unavailable or revoked by the user.

**Permissions that are checked**
```
Manifest.permission.ACCESS_COARSE_LOCATION
Manifest.permission.WRITE_EXTERNAL_STORAGE
Manifest.permission.READ_PHONE_STATE
```
**Interface Example**
```java
class GreedyAgentListener implements IAgentListener {

    @Override
    public void onAvailable() {
        /**
         * TODO: New campaign is available and ready to use for the next scene.
         **/
    }

    @Override
    public void onUnAvailable() {
        /**
         * TODO: No campaign is available, proceed with normal follow of the game.
         **/
    }

    @Override
    public void onProgress(int progress) {
        /**
         * TODO: Progress bar can be shown using progress value from 0 to 100.
         **/
    }

    @Override
    public void onPermissionsUnavailable(ArrayList<String> permissions) {
        /**
         * TODO: Prompt user to give required permission
         **/
        for(int i = 0; i < permissions.size(); i++) {
            String p = permissions.get(i);
            System.out.print("permission unavailable = "+p);
        }
    }
}
```

-----
### FloatUnitLayout
#### Class Overview
Extended FrameLayout used to display FloatAd creatives

#### Public Constructors
##### `FloatUnitLayout(Context context)`
Constructs a new instance of FloatUnitLayout.

#### Public Methods
##### `public void fetchFloatUnit(String unit_id) throws AgentInitNotCalledException`
Fetch FloatUnit and add view to current context.

**For example**
```java
/*** Fetch Float Unit ***/
FloatUnitLayout floatUnitlayout = new FloatUnitLayout(this);

/*** Fetch Float Unit ***/
try {
  floatUnitlayout.fetchFloatUnit("float-1874", true);
  FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  ((Activity) this).addContentView(floatUnitlayout, params);
} catch (AgentInitNotCalledException e) {
  e.printStackTrace();
}

/*** Remove Float Unit ***/
floatUnitlayout.removeAllFloatUnits ();
```

### Proguard Settings

If you are using Proguard add the following to your Proguard settings ! 
```
-keep class com.greedygame.android.** { *;}
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
 }
```
 
# FAQ
For more help please see [FAQ](https://github.com/GreedyGame/unity-plugin/wiki/FAQs)
