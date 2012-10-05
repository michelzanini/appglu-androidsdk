package com.appglu;

@SuppressWarnings("serial")
public class AppGluRestClientException extends RuntimeException {
	
	public AppGluRestClientException(String msg) {
		super(msg);
	}

	public AppGluRestClientException(String msg, Throwable ex) {
		super(msg, ex);
	}
	
}