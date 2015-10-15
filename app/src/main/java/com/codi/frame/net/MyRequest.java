package com.codi.frame.net;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.codi.frame.App;
import com.codi.frame.utils.CommonUtil;

/**
 * Title: MyRequest
 * Description: 
 * @author Codi
 * @date 2014-5-14
 */
public class MyRequest extends StringRequest {
	
	public static final HashMap<String, String> sHeaders;
	
	static {

		sHeaders = new HashMap<>();
		sHeaders.put("x-client-udid", App.getInstance().IMEI);
		sHeaders.put("x-client-ver", CommonUtil.getAppVersionName());
		sHeaders.put("x-client-identifier", "android");
		
	}
	
	public MyRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(Method.POST, url, listener, errorListener);
	}

	public MyRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return sHeaders;
	}
	
}
