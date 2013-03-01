package com.appglu;

/**
 * TODO
 */

@SuppressWarnings("serial")
public class AppGluHttpServerException extends AppGluHttpStatusCodeException {
	
	public AppGluHttpServerException(int statusCode, Error error) {
		super(statusCode, error);
	}

}