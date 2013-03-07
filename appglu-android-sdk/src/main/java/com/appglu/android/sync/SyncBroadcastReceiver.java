/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p>Create a sub-class of {@code SyncBroadcastReceiver} to receive broadcast events from {@link SyncIntentService}.<br>
 * <p>To register this <code>BroadcastReceiver</code> use a {@link SyncIntentFilter} like below:
 * 
 * <p><code>
 * Activity.this.registerReceiver(syncBroadcastReceiver, new SyncIntentFilter());
 * </code>
 * 
 * @see SyncIntentFilter
 * @since 1.0.0
 */
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
		if (SyncIntentService.ON_TRANSACTION_START_ACTION.equals(action)) {
			this.onTransactionStart();
		}
		if (SyncIntentService.ON_TRANSACTION_FINISH_ACTION.equals(action)) {
			this.onTransactionFinish();
		}
		if (SyncIntentService.EXCEPTION_ACTION.equals(action)) {
			SyncExceptionWrapper exceptionWrapper = (SyncExceptionWrapper) intent.getSerializableExtra(SyncIntentService.EXCEPTION_WRAPPER_SERIALIZABLE_EXTRA);
			this.onException(exceptionWrapper);
		}
		if (SyncIntentService.FINISH_ACTION.equals(action)) {
			this.onFinish();
		}
	}
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(boolean changesWereApplied);
	
	public void onTransactionStart() {
		
	}
	
	public void onTransactionFinish() {
		
	}
	
	public void onException(SyncExceptionWrapper exceptionWrapper) {
		
	}
	
	public void onNoInternetConnection() {
		
	}
	
	public void onFinish() {
		
	}

}
