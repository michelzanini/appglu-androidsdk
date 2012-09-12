package com.appglu;

public abstract class AsyncCallback<Result> {
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(Result result);
	
	public void onException(Exception exception) {
		
	}
	
	public void onFinish() {
		
	}

}