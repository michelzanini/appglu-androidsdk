package com.appglu;

public class ErrorResponse {
	
	private Error error;
	
	public ErrorResponse() {
		super();
	}
	
	public ErrorResponse(String code, String message) {
		this.error = new Error(code, message);
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
}
