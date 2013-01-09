package com.appglu.android;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;

public class AppGluAsyncCallbackTask<Result> extends AppGluAsyncTask<Void, Void, Result> {
	
	private AsyncCallback<Result> asyncCallback;
	
	private Callable<Result> executorCallable;
	
	public AppGluAsyncCallbackTask(AsyncCallback<Result> asyncCallback, Callable<Result> executorCallable) {
		this.asyncCallback = asyncCallback;
		this.executorCallable = executorCallable;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		asyncCallback.onPreExecute();
	}

	protected Result doExecuteInBackground(Void... params) throws Exception {
		return executorCallable.call();
	}

	protected void onResult(Result result) {
		asyncCallback.onResult(result);
	}

	protected void onException(Exception exception) {
		super.onException(exception);
		asyncCallback.onException(new ExceptionWrapper(exception));
	}

	protected void onFinished() {
		super.onFinished();
		asyncCallback.onFinish();
	}

}