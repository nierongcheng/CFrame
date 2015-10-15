package com.codi.frame.parser;

import com.codi.frame.model.Error;

/**
 * Title: IParserListener
 * Description: 
 * @author Codi
 * @date 2014-3-14
 */
public interface IParserListener {

	void onParserSuccess(Object result);
	void onParserError(Error error);
	
}
