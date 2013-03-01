package com.appglu.android;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;

/**
 * Adapt {@link com.appglu.android.AppGluAsyncTask} callbacks to a more user friendly version implemented in the {@link com.appglu.AsyncCallback} abstract class.<br>
 * {@link com.appglu.AsyncCallback} is used in every <strong>asynchronous</strong> method of the SDK.
 *  
 * @see com.appglu.impl.AsyncExecutor
 * @see com.appglu.AsyncCallback
 * @since 1.0.0
 */
public class AppGluAsyncCallbackTask<Result> extends AppGluAsyncTask<Void, Void, Result> {
	
	private AsyncCallback<Result> asyncCallback;
	
	private Callable<Result> workerThreadCallback;
	
	public AppGluAsyncCallbackTask(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		this.asyncCallback = asyncCallback;
		this.workerThreadCallback = workerThreadCallback;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		asyncCallback.onPreExecute();
	}

	protected Result doExecuteInBackground(Void... params) throws Exception {
		return workerThreadCallback.call();
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