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
 * Events that are fired from {@link SyncIntentService} and caught in {@link SyncListener}.
 * 
 * @see SyncIntentService
 * @see SyncListener
 * @since 1.0.0
 */
public class SyncEvent {

	public enum Type {
		ON_PRE_EXECUTE,
		ON_NO_INTERNET_CONNECTION,
		ON_TRANSACTION_START,
		ON_TRANSACTION_FINISH,
		ON_RESULT,
		ON_EXECPTION,
		ON_FINISH
	}
	
	private Type type;
	
	private boolean changesWereApplied;
	
	private SyncExceptionWrapper exceptionWrapper;

	public SyncEvent(Type type) {
		this.type = type;
	}
	
	public SyncEvent(boolean changesWereApplied) {
		this.changesWereApplied = changesWereApplied;
		this.type = Type.ON_RESULT;
	}

	public SyncEvent(SyncExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
		this.type = Type.ON_EXECPTION;
	}

	public Type getType() {
		return type;
	}

	public boolean getChangesWereApplied() {
		return changesWereApplied;
	}

	public SyncExceptionWrapper getExceptionWrapper() {
		return exceptionWrapper;
	}
	
}