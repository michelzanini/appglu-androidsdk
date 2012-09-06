package com.appglu.android;

@SuppressWarnings("serial")
public class AppGluException extends RuntimeException {
	
	public AppGluException(String message) {
		super(message);
	}

	public AppGluException(String message, Throwable cause) {
		super(message, cause);
	}

}
