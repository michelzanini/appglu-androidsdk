package com.appglu.android.sync;

import android.content.IntentFilter;

public class SyncIntentFilter extends IntentFilter {

	public SyncIntentFilter() {
		super();
		
		this.addAction(SyncIntentService.PRE_EXECUTE_ACTION);
		this.addAction(SyncIntentService.NO_INTERNET_CONNECTION_ACTION);
		this.addAction(SyncIntentService.RESULT_ACTION);
		this.addAction(SyncIntentService.ON_TRANSACTION_START_ACTION);
		this.addAction(SyncIntentService.ON_TRANSACTION_FINISH_ACTION);
		this.addAction(SyncIntentService.EXCEPTION_ACTION);
		this.addAction(SyncIntentService.FINISH_ACTION);
	}

}