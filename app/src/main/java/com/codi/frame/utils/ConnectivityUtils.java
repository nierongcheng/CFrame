package com.codi.frame.utils;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 19:33:01 - 21.05.2013
 */
public final class ConnectivityUtils {

	private ConnectivityUtils() {

	}

	public static ConnectivityManager getConnectivityManager(final Context pContext) {
		return (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link android.net.ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getConnectivityManager(pContext).getNetworkInfo(pNetworkType);
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (manager == null) {  
	        return false;  
	    }  
	    NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
	     
	    if (networkinfo == null || !networkinfo.isAvailable()) {  
	        return false;  
	    }
	    return true;
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link android.net.ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkAvailable(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isAvailable();
	}
	
	/**
	 * 通过Ping的方式来监测网络连接
	 * @return
	 */
    public static boolean isNetworkAvailable(String urlPath) {
    	boolean flag = false;
		HttpURLConnection httpconn = null;
		try {
			URL url = new URL(urlPath);
			httpconn = (HttpURLConnection) url.openConnection();
			httpconn.setConnectTimeout(1000);
			httpconn.setRequestProperty("Connection", "close");
			httpconn.connect();
			
			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				httpconn.disconnect();
				flag = true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if(httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}
		}
		return flag;
    }

	/**
	 * @param pContext
	 * @param pNetworkType {@link android.net.ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkConnected(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isConnected();
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link android.net.ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkConnectedOrConnecting(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isConnectedOrConnecting();
	}

	public static boolean isWifiAvailable(final Context pContext) {
		return ConnectivityUtils.isNetworkAvailable(pContext, ConnectivityManager.TYPE_WIFI);
	}

	public static boolean isWifiConnected(final Context pContext) {
		return ConnectivityUtils.isNetworkConnected(pContext, ConnectivityManager.TYPE_WIFI);
	}

	public static boolean isWifiConnectedOrConnecting(final Context pContext) {
		return ConnectivityUtils.isNetworkConnectedOrConnecting(pContext, ConnectivityManager.TYPE_WIFI);
	}
	
    /**
     * 结合Ping和API的方式来监测网络连接
     * @return
     */
    public static boolean detect(Context context, String urlPath) {
		return isNetworkAvailable(context) && isNetworkAvailable(urlPath);
	}

}
