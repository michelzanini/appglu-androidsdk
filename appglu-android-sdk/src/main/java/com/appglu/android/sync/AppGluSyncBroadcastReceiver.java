package com.appglu.android.sync;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppGluSyncBroadcastReceiver extends BroadcastReceiver {

	private AsyncCallback<Void> asyncCallback;
	
	public AppGluSyncBroadcastReceiver(AsyncCallback<Void> asyncCallback) {
		this.asyncCallback = asyncCallback;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (AppGluSyncIntentService.PRE_EXECUTE_ACTION.equals(action)) {
			asyncCallback.onPreExecute();
		}
		if (AppGluSyncIntentService.NO_INTERNET_CONNECTION_ACTION.equals(action)) {
			asyncCallback.onNoInternetConnection();
		}
		if (AppGluSyncIntentService.RESULT_ACTION.equals(action)) {
			asyncCallback.onResult(null);
		}
		if (AppGluSyncIntentService.EXCEPTION_ACTION.equals(action)) {
			Exception exception = (Exception) intent.getSerializableExtra(AppGluSyncIntentService.EXCEPTION_SERIALIZABLE_EXTRA);
			asyncCallback.onException(new ExceptionWrapper(exception));
		}
		if (AppGluSyncIntentService.FINISH_ACTION.equals(action)) {
			asyncCallback.onFinish();
		}
	}

}
