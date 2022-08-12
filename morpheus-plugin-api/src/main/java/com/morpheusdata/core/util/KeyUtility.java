package com.morpheusdata.core.util;

import java.util.Date;
import java.util.Random;

public class KeyUtility {

	final static String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
	final static String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final static String NUMERIC = "0123456789";
	final static String PASSWORD_CHARS = "!@#$%^&*";

	static String charSet = UPPER_ALPHA+LOWER_ALPHA+NUMERIC;
	static String hexSet = UPPER_ALPHA+NUMERIC;
	static String smallHexSet = LOWER_ALPHA+NUMERIC;
	static String simpleCharSet = UPPER_ALPHA+NUMERIC;
	static String passwordCharSet = UPPER_ALPHA+LOWER_ALPHA+NUMERIC+PASSWORD_CHARS;
	static Random random = new Random();


	public static String generateKey(int length) {
		String rtn = "";
		for(int i=0; i<length; i++) {
			rtn += charSet.charAt(random.nextInt(charSet.length()));
		}

		return rtn;
	}


	public static String generateDateKey(int keyLength) {
		return String.valueOf(new Date().getTime()) + generateKey(keyLength);
	}

}
