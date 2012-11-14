package com.appglu.impl.util;

public class StringUtils {
	
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	public static boolean hasLength(String string) {
		return hasLength((CharSequence) string);
	}
	
	public static boolean isNotEmpty(CharSequence str) {
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
	
	public static boolean isNotEmpty(String string) {
		return isNotEmpty((CharSequence) string);
	}
	
	public static boolean isEmpty(String string) {
		return !isNotEmpty(string);
	}
	
	public static boolean isEmpty(CharSequence str) {
		return !isNotEmpty(str);
	}

}