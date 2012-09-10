package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppGluHttpClientException extends AppGluHttpException {
	
	public AppGluHttpClientException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}