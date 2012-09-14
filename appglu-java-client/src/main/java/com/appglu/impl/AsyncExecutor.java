package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;

public interface AsyncExecutor {
	
	<Result> void execute(AsyncCallback<Result> asyncCallback, Callable<Result> executorCallable);

}