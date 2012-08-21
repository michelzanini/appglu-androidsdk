package com.appglu;

public class Error {
	
	private String code;
	
	private String message;
	
	public Error() {
		super();
	}
	
	public Error(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Error [code=" + code + ", message=" + message + "]";
	}
	
}