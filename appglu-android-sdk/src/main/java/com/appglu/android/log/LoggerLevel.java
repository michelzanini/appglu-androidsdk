package com.appglu.android.log;

import android.util.Log;

/**
 * Represents the level that the log message will be logged.<br>
 * Levels are used to filter log messages that are too specific. For example, when the {@code LoggerLevel} is {@link #INFO}, then {@link #VERBOSE} and {@link #DEBUG} messages are not logged.<br>
 * 
 * <p>The default level to be used is {@link #INFO} but that can be change by calling {@link LoggerFactory#setLevel(LoggerLevel)} or {@link com.appglu.android.AppGluSettings#setLoggerLevel(LoggerLevel)} before initializing {@link com.appglu.android.AppGlu}.
 * 
 * @see LoggerFactory
 * @since 1.0.0
 */
public enum LoggerLevel {
	
	VERBOSE (Log.VERBOSE),
    
	DEBUG (Log.DEBUG),
    
    INFO (Log.INFO),
    
    WARN (Log.WARN),
    
    ERROR (Log.ERROR),
	
	NONE (LoggerLevel.NONE_CONSTANT);
	
	private static final int NONE_CONSTANT = 99;
	
	private final int androidLevel;
	
	LoggerLevel(int androidLevel) {
		this.androidLevel = androidLevel;
	}

	public int getLevelAsInteger() {
		return androidLevel;
	}

}