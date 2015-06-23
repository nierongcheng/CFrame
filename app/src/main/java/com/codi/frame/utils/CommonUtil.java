package com.codi.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.os.Binder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.codi.frame.App;

import java.util.List;

/**
 * Created by Codi on 2015/4/20 0020.
 */
public class CommonUtil {

    private static long sLastClickTime;

    /**
     * show or hide soft input.
     *
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempt to hide the Android Keyboard.
     * @param activity The current activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager keyboard = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Not using getCurrentFocus as that sometimes is null, but the keyboard is still up.
        keyboard.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * Attempt to display the Android keyboard.
     * Android should always show the keyboard at the appropriate time. This method allows you to display the keyboard
     * when Android fails to do so.
     *
     * @param activity The current activity
     * @param view The currently focused view that will receive the keyboard input
     */
    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager keyboard = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**n
     * check whether app has some permission
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        try {
            return context.checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid()) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * check whether intent is exist in phone.
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable (Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    /**
     * Returns the height of the ActionBar in the current activity. The system controls the
     * height of the ActionBar, which may be slightly different depending on screen orientation,
     * and device version.
     * @param context Context used for retrieving the height attribute.
     * @return Height of the ActionBar.
     */
    public static int getActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[] {
                android.support.v7.appcompat.R.attr.actionBarSize
        });
        int size = (int)styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return size;
    }

    /**
     * Resolves the resource ID of a theme-dependent attribute (for example, a color value
     * that changes based on the selected theme)
     * @param activity The activity whose theme contains the attribute.
     * @param id Theme-dependent attribute ID to be resolved.
     * @return The actual resource ID of the requested theme-dependent attribute.
     */
    public static int getThemedAttributeId(Activity activity, int id) {
        TypedValue tv = new TypedValue();
        activity.getTheme().resolveAttribute(id, tv, true);
        return tv.resourceId;
    }

    /**
     * Calculates the actual font size for the current device, based on an "sp" measurement.
     * @param window The window on which the font will be rendered.
     * @param fontSp Measurement in "sp" units of the font.
     * @return Actual font size for the given sp amount.
     */
    public static float getFontSizeFromSp(Window window, float fontSp) {
        final DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return fontSp / metrics.scaledDensity;
    }

    /**
     * 获得应用程序的版本号
     * @return
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageManager pm = App.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(App.getInstance().getPackageName(), 0);
            versionName += pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * To avoid multi click.
     * @param interval
     * @return
     */
    public static boolean isFastDoubleClick(long interval) {

        long time = System.currentTimeMillis();
        boolean isFlag = time - sLastClickTime < interval ? true : false;
        if(!isFlag) {
            sLastClickTime = time;
        }
        return isFlag;
    }

}
