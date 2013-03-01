package com.appglu;

import org.springframework.http.HttpStatus;

/**
 * TODO
 */

@SuppressWarnings("serial")
public class AppGluHttpNotFoundException extends AppGluHttpClientException {
	
	public AppGluHttpNotFoundException(Error error) {
		super(HttpStatus.NOT_FOUND.value(), error);
	}

}