package com.appglu;

public class ErrorResponse {
	
	private Error error;
	
	public ErrorResponse() {
		super();
	}
	
	public ErrorResponse(Error error) {
		this.error = error;
	}
	
	public Error getError() {
		return error;
	}

}
