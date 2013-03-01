package com.appglu;

/**
 * Define methods to be called as a callback while doing an <strong>asynchronous</strong> operation.<br>
 * {@code AsyncCallback} is used in every <strong>asynchronous</strong> method of the SDK.
 * 
 * @see com.appglu.impl.AsyncExecutor
 * @since 1.0.0
 */
public abstract class AsyncCallback<Result> {
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(Result result);
	
	public void onException(ExceptionWrapper exceptionWrapper) {
		
	}
	
	public void onNoInternetConnection() {
		
	}
	
	public void onFinish() {
		
	}

}