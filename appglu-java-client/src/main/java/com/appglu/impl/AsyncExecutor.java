package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;

/**
 * A strategy to define how <strong>asynchronous</strong> execution is achieved.<br>
 * For example, on pure Java SE platform, a implementation of {@code AsyncExecutor} could use the <code>java.util.concurrent</code> API.<br>
 * If used on a Android platform, for example, the implementation of {@code AsyncExecutor} could use the Android's <code>AsyncTask</code> class.
 * 
 * @see com.appglu.AsyncCallback
 * @since 1.0.0
 */
public interface AsyncExecutor {
	
	<Result> void execute(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback);

}