package com.appglu;

/**
 * Happens when an HTTP status error of the client series is received (anything between 400 and 499).
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpClientException extends AppGluHttpStatusCodeException {
	
	public AppGluHttpClientException(int statusCode, Error error) {
		super(statusCode, error);
	}

}