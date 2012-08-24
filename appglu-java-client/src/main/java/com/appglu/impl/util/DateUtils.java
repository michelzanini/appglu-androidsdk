package com.appglu.impl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appglu.Appglu;

public abstract class DateUtils {
	
	private final static String TIME_REGEX = "T\\d{2}:\\d{2}:\\d{2}\\[\\+-]\\d{4}";
	
	private static Pattern timeRegex;
	
	static {
		timeRegex = Pattern.compile(TIME_REGEX);
	}
	
	public static Date parseDate(String date) throws ParseException {
		DateFormat dateFormat = DateUtils.getDateFormat(date);
		return dateFormat.parse(date);
	}
	
	public static DateFormat getDateFormat(String date) {
		Matcher timeMatcher = timeRegex.matcher(date);
		if (timeMatcher.matches()) {
			return new SimpleDateFormat(Appglu.TIME_FORMAT);
		}
		
		return new SimpleDateFormat(Appglu.DATE_TIME_FORMAT);
	}

}