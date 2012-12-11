package com.appglu.android.sync;

@SuppressWarnings("serial")
public class SyncRepositoryException extends RuntimeException {

	public SyncRepositoryException() {
		super();
	}
	
	public SyncRepositoryException(String msg) {
		super(msg);
	}

	public SyncRepositoryException(Throwable cause) {
		super(cause);
	}
	
}