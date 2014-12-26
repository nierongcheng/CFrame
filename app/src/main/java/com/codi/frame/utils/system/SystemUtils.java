package com.codi.frame.utils.system;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.regex.MatchResult;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;

import com.codi.frame.utils.StreamUtils;


public final class SystemUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final boolean SDK_VERSION_ECLAIR_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	public static final boolean SDK_VERSION_FROYO_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	public static final boolean SDK_VERSION_GINGERBREAD_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	public static final boolean SDK_VERSION_HONEYCOMB_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	public static final boolean SDK_VERSION_ICE_CREAM_SANDWICH_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

	private static final String BOGOMIPS_PATTERN = "BogoMIPS[\\s]*:[\\s]*(\\d+\\.\\d+)[\\s]*\n";
	private static final String MEMTOTAL_PATTERN = "MemTotal[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";
	private static final String MEMFREE_PATTERN = "MemFree[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";

	private static MemoryInfo sMemoryInfo;

	private SystemUtils() {

	}

	public static boolean isEmulator() {
		if (Build.PRODUCT != null && Build.PRODUCT.equals("google_sdk")) {
			return true;
		} else if (Build.MODEL != null && Build.MODEL.equals("google_sdk")) {
			return true;
		} else if (Build.BRAND != null && Build.BRAND.startsWith("generic")){
			return true;
		} else if (Build.DEVICE != null && Build.DEVICE.startsWith("generic")) {
			return true;
		} else {
			return false;
		}
	}

	public static MemoryInfo getMemoryInfo() {
		/* Lazy allocation. */
		if (SystemUtils.sMemoryInfo == null) {
			SystemUtils.sMemoryInfo = new MemoryInfo();
		}

		Debug.getMemoryInfo(SystemUtils.sMemoryInfo);

		return SystemUtils.sMemoryInfo;
	}

	public static boolean isGoogleTV(final Context pContext) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return SystemUtils.hasSystemFeature(pContext, "com.google.android.tv");
	}

	public static boolean hasCamera(final Context pContext) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_CAMERA);
	}

	public static boolean isNDKSupported(final Context pContext, final boolean pDefault) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (SystemUtils.isGoogleTV(pContext)) {
			if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public static String getApplicationLabel(final Context pContext) throws NameNotFoundException {
	    final int labelResID = SystemUtils.getApplicationInfo(pContext).labelRes;
	    return pContext.getString(labelResID);
	}

	public static int getPackageVersionCode(final Context pContext) {
		return SystemUtils.getPackageInfo(pContext).versionCode;
	}

	public static String getPackageVersionName(final Context pContext) {
		return SystemUtils.getPackageInfo(pContext).versionName;
	}

	public static String getPackageName(final Context pContext) {
		return pContext.getPackageName();
	}

	public static String getApkFilePath(final Context pContext) {
		try {
			return SystemUtils.getApplicationInfo(pContext, 0).sourceDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ApplicationInfo getApplicationInfo(final Context pContext) throws NameNotFoundException  {
		return SystemUtils.getApplicationInfo(pContext, 0);
	}

	public static ApplicationInfo getApplicationInfo(final Context pContext, final int pFlags) throws NameNotFoundException {
		return pContext.getPackageManager().getApplicationInfo(pContext.getPackageName(), pFlags);
	}

	public static PackageInfo getPackageInfo(final Context pContext) {
		return SystemUtils.getPackageInfo(pContext, 0);
	}

	public static PackageInfo getPackageInfo(final Context pContext, final int pFlags) {
		try {
			return pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), pFlags);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static int getTargetSDKVersion(final Context pContext) throws NameNotFoundException {
		return SystemUtils.getApplicationInfo(pContext).targetSdkVersion;
	}

	public static boolean hasSystemFeature(final Context pContext, final String pFeature) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final PackageManager packageManager = pContext.getPackageManager();
		try {
			return packageManager.hasSystemFeature(pFeature);
		} catch (final Throwable t) {
			final Method PackageManager_hasSystemFeatures = PackageManager.class.getMethod("hasSystemFeature", String.class);
			if (PackageManager_hasSystemFeatures == null) {
				return false;
			} else {
				final boolean result = (Boolean) PackageManager_hasSystemFeatures.invoke(packageManager, pFeature);
				return result;
			}
		}
	}

	public static boolean hasSystemFeature(final Context pContext, final String pFeature, final boolean pDefault) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return SystemUtils.hasSystemFeature(pContext, pFeature);
	}

	public static boolean optMetaDataBoolean(final Context pContext, final String pKey) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getBoolean(pKey);
	}

	public static boolean optMetaDataBoolean(final Context pContext, final String pKey, final boolean pDefaultValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getBoolean(pKey, pDefaultValue);
	}

	public static int optMetaDataInt(final Context pContext, final String pKey) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getInt(pKey);
	}

	public static int optMetaDataInt(final Context pContext, final String pKey, final int pDefaultValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getInt(pKey, pDefaultValue);
	}

	public static float optMetaDataFloat(final Context pContext, final String pKey) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getFloat(pKey);
	}

	public static float optMetaDataFloat(final Context pContext, final String pKey, final float pDefaultValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getFloat(pKey, pDefaultValue);
	}

	public static String optMetaDataString(final Context pContext, final String pKey) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getString(pKey);
	}

	public static String optMetaDataString(final Context pContext, final String pKey, final String pDefaultValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getString(pKey);
		} else {
			return pDefaultValue;
		}
	}

	public static int optMetaDataColor(final Context pContext, final String pKey) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return Color.parseColor(bundle.getString(pKey));
	}

	public static int optMetaDataColor(final Context pContext, final String pKey, final int pDefaultValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return Color.parseColor(bundle.getString(pKey));
		} else {
			return pDefaultValue;
		}
	}

	public static boolean getMetaDataBoolean(final Context pContext, final String pKey, boolean defValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getBoolean(pKey);
		} else {
			return defValue;
		}
	}

	public static int getMetaDataInt(final Context pContext, final String pKey, int defValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getInt(pKey);
		} else {
			return defValue;
		}
	}

	public static float getMetaDataFloat(final Context pContext, final String pKey, float defValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getFloat(pKey);
		} else {
			return defValue;
		}
	}

	public static String getMetaDataString(final Context pContext, final String pKey, String defValue) throws NameNotFoundException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getString(pKey);
		} else {
			return defValue;
		}
	}

	public static Bundle getMetaData(final Context pContext) throws NameNotFoundException {
		final ApplicationInfo applicationInfo = SystemUtils.getApplicationInfo(pContext, PackageManager.GET_META_DATA);
		return applicationInfo.metaData;
	}

	/**
	 * @param pBuildVersionCode taken from {@link android.os.Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersionOrLower(final int pBuildVersionCode) {
		return Build.VERSION.SDK_INT <= pBuildVersionCode;
	}

	/**
	 * @param pBuildVersionCode taken from {@link android.os.Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersionOrHigher(final int pBuildVersionCode) {
		return Build.VERSION.SDK_INT >= pBuildVersionCode;
	}

	/**
	 * @param pBuildVersionCodeMin taken from {@link android.os.Build.VERSION_CODES}.
	 * @param pBuildVersionCodeMax taken from {@link android.os.Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersion(final int pBuildVersionCodeMin, final int pBuildVersionCodeMax) {
		return (Build.VERSION.SDK_INT >= pBuildVersionCodeMin) && (Build.VERSION.SDK_INT <= pBuildVersionCodeMax);
	}

	/**
	 * @return in kiloBytes.
	 */
	public static long getSystemMemorySize() {
		final MatchResult matchResult = SystemUtils.matchSystemFile("/proc/meminfo", SystemUtils.MEMTOTAL_PATTERN, 1000);

		if (matchResult.groupCount() > 0) {
			return Long.parseLong(matchResult.group(1));
		} else {
			return 0;
		}
	}

	/**
	 * @return in kiloBytes.
	 */
	public static long getSystemMemoryFreeSize() {
		final MatchResult matchResult = SystemUtils.matchSystemFile("/proc/meminfo", SystemUtils.MEMFREE_PATTERN, 1000);

		if (matchResult.groupCount() > 0) {
			return Long.parseLong(matchResult.group(1));
		} else {
			return 0;
		}
	}


	private static MatchResult matchSystemFile(final String pSystemFile, final String pPattern, final int pHorizon) {
		InputStream in = null;
		Scanner scanner = null;
		try {
			final Process process = new ProcessBuilder(new String[] {"/system/bin/cat", pSystemFile}).start();

			in = process.getInputStream();
			scanner = new Scanner(in);

			final boolean matchFound = scanner.findWithinHorizon(pPattern, pHorizon) != null;
			if (matchFound) {
				return scanner.match();
			} 
		} catch (final IOException e) {
			
		} finally {
			StreamUtils.close(in);
			if(scanner != null) {
				scanner.close();
			}
		}
		return null;
	}

}
