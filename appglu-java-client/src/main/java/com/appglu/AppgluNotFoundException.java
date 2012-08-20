package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppgluNotFoundException extends AppgluHttpClientException {
	
	public AppgluNotFoundException(Error error) {
		super(HttpStatus.NOT_FOUND, error);
	}

}