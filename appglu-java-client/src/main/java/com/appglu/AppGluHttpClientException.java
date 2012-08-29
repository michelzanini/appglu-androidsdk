package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppGluHttpClientException extends AppGluStatusCodeException {
	
	public AppGluHttpClientException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}