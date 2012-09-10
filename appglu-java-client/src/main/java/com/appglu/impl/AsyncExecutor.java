package com.appglu.impl;

import com.appglu.AsyncCallback;

public interface AsyncExecutor {
	
	<Result> void execute(AsyncCallback<Result> asyncCallback, AsyncExecutorCallback<Result> executorCallback);

}