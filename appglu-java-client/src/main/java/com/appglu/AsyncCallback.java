package com.appglu;

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