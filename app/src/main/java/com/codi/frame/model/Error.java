package com.codi.frame.model;

/**
 * Title: RequestError
 * Description: 
 * @author Codi
 * @date 2014-3-13
 */
public class Error {
	
	public String code;
	public String text;
	
	@Override
	public String toString() {
		return "RequestError [code=" + code + ", text=" + text + "]";
	}
	
}
