package com.appglu.android.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class SyncBroadcastReceiver extends BroadcastReceiver {

	@Override
	public final void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (SyncIntentService.PRE_EXECUTE_ACTION.equals(action)) {
			this.onPreExecute();
		}
		if (SyncIntentService.NO_INTERNET_CONNECTION_ACTION.equals(action)) {
			this.onNoInternetConnection();
		}
		if (SyncIntentService.RESULT_ACTION.equals(action)) {
			boolean changesWereApplied = intent.getBooleanExtra(SyncIntentService.CHANGES_WERE_APPLIED_BOOLEAN_EXTRA, false);
			this.onResult(changesWereApplied);
		}
		if (SyncIntentService.EXCEPTION_ACTION.equals(action)) {
			Exception exception = (Exception) intent.getSerializableExtra(SyncIntentService.EXCEPTION_SERIALIZABLE_EXTRA);
			this.onException(new SyncExceptionWrapper(exception));
		}
		if (SyncIntentService.FINISH_ACTION.equals(action)) {
			this.onFinish();
		}
	}
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(boolean changesWereApplied);
	
	public void onException(SyncExceptionWrapper exceptionWrapper) {
		
	}
	
	public void onNoInternetConnection() {
		
	}
	
	public void onFinish() {
		
	}

}