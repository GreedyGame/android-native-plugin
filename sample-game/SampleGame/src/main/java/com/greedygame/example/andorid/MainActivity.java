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
import android.view.View;
import android.widget.Button;
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

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    int PERMISSION_ALL = 1;
    private String floatUnitIdString = "float-2473";
    private String nativeUnitIdString = "unit-3408";
    private Runnable updateProgress = null;
    private float downloadProgress = 0;
    private DonutProgress mDonutProgress;
    ImageView nativeUnit, moreInfo;
    TextView floatUnitId, nativeUnitid;
    CharSequence information = "\n Information not available";
    Button showUII, initSDK, showFloat, removeFloat, showNative, hideNative;
    TextView mEventRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Candara.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        nativeUnit = (ImageView) findViewById(R.id.nativeImage);
        mDonutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        showFloat = (Button) findViewById(R.id.showFloat);
        moreInfo = (ImageView) findViewById(R.id.menuInfo);
        showUII = (Button) findViewById(R.id.showUII);
        removeFloat = (Button) findViewById(R.id.removeFloat);
        showNative = (Button) findViewById(R.id.showNative);
        hideNative = (Button) findViewById(R.id.hideNative);
        initSDK = (Button) findViewById(R.id.initSDK);
        floatUnitId = (TextView) findViewById(R.id.funitId);
        nativeUnitid = (TextView) findViewById(R.id.nunitId);
        mEventRefresh = (TextView) findViewById(R.id.event_refresh);
        mDonutProgress.setTextSize(15);
        initSDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SDKHelper.getInstance().isInitialized()) {
                    GreedyGameAgent.init(MainActivity.this);
                    runOnUiThread(updateProgress);
                    mDonutProgress.setProgress(downloadProgress);
                }
            }
        });

        updateProgress = new Runnable() {
            @Override
            public void run() {
                //if(!SDKHelper.getInstance().isInitialized()){
                //Log.d(TAG,"SDK is not initialized");
                mDonutProgress.setProgress(downloadProgress);
                //Log.d(TAG,"SDK is initialized");
                /*//*mProgressDialog.setMessage("Please Wait...");*/
                /*mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setProgress((int)downloadProgress);
				mProgressDialog.show();
				mProgressDialog.setCancelable(false);
				if(mProgressDialog.getProgress() == 100){
					mProgressDialog.dismiss();
				}*/
				/*if(!SDKHelper.getInstance().isInitialized()){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress((int) downloadProgress);
                    if(mProgressBar.getProgress()==100){
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }*/
            }
        };

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
                alertDialog.setTitle("Information");
                alertDialog.setMessage(information);
                alertDialog.show();
            }
        });

        GreedyGameAgent.setCampaignStateListener(new CampaignStateListener() {
            @Override
            public void onFound() {
				/*if(!GreedyGameAgent.isCampaignAvailable()){
					Toast.makeText(getApplication(),"Campaign not found",Toast.LENGTH_SHORT).show();
				}*/

            }

            @Override
            public void onUnavailable() {
                Toast.makeText(getApplication(), "sample unavailable", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAvailable() {
                Toast.makeText(getApplication(), "sample available", Toast.LENGTH_SHORT).show();
                nativeUnitid.setText(nativeUnitIdString);
                floatUnitId.setText(floatUnitIdString);
                information = "Game ID: " + getGameProfileId(MainActivity.this) + "\n" +
                        "Android ID: " + DeviceHelper.getInstance().getValue(RequestConstants.ANDROID_ID) + "\n" +
                        "SDK Version: " + getSDKVersion(MainActivity.this);
                changeTexture();

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
            }

        });

        GreedyGameAgent.setCampaignProgressListener(new CampaignProgressListener() {
            @Override
            public void onProgress(int progress) {
                downloadProgress = progress;
                updateProgress.run();
            }
        });

        showUII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GreedyGameAgent.Float.showUII(floatUnitIdString);
            }
        });

        showFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GreedyGameAgent.Float.show(MainActivity.this, floatUnitIdString);
            }
        });

        removeFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GreedyGameAgent.Float.remove(floatUnitIdString);
            }
        });

        showNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTexture();
            }
        });

        hideNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativeUnit.setImageResource(R.drawable.native_unit);
            }
        });

        mEventRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GreedyGameAgent.startEventRefresh();
            }
        });

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
        Bitmap bitmap;
        if (file != null) {
            bitmap = BitmapFactory.decodeFile(file);
            nativeUnit.setImageBitmap(bitmap);
        } else {
            nativeUnit.setImageResource(R.drawable.native_unit);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
		/*//*** Fetching Float Ad unit ***//**/
        GreedyGameAgent.Float.show(this, floatUnitIdString);
        if (SDKHelper.getInstance().isInitialized()) {
            mDonutProgress.setProgress(100);
            information = "Game ID: " + getGameProfileId(MainActivity.this) + "\n" +
                    "Android ID: " + DeviceHelper.getInstance().getValue(RequestConstants.ANDROID_ID) + "\n" +
                    "SDK Version: " + getSDKVersion(MainActivity.this);
            nativeUnitid.setText(nativeUnitIdString);
            floatUnitId.setText(floatUnitIdString);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
		/*//*** Fetching Float Ad unit ***//**/
        GreedyGameAgent.Float.remove(floatUnitIdString);

    }

    public String getGameProfileId(Context context) {
        String gameId = "";
        if (context != null) {
            String packageName = context.getPackageName();
            int resId = context.getResources().getIdentifier("greedygame_profile", "string", packageName);
            if (resId > 1) {
                gameId = context.getString(resId);
            }
        }

        return gameId;
    }

    public String getSDKVersion(Context context) {
        String sdkVersion = "";
        if (context != null) {
            String packageName = context.getPackageName();
            int resId = context.getResources().getIdentifier("greedygame_sdk_version", "string", packageName);
            if (resId > 1) {
                sdkVersion = context.getString(resId);
            }
        }
        return sdkVersion;
    }
}
