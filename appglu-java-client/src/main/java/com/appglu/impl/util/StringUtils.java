package com.appglu.impl.util;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.util.CollectionUtils;

public abstract class StringUtils {
	
	public static String underscoreName(String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		
		StringBuilder result = new StringBuilder();
		result.append(name.substring(0, 1).toLowerCase());
		
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			if (s.equals(s.toUpperCase())) {
				result.append("_");
				result.append(s.toLowerCase());
			} else {
				result.append(s);
			}
		}
		
		return result.toString();
	}
	
	public static String camelCaseName(String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		
		StringBuilder result = new StringBuilder();
		
		boolean nextIsUpper = false;
		
		for (int i = 0; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			if (s.equals("_")) {
				nextIsUpper = true;
			} else {
				if (nextIsUpper) {
					result.append(s.toUpperCase());
					nextIsUpper = false;
				} else {
					result.append(s.toLowerCase());
				}
			}
		}
		
		return result.toString();
	}
	
	public static String replaceSemicolon(String string) {
		if (string == null) {
			return "";
		}
		return string.replace(';', ':');
	}
	
	public static String addDoubleQuotes(String string) {
		if (string == null) {
			return "";
		}
		return "\"" + string + "\"";
	}
	
	public static String removeDoubleQuotes(String string) {
		if (string == null) {
			return "";
		}
		return string.replace("\"", "");
	}
	
	public static String escapeColumn(String columnName) {
		return "`" + columnName + "`";
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
	
	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

}