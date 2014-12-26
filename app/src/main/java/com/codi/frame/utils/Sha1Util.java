package com.codi.frame.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Title: Sha1Util
 * Description: 
 * @author Codi
 * @date 2013-11-13
 */
public class Sha1Util {

	public static String getSha1(String input) {
		MessageDigest mDigest = null;
		byte[] result = null;

		try {
			mDigest = MessageDigest.getInstance("SHA1");
			result = mDigest.digest(input.getBytes());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
}
