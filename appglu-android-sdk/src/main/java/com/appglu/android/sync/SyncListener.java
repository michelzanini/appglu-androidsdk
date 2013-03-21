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

/**
 * <p>Create a sub-class of <code>SyncListener</code> to receive events from {@link SyncIntentService}.<br>
 * <p>To register and unregister the <code>SyncListener</code> use the {@link SyncApi} methods like below:
 * 
 * <p><code>
 * AppGlu.syncApi().registerSyncListener(syncListener);<br>
 * AppGlu.syncApi().unregisterSyncListener(syncListener);<br>
 * </code>
 * 
 * @see SyncApi#registerSyncListener(SyncListener)
 * @see SyncApi#unregisterSyncListener(SyncListener)
 * @see SyncIntentService
 * @since 1.0.0
 */
public abstract class SyncListener {
	
	public final void onEventMainThread(SyncEvent event) {
		if (event.getType() == SyncEvent.Type.ON_PRE_EXECUTE) {
			this.onPreExecute();
		}
		if (event.getType() == SyncEvent.Type.ON_RESULT) {
			this.onResult(event.getChangesWereApplied());
		}
		if (event.getType() == SyncEvent.Type.ON_TRANSACTION_START) {
			this.onTransactionStart();
		}
		if (event.getType() == SyncEvent.Type.ON_TRANSACTION_FINISH) {
			this.onTransactionFinish();
		}
		if (event.getType() == SyncEvent.Type.ON_EXECPTION) {
			this.onException(event.getExceptionWrapper());
		}
		if (event.getType() == SyncEvent.Type.ON_NO_INTERNET_CONNECTION) {
			this.onNoInternetConnection();
		}
		if (event.getType() == SyncEvent.Type.ON_FINISH) {
			this.onFinish();
		}
	}

	public void onPreExecute() {
		
	}
	
	public void onResult(boolean changesWereApplied) {
		
	}
	
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