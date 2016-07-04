
GreedyGame Android Native Reference
===================

## Important Terms
Before we get started with the detailed reference, let’s brush through the definitions of some important terms that we’ll see referenced regularly. [Here at greedygame.github.io] (http://greedygame.github.io/)

## Documentations

#### GreedyGameAgent

Install the GreedygameAgent in the activity

##### `public GreedyGameAgent install(Activity activity, IAgentListener agentListener);`

---------

**Methods**

##### `public void init()`
Lookup for an active campaign from the server. 

##### `public String getCampaignPath()`
 Return path of folder, where assets of activeCampaign is stored.

----

### FloatAdLayout - For Floating ads
**Class Overview**

Extended FrameLayout used to display FloatAd creatives

**Public Constructors**
##### `FloatAdLayout(Context context)`

Constructs a new instance of FloatAdLayout.

----------

**Methods**

##### `public void fetchFloatUnit(String unit_id) throws AgentInitNotCalledException`
Fetch floating AdHead unit and add view to current context. 

* **unit_id** - Float unit id from panel.greedygame.com (e.g 'float-123')
* **AgentInitNotCalledException** - throws exception if called before calling GreedyGameAgent's init callback.
    
```java
/*** Fetching Float Ad unit ***/
floatAdlayout = new FloatAdLayout(context);
try {
    floatAdlayout.fetchFloatUnit("float-111");
} catch (AgentInitNotCalledException e) {
    e.printStackTrace();
}
```

----

**Other Utilities Methods**

##### `public String get_verison()`
Return sdk version
    
##### `public void setDebugLog(boolean b)`
Set sdk into debug mode

----

#### interface IAgentListener
**Class Overview**

It is used as a callback listener argument for GreedyGameAgent class

**Methods**
 
##### `void onAvailable()`
When a new campaign is available and ready to use for the next scene.

##### `void onUnavailable()`
When no campaign is available

##### `void onProgress(int progress)`
Gives progress of campaign being downloaded as an integer.

##### `void onPermissionsUnavailable(ArrayList permissions)`
Gives a list of permission unavailable or revoked by the user.

##### Permissions that are checked :

    Manifest.permission.ACCESS_COARSE_LOCATION
    Manifest.permission.WRITE_EXTERNAL_STORAGE
    Manifest.permission.READ_PHONE_STATE



Called when there is a problem with downloading the units.

For example

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

#### Manifest Requirement
```xml
    
<application>

   <!-- GreedyGame SDK's requirements start -->
   
    <activity
        android:name="com.greedygame.android.adhead.AdHeadActivity"
        android:configChanges="keyboardHidden|orientation|screenSize|screenLayout|layoutDirection"
        android:launchMode="singleTask"
        android:theme="@style/Theme.Transparent">
    </activity>


    <receiver
        android:name=".agent.GreedyRefReceiver"
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

**Recommended Manifest changes**

The GreedyGame SDK is connected with the activity lifecycle of your game. 
Generally Games works in either portrait or landscape mode for consistent user experience. Hence it is advisable **not** to recreate the activity on config changes which may happen due to power button press etc.

To avoid this immediate activity creation and destroying overhead, add the following configChanges in the Manifest file.

```
<activity
 android:name="com.package.YourActivity"
 android:configChanges="keyboardHidden|orientation|screenSize|screenLayout|layoutDirection">
</activity>
```

This is an **optimization** . The SDK will handle all related changes if your game supports multiple orientations.

#### **Special Requirements**
**On Game Exit**
If you are using the following function to exit from the game 
`System.exit(0);` or `android.os.Process.killProcess(android.os.Process.myPid());`
then you should make sure that you are calling the below function just before exiting !
`GreedyGameAgent.getInstance().onActivityPaused((Activity) context);` 
>**Note** : This is applicable only for `System.exit(0);` or `android.os.Process.killProcess(android.os.Process.myPid());` if you are using `finish();` function inside the Activity then you **should not call** the above code snippet !

#### **Proguard Settings**

If you are using Proguard add the following to your Proguard settings ! 
```
-keep class com.greedygame.android.** { *;}
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
 }
```
 
### For more help please see [FAQ] (https://github.com/GreedyGame/unity-plugin/wiki/FAQs)
