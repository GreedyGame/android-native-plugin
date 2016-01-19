package com.greedygame.android;

import java.io.File;

import com.greedygame.android.helper.GlobalSingleton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings.Global;

public class GreedyUtilities {

	public static Bitmap getBitmapByResid(GreedyGameAgent ggAgent, int resid){ 
		if(GreedyGameAgent.gameActivity.getApplicationContext() == null){
			return null;
		}
		
			String path = GlobalSingleton.getInstance().getActivePath();
			String resName = GreedyGameAgent.gameActivity.getResources().getResourceEntryName(resid);
			File file = new File(GlobalSingleton.getInstance().getActivePath()
					+ "/" + resName + ".png");
			if (file.exists()) {
				return BitmapFactory.decodeFile(file.getAbsolutePath());
			}
	    
	    return null;
	}
}
