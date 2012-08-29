package com.appglu;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AppGluNotFoundException extends AppGluHttpClientException {
	
	public AppGluNotFoundException(Error error) {
		super(HttpStatus.NOT_FOUND, error);
	}

}