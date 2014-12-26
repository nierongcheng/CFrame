package com.codi.frame.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codi.frame.App;

public class ApplicationUtil {

	private static long lastClickTime;
	
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
	 * To avoid muti click.
	 * @param interval
	 * @return
	 */
	public static boolean isFastDoubleClick(long interval) {

		long time = System.currentTimeMillis();
		boolean isFlag = time - lastClickTime < interval ? true : false;
		if(!isFlag) {
			lastClickTime = time;
		}
		return isFlag;
	}

	/**
	 * 关闭软键盘
	 */
	public static void closeSoftInput(Activity activity) {
		View v = activity.getCurrentFocus();
		if(v != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
}
