package com.codi.frame;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.codi.frame.net.MyVolley;
import com.codi.frame.utils.DebugUtil;

import java.util.List;

/**
 * Created by Codi on 2014/12/26.
 */
public class App extends Application {
    //	private static int DISK_IMAGE_CACHE_SIZE = 1024 * 1024 * 2;
//	private static CompressFormat DISK_IMAGE_CACHE_COMPRESS_FORMAT = CompressFormat.JPEG;
//	private static int DISK_IMAGE_CACHE_QUALITY = 100;

    private static App app;

    public boolean isFirstInstall = false;
    // IMEI,IMSI
    public String IMEI, IMSI;
    // SDK version
    public int sdkVersion;
    public boolean isLogin = false;

    public static Typeface TYPE_FACE;			// 字体

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (app == null) {
            app = (App) this;
        }
        MyVolley.init(this);

        if(BuildConfig.DEBUG) {
            DebugUtil.enableStrictMode();
        }

        getPhoneParams();
//        TYPE_FACE = Typeface.createFromAsset(getAssets(), "fonts/Founder quasi-circular.ttf");
    }

    public void getPhoneParams() {
        // Get information from the device
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        IMSI = telephonyManager.getSubscriberId();
        sdkVersion = Build.VERSION.SDK_INT;
        if (IMEI == null)  {
            IMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        telephonyManager = null;
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
