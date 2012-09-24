package com.appglu.android.log;

import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {
	
	private static LoggerLevel level = LoggerLevel.INFO;
	
	private static final Map<String, AndroidLogger> loggerCache;
	
	static {
		loggerCache = new HashMap<String, AndroidLogger>();
	}
	
	public static LoggerLevel getLevel() {
		return level;
	}
	
	public static void setLevel(LoggerLevel loggerLevel) {
		if (loggerLevel != null) {
			level = loggerLevel;
		}
	}
	
	public static AndroidLogger getLogger(Class<?> clazz) {
		String tag = null;
		if (clazz != null) {
			tag = clazz.getSimpleName();
		}
		return getLogger(tag);
	}
	
	public static AndroidLogger getLogger(String tag) {
		AndroidLogger logger = null;
		
		synchronized (loggerCache) {
			logger = loggerCache.get(tag);
			if (logger == null) {
				logger = new AndroidLogger(tag);
				loggerCache.put(tag, logger);
			}
		}
		
		return logger;
	}

}