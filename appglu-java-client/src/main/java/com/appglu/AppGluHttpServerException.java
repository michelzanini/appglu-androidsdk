package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppGluHttpServerException extends AppGluStatusCodeException {
	
	public AppGluHttpServerException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}
