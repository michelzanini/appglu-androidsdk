package com.appglu;

import org.springframework.http.HttpStatus;

/**
 * TODO
 */

@SuppressWarnings("serial")
public class AppGluHttpUserUnauthorizedException extends AppGluHttpClientException {
	
	public AppGluHttpUserUnauthorizedException(Error error) {
		super(HttpStatus.UNAUTHORIZED.value(), error);
	}

}