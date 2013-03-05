package com.appglu;

import java.io.Serializable;

/**
 * An error response contains an {@link Error} object.
 * 
 * @since 1.0.0
 */
public class ErrorResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
