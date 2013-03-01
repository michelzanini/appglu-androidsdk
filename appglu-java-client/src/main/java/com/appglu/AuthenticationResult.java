package com.appglu;

import java.io.Serializable;

/**
 * Contains the result of a mobile application user sign up or log in.<br>
 * If <code>succeed</code> is <code>false</code> then <code>error</code> will be not <code>null</code>.
 * 
 * @see UserOperations
 * @since 1.0.0
 */
public class AuthenticationResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean succeed;
	
	private Error error;
	
	public AuthenticationResult(boolean succeed) {
		this.succeed = succeed;
	}
	
	public AuthenticationResult(boolean succeed, Error error) {
		this.succeed = succeed;
		this.error = error;
	}

	public boolean succeed() {
		return succeed;
	}
	
	public Error getError() {
		return error;
	}
	
	public boolean hasError() {
		return error != null;
	}

	public String toString() {
		return "AuthenticationResult [succeed=" + succeed + ", error=" + error + "]";
	}

}