package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppGluHttpServerException extends AppGluHttpException {
	
	public AppGluHttpServerException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}
