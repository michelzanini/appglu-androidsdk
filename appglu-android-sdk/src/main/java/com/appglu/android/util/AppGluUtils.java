package com.appglu.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class AppGluUtils {

	public static void assertNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasLength(String string) {
		return hasLength((CharSequence) string);
	}
	
	public static boolean hasText(String string) {
		return hasText((CharSequence) string);
	}
	
	public static String replaceSemicolon(String string) {
		if (!hasText(string)) {
			return "";
		}
		return string.replace(';', ':');
	}
	
	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
}