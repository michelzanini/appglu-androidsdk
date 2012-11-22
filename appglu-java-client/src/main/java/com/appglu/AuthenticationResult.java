package com.appglu;

public class AuthenticationResult {
	
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

	public String toString() {
		return "AuthenticationResult [succeed=" + succeed + ", error=" + error + "]";
	}

}