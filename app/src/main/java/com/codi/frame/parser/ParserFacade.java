package com.codi.frame.parser;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.codi.frame.model.Error;
import com.codi.frame.parser.*;

/**
 * Title: IParserFacade
 * Description: 
 * @author Codi
 * @date 2014-3-14
 */
public class ParserFacade {

	com.codi.frame.parser.AbsParser mParser;
	IParserListener mParserListener;
	
	public ParserFacade(com.codi.frame.parser.AbsParser parser, IParserListener parserListener) {
		mParser = parser;
		mParserListener = parserListener;
		if(mParserListener == null) {
			throw new NullPointerException("ParserListener is null!");
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(String result) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new ParserTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, result);
        } else {
            new ParserTask().execute(result);
        }
	}
	
	protected Object handler(String result) {
		return mParser.parser(result);
	}
	
	protected class ParserTask extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... params) {
			return handler(params[0]);
		}
		
		@Override
		protected void onPostExecute(Object result) {
			
			if(result instanceof Error) {
				mParserListener.onParserError((Error) result);
			} else {
				mParserListener.onParserSuccess(result);
			}
		}
		
	}
}
