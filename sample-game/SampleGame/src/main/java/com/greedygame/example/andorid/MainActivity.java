package com.greedygame.example.andorid;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignProgressListener;
import com.greedygame.android.core.campaign.CampaignStateListener;
import com.greedygame.android.core.helper.DeviceHelper;
import com.greedygame.android.core.helper.SDKHelper;
import com.greedygame.android.core.network.RequestConstants;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity implements CampaignStateListener, CampaignProgressListener{

	private static final String TAG = MainActivity.class.getSimpleName();
	private Runnable mUpdateProgress = null;
	private float mDownloadProgress = 0;
	int PERMISSION_ALL = 1;
	CharSequence information="\n Information not available";

	@BindView(R.id.menuInfo)
	ImageView moreInfo;
	@BindView(R.id.nativeImage)
	ImageView nativeUnit;
	@BindView(R.id.donut_progress)
	DonutProgress mDonutProgress;
	@BindView(R.id.funitId)
	TextView floatUnitId;
	@BindView(R.id.nunitId)
	TextView nativeUnitid;
	@BindString(R.string.float_unitID)
	String floatUnitIdString;
	@BindString(R.string.native_unitID)
	String nativeUnitIdString;
	@BindString(R.string.campaign_available)
	String campaignAvailable;
	@BindString(R.string.campaign_unavailable)
	String campaignUnavailable;
	@BindString(R.string.campaign_found)
	String campaignFound;
	@BindString(R.string.campaign_error)
	String campaignError;
	@BindString(R.string.greedygameProfile)
	String gg_profile;
	@BindString(R.string.greedygame_sdkVersion)
	String gg_sdkVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Fabric.with(this, new Crashlytics());
		ButterKnife.bind(this);

		String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

		if(!hasPermissions(this, PERMISSIONS)){
			ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
		}
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Candara.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		GreedyGameAgent.setCampaignProgressListener(this);
		GreedyGameAgent.setCampaignStateListener(this);
		mUpdateProgress =new Runnable() {
			@Override
			public void run() {
				mDonutProgress.setProgress(mDownloadProgress);
			}
		};
		changeTexture();
	}

	public static boolean hasPermissions(Context context, String... permissions) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
			for (String permission : permissions) {
				if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	public void changeTexture() {
		String file = GreedyGameAgent.Native.getPath(nativeUnitIdString);
		Bitmap bitmap ;
		if(file!=null) {
			bitmap = BitmapFactory.decodeFile(file);
			nativeUnit.setImageBitmap(bitmap);
		} else {
			nativeUnit.setImageResource(R.drawable.native_unit);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		/*//*** Fetching Float Ad unit ***//**/
		GreedyGameAgent.Float.show(this, floatUnitIdString);
		if(SDKHelper.getInstance().isInitialized()){
			mDonutProgress.setProgress(100);
			information="Game ID: "+getGameProfileId(MainActivity.this)+"\n"+
					"Android ID: "+DeviceHelper.getInstance().getValue(RequestConstants.ANDROID_ID)+"\n"+
					"SDK Version: "+getSDKVersion(MainActivity.this);
			nativeUnitid.setText(nativeUnitIdString);
			floatUnitId.setText(floatUnitIdString);
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	@Override
	public void onPause(){
		super.onPause();
		/*//*** Fetching Float Ad unit ***//**/
		GreedyGameAgent.Float.remove(floatUnitIdString);
	
	}

	public String getGameProfileId(Context context) {
		String gameId = "";
		if(context != null) {
			String packageName = context.getPackageName();
			int resId = context.getResources().getIdentifier(gg_profile, "string", packageName);
			if(resId > 1) {
				gameId = context.getString(resId);
			}
		}

		return gameId;
	}

	public String getSDKVersion(Context context){
		String sdkVersion="";
		if(context != null){
			String packageName=context.getPackageName();
			int resId=context.getResources().getIdentifier(gg_sdkVersion,"string",packageName);
			if(resId>1){
				sdkVersion=context.getString(resId);
			}
		}
		return sdkVersion;
	}

	@OnClick(R.id.initSDK)
	void initializeSDK(){
		if(!SDKHelper.getInstance().isInitialized()){
			GreedyGameAgent.init(MainActivity.this);
			runOnUiThread(mUpdateProgress);
			mDonutProgress.setProgress(mDownloadProgress);
		}
	}

	@OnClick(R.id.showUII)
	void showUII(){
		GreedyGameAgent.Float.showUII(floatUnitIdString);
	}

	@OnClick(R.id.showFloat)
	void showFloat(){
		GreedyGameAgent.Float.show(MainActivity.this, floatUnitIdString);
	}

	@OnClick(R.id.removeFloat)
	void removeFloat(){
		GreedyGameAgent.Float.remove(floatUnitIdString);
	}

	@OnClick(R.id.showNative)
	void showNative(){
		changeTexture();
	}

	@OnClick(R.id.hideNative)
	void hideNative(){
		nativeUnit.setImageResource(R.drawable.native_unit);
	}

	@OnClick(R.id.menuInfo)
	void moreInfo(){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
		alertDialog.setTitle("Information");
		alertDialog.setMessage(information);
		alertDialog.show();
	}

	@Override
	public void onFound() {
		Log.d(TAG,campaignFound);
	}

	@Override
	public void onUnavailable() {
		Log.d(TAG,campaignUnavailable);
		Toast.makeText(getApplication(),"sample unavailable",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAvailable() {
		Log.d(TAG,campaignAvailable);
		Toast.makeText(getApplication(),"sample available",Toast.LENGTH_SHORT).show();
		nativeUnitid.setText(nativeUnitIdString);
		floatUnitId.setText(floatUnitIdString);
		information="Game ID: "+getGameProfileId(MainActivity.this)+"\n"+
				"Android ID: "+DeviceHelper.getInstance().getValue(RequestConstants.ANDROID_ID)+"\n"+
				"SDK Version: "+getSDKVersion(MainActivity.this);
		changeTexture();
	}

	@Override
	public void onError(String error) {
		Log.d(TAG,campaignError);
		Toast.makeText(getApplication(),error,Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProgress(int progress) {
		mDownloadProgress =progress;
		mUpdateProgress.run();
	}
}
