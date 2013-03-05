package com.appglu;

import org.springframework.http.HttpStatus;

/**
 * Happens when {@link UserOperations#login(String, String)} does not succeed because either the login or the password does not match.<br>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpUserUnauthorizedException extends AppGluHttpClientException {
	
	public AppGluHttpUserUnauthorizedException(Error error) {
		super(HttpStatus.UNAUTHORIZED.value(), error);
	}

}