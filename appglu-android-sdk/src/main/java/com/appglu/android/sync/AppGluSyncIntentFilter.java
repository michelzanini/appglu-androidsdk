package com.appglu.android.sync;

import android.content.IntentFilter;

public class AppGluSyncIntentFilter extends IntentFilter {

	public AppGluSyncIntentFilter() {
		super();
		
		this.addAction(AppGluSyncIntentService.PRE_EXECUTE_ACTION);
		this.addAction(AppGluSyncIntentService.NO_INTERNET_CONNECTION_ACTION);
		this.addAction(AppGluSyncIntentService.RESULT_ACTION);
		this.addAction(AppGluSyncIntentService.EXCEPTION_ACTION);
		this.addAction(AppGluSyncIntentService.FINISH_ACTION);
	}

}