package com.appglu.android;

/**
 * It happens when you try to access an {@link AppGlu} method before initializing the class using {@link AppGlu#initialize(android.content.Context, AppGluSettings)}.
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluNotInitializedException extends RuntimeException {
	

}