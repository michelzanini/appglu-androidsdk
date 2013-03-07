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
package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncPushOperations;
import com.appglu.Device;
import com.appglu.PushOperations;

public final class AsyncPushTemplate implements AsyncPushOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final PushOperations pushOperations;
	
	public AsyncPushTemplate(AsyncExecutor asyncExecutor, PushOperations pushOperations) {
		this.asyncExecutor = asyncExecutor;
		this.pushOperations = pushOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerDeviceInBackground(final Device device, AsyncCallback<Void> registerCallback) {
		asyncExecutor.execute(registerCallback, new Callable<Void>() {
			public Void call() {
				pushOperations.registerDevice(device);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readDeviceInBackground(final String token, AsyncCallback<Device> readCallback) {
		asyncExecutor.execute(readCallback, new Callable<Device>() {
			public Device call() {
				return pushOperations.readDevice(token);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeDeviceInBackground(final String token, AsyncCallback<Boolean> removeCallback) {
		asyncExecutor.execute(removeCallback, new Callable<Boolean>() {
			public Boolean call() {
				return pushOperations.removeDevice(token);
			}
		});
	}

}
