package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppgluHttpServerException extends AppgluStatusCodeException {
	
	public AppgluHttpServerException(HttpStatus statusCode, Error error) {
		super(statusCode, error);
	}

}
