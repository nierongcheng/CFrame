package com.codi.frame.parser;

import com.codi.frame.model.Error;

/**
 * Title: IParserListener
 * Description: 
 * @author Codi
 * @date 2014-3-14
 */
public interface IParserListener {

	public void onParserSuccess(Object result);
	public void onParserError(Error error);
	
}
