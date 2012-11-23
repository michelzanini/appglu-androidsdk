package com.appglu;

@SuppressWarnings("serial")
public class AppGluHttpInvalidUserSignupException extends AppGluHttpClientException {

	public AppGluHttpInvalidUserSignupException(int statusCode, Error error) {
		super(statusCode, error);
	}
	
}