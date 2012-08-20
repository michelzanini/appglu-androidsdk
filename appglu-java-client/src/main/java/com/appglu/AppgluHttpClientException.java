package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppgluHttpClientException extends AppgluStatusCodeException {
	
	public AppgluHttpClientException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}