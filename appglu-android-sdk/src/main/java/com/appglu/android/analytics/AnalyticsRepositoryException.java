package com.appglu.android.analytics;

@SuppressWarnings("serial")
public class AnalyticsRepositoryException extends RuntimeException {

	public AnalyticsRepositoryException() {
		super();
	}
	
	public AnalyticsRepositoryException(String msg) {
		super(msg);
	}

	public AnalyticsRepositoryException(Throwable cause) {
		super(cause);
	}
	
}
