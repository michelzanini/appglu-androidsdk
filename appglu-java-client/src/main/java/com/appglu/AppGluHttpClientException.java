package com.appglu;

/**
 * TODO
 */

@SuppressWarnings("serial")
public class AppGluHttpClientException extends AppGluHttpStatusCodeException {
	
	public AppGluHttpClientException(int statusCode, Error error) {
		super(statusCode, error);
	}

}