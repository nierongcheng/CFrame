package com.codi.frame.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.codi.frame.model.Error;
import com.google.gson.Gson;

/**
 * Title: AbsParser
 * Description: 
 * @author Codi
 * @date 2014-3-13
 */
public abstract class AbsParser {

	public final String ERROR_LABEL = "error";
	Gson mGson;
	
	public AbsParser() {
		mGson = new Gson();
	}
	
	public Object parser(String jsonStr) {
		Object object = parseError(jsonStr);
		if(object == null) {
			object = parserRight(jsonStr);
		}
		return object;
	}
	
	public abstract Object parserRight(String jsonStr);
	
	private Object parseError(String jsonStr) {
		String errorStr = detectElement(jsonStr, ERROR_LABEL);
		if(errorStr != null) {
			return parserObject(errorStr, Error.class);
		}
		return null;
	}
	
	public <T> T parserObject(String jsonStr, Class<T> classOfT) {
		return mGson.fromJson(jsonStr, classOfT);
	}
	
	public String detectElement(String jsonStr, String element) {
		String elementStr;
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			elementStr = jsonObject.getString(element);
		} catch (JSONException e) {
			elementStr = null;
			e.printStackTrace();
		}
		 
		return elementStr;
	}
}
