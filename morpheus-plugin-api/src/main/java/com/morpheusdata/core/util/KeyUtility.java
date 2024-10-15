/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.util;

import java.security.SecureRandom;
import java.util.Date;

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
	static SecureRandom random = new SecureRandom();

	public static String generateKey(int length) {
		return _generateKey(length, charSet);
	}

	public static String generateSimpleKey(int length) {
		return _generateKey(length, simpleCharSet);
	}

	public static String generateDateKey(int keyLength) {
		return String.valueOf(new Date().getTime()) + generateKey(keyLength);
	}

	private static String _generateKey(int length, String charSet) {
		String rtn = "";
		for(int i=0; i<length; i++) {
			rtn += charSet.charAt(random.nextInt(charSet.length()));
		}
		return rtn;
	}

}
