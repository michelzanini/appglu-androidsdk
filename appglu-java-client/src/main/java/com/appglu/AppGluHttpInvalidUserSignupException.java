package com.appglu;

/**
 * Happens when {@link UserOperations#signup(User)} does not succeed.<br>
 * That can be because the user already existed or maybe the password is weak.
 */

@SuppressWarnings("serial")
public class AppGluHttpInvalidUserSignupException extends AppGluHttpClientException {

	public AppGluHttpInvalidUserSignupException(int statusCode, Error error) {
		super(statusCode, error);
	}
	
}