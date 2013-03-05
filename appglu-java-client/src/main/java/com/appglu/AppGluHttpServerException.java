package com.appglu;

/**
 * Happens when an HTTP status error of the server series is received (anything between 500 and 599).
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpServerException extends AppGluHttpStatusCodeException {
	
	public AppGluHttpServerException(int statusCode, Error error) {
		super(statusCode, error);
	}

}