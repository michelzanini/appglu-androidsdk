package com.appglu;

import org.springframework.http.HttpStatus;

/**
 * Happens when the HTTP status error 400 is received.
 * 
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpNotFoundException extends AppGluHttpClientException {
	
	public AppGluHttpNotFoundException(Error error) {
		super(HttpStatus.NOT_FOUND.value(), error);
	}

}