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

	@Override
	public void registerDeviceInBackground(final Device device, AsyncCallback<Void> registerCallback) {
		asyncExecutor.execute(registerCallback, new Callable<Void>() {
			public Void call() {
				pushOperations.registerDevice(device);
				return null;
			}
		});
	}

	@Override
	public void readDeviceInBackground(final String token, AsyncCallback<Device> readCallback) {
		asyncExecutor.execute(readCallback, new Callable<Device>() {
			public Device call() {
				return pushOperations.readDevice(token);
			}
		});
	}

	@Override
	public void removeDeviceInBackground(final String token, AsyncCallback<Boolean> removeCallback) {
		asyncExecutor.execute(removeCallback, new Callable<Boolean>() {
			public Boolean call() {
				return pushOperations.removeDevice(token);
			}
		});
	}

}