/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.impl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DateUtils {
	
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	private final static String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
	
	private final static String TIME_REGEX = "\\d{2}:\\d{2}:\\d{2}";
	
	private static Pattern dateRegex;
	
	private static Pattern timeRegex;
	
	static {
		dateRegex = Pattern.compile(DATE_REGEX);
		timeRegex = Pattern.compile(TIME_REGEX);
	}
	
	public static Date parseDate(String date) throws ParseException {
		DateFormat dateFormat = DateUtils.getDateFormat(date);
		return dateFormat.parse(date);
	}
	
	public static DateFormat getDateFormat(String date) {
		Matcher dateMatcher = dateRegex.matcher(date);
		if (dateMatcher.matches()) {
			return new SimpleDateFormat(DATE_FORMAT);
		}
		Matcher timeMatcher = timeRegex.matcher(date);
		if (timeMatcher.matches()) {
			return new SimpleDateFormat(TIME_FORMAT);
		}
		return new SimpleDateFormat(DATE_TIME_FORMAT);
	}
	
	public static String formatDatetime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		return dateFormat.format(date);
	}
	
	public static String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date);
	}
	
	public static String formatTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
		return dateFormat.format(date);
	}

}
