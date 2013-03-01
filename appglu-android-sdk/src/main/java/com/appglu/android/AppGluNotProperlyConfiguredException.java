package com.appglu.android;

/**
 * It happens if a required configuration is missing and was not properly set by the time {@link AppGlu#initialize(android.content.Context, AppGluSettings)} was called.
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluNotProperlyConfiguredException extends RuntimeException {

	public AppGluNotProperlyConfiguredException(String msg) {
		super(msg);
	}

}