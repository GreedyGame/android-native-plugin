GreedyGame Android Native Integration Guide
===================

This is a complete guide to integrate GreedyGame plugin within your native android game. You can download [GreedyGameAgent_v5.6.jar](#publish-a-document).

#### Steps

* Paste jar to your android project's libs folder or directly add to build path

### Documentations
#### GreedyGameAgent
**Class Overview**

Contains high-level classes encapsulating the overall GreedyGame ad flow and model.

**Public Constructors**
#####`GreedyGameAgent(Activity gameActivity, IAgentListner greedyListner)`

Constructs a new instance of GreedyGame handler.

----------

**Method**

 - #####`public void init(String GameId, String []Units)`
Lookup for new native campaign from server. 

* GameId - Unique game profile id from panel.greedygame.com
* Units - List of relative path of assets used in games. 
    Also register unit id can be used
    
 - `public String activeTheme()`
Return Theme id of currently active and running theme

 - `public String newTheme()`
Return Theme id of new theme from server

 - `public void download()`
 Download branded assets for new campaign by unit-ids.
 *Should be used inside IAgentListner.onInit.*
	
 - `public void downloadByPath()`
 Download branded assets for new campaign by relative path.
 *Should be used inside IAgentListner.onInit.*
	
 - `public void cancelDownload()`
 Cancel current campaign download
		
 - `public String getActivePath()`
 Return path of folder, where assets of activeTheme is stored.

----
**Floating Ad Method**

- `public void fetchHeadAd(String unit_id)`
Fetch floating AdHead with unit-id

- `public void fetchHeadAd(String unit_id, float x, float y)`
 Fetch floating AdHead with unit-id, at specific x, y with screen pixels
 
- `public void removeHeadAd(String unit_id)`
Hide floating AdHead with unit-id

----
**Analytics Methods**
As name suggest, put following method inside Andorid main acitivity method.

- `public void onStart()`
- `public void onResume()`
- `public void onRestart()`
- `public void onPause()`
- `public void onStop()`
- `public void onDestroy()`

For example
```
@Override
	protected void onResume(){
    	super.onResume();
    	ggAgent.onResume();
    }
```
----
**Other Utilities Methods**

 - `public String get_verison()`
Return sdk version
	
 - `public void setDebug(boolean b)`
Set sdk into debug mode
	
 - `public void useSecureConnection(boolean b)`
To use api with HTTPS


---
#### interface IAgentListner
**Class Overview**
Is is used as callback listener argument for GreedyAgent class

**Methods**
 
 - `void onInit(int response)`
 	response value indicate
	 * -2 = loader busy right now
	 * -1 = using no campaign
	 * 0 = campaign already cached
	 * 1 = new campaign found to download
 - `void onDownload(boolean success)`
success true , If new branded contents are downloaded so that new scene can fetch assets from **getActivePath()**.

 - `void onUnitClicked(boolean clicked)`
 clicked true, if floating adhead unit is clicked so that game developer can manage game pause.

For example



```
class GG_Listner implements IAgentListner{
	@Override
	public void onProgress(float progress) {
		//Use this for showing progress bar
	}

	@Override
	public void onInit(int response) {
		/*
		 * -2 = loader busy right now
		 * -1 = using no campaign
		 * 0 = campaign already cached
		 * 1 = new campaign found to download
		 */

		if(response == 1){
			ggAgent.downloadByPath();
		} else if(response == 0){
			//Start game scene with content from getActivePath
		} else if(response == -1){
			//Start game with default content
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




