package com.appglu;

/**
 * The base exception for any exception that can be thrown by the Java AppGlu client.
 * 
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluRestClientException extends RuntimeException {
	
	public AppGluRestClientException(String msg) {
		super(msg);
	}

	public AppGluRestClientException(String msg, Throwable ex) {
		super(msg, ex);
	}
	
	public AppGluRestClientException(Throwable ex) {
		super(ex);
	}
	
}