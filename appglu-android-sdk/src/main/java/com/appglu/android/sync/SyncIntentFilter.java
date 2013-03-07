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

import android.content.IntentFilter;

/**
 * Use this {@code IntentFilter} to register a {@link SyncBroadcastReceiver}.
 * 
 * <p><code>
 * Activity.this.registerReceiver(syncBroadcastReceiver, new SyncIntentFilter());
 * </code>
 * 
 * @see SyncBroadcastReceiver
 * @since 1.0.0
 */
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
