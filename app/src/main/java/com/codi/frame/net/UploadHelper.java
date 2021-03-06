package com.codi.frame.net;

import android.os.AsyncTask;

import com.codi.frame.model.FormFile;
import com.codi.frame.utils.UploadUtil;

import java.util.HashMap;

/**
 * Title: UploadHelper
 * Description: 管理文件后台上传
 */
public class UploadHelper extends AsyncTask<String, Void, String> {
	private String requestURL;		//请求的url
	private HashMap<String, String> formParams;
	private FormFile[] formFiles;
	private UploadListener mUploadListener;
	private HashMap<String, String> mHeaders;
	
	/**
	 * @param headers			表头
	 * @param uploadListener	上传监听
	 * @param requestURL		请求的URL
	 * @param formParams		表单的其他元素
	 * @param formFiles			表单文件
	 */
	public UploadHelper(HashMap<String, String> headers, UploadListener uploadListener, String requestURL, HashMap<String,String> formParams, FormFile... formFiles) {
		this.requestURL = requestURL;
		this.formFiles = formFiles;
		this.formParams = formParams;
		this.mHeaders = headers;
		if(uploadListener == null) {
			throw new NullPointerException("UploadListener is null!");
		}
		this.mUploadListener = uploadListener;
	}
	
	/**
	 * @param uploadListener	上传监听
	 * @param requestURL		请求的URL
	 * @param formFiles			表单文件
	 */
	public UploadHelper(UploadListener uploadListener, String requestURL, HashMap<String,String> formParams, FormFile... formFiles) {
		this(null, uploadListener, requestURL, formParams, formFiles);
	}
	
	public UploadHelper(HashMap<String, String> headers, UploadListener uploadListener, String requestURL, FormFile... formFiles) {
		this(headers, uploadListener, requestURL, null, formFiles);
	}
	
	public UploadHelper(UploadListener uploadListener, String requestURL, FormFile... formFiles) {
		this(null, uploadListener, requestURL, null, formFiles);
	}
	
	@Override
	protected void onPreExecute() {
		mUploadListener.onUploadStart();
	}

	@Override
	protected String doInBackground(String... params) {
		return UploadUtil.post(mHeaders, requestURL, formParams, formFiles);
	}

	@Override
	protected void onPostExecute(String result) {
		if(UploadUtil.SUCCESS.equalsIgnoreCase(result)) {
			mUploadListener.onUploadSuccess();
		}else {
			mUploadListener.onUploadFailed();
		}
	}
	
	public interface UploadListener {
		public void onUploadStart();
		public void onUploadSuccess();
		public void onUploadFailed();
	}
}







