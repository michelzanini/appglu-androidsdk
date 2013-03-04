package com.appglu.android;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.impl.AsyncExecutor;

/**
 * A {@link AsyncExecutor} implementation that uses {@link AppGluAsyncTask} to provide <strong>asynchronous</strong> execution.
 * 
 * @see com.appglu.android.AppGluAsyncTask
 * @see com.appglu.impl.AsyncExecutor
 * 
 * @since 1.0.0
 */
public class AsyncTaskExecutor implements AsyncExecutor {
	
	@Override
	public <Result> void execute(final AsyncCallback<Result> asyncCallback, final Callable<Result> workerThreadCallback) {
		AppGluAsyncCallbackTask<Result> asyncTask = new AppGluAsyncCallbackTask<Result>(asyncCallback, workerThreadCallback);
		
		if (AppGlu.hasInternetConnection()) {
			asyncTask.execute();
		} else {
			asyncCallback.onNoInternetConnection();
		}
	}

}