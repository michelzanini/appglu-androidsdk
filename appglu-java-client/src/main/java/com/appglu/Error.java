package com.appglu;

import java.io.Serializable;

/**
 * TODO
 */
public class Error implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ErrorCode code;
	
	private String message;
	
	public Error() {
		super();
	}
	
	public Error(ErrorCode code, String message) {
		this.code = code;
		this.message = message;
	}

	public ErrorCode getCode() {
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