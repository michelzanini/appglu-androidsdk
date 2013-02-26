package com.appglu.android;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.impl.AsyncExecutor;

public class AsyncTaskExecutor implements AsyncExecutor {
	
	@Override
	public <Result> void execute(final AsyncCallback<Result> asyncCallback, final Callable<Result> executorCallable) {
		AppGluAsyncCallbackTask<Result> asyncTask = new AppGluAsyncCallbackTask<Result>(asyncCallback, executorCallable);
		
		if (AppGlu.hasInternetConnection()) {
			asyncTask.execute();
		} else {
			asyncCallback.onNoInternetConnection();
		}
	}

}