package com.codi.frame.net;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.codi.frame.net.*;
import com.codi.frame.net.MyRequest;

/**
 * Title: RequestManager
 * Description: 
 * @author Codi
 * @date 2014-3-13
 */
public class RequestManager {

	private static RequestManager sRequestManager = new RequestManager();
	private RequestQueue mRequestQueue;
	
	private RequestManager() {
		mRequestQueue = com.codi.frame.net.MyVolley.getRequestQueue();
	}
	
	public static RequestManager getInstance() {
		return sRequestManager;
	}
	
	public void commonRepuest(String url, Listener<String> successListener, ErrorListener errorListener) {
		StringRequest request = new com.codi.frame.net.MyRequest(url, successListener, errorListener);
		mRequestQueue.add(request);
	}
	
	public void bookInfoRequest(final int version, Listener<String> successListener, ErrorListener errorListener) {
//		String url = HttpConst.getHttpBookInfo();
//		StringRequest request = new com.codi.frame.net.MyRequest(url, successListener, errorListener) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				HashMap<String, String> params = new HashMap<String, String>();
//				params.put("version", version + "");
//				return params;
//			}
//		};
//        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 0, 1.0f));
//		mRequestQueue.add(request);
	}
	

}
