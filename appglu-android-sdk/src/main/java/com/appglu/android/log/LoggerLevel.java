package com.appglu.android.log;

import android.util.Log;

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