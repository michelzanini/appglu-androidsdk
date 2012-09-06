package com.appglu.android;

import com.appglu.AppGluHttpException;

public abstract class AsyncCallback<Result> {
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(Result result);
	
	public void onException(AppGluHttpException exception) {
		
	}
	
	public void onFinish() {
		
	}

}
