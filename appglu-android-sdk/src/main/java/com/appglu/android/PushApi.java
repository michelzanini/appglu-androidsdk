package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncPushOperations;
import com.appglu.Device;
import com.appglu.DevicePlatform;
import com.appglu.PushOperations;

public final class PushApi {
	
	private PushOperations pushOperations;
	
	private AsyncPushOperations asyncPushOperations;
	
	private DeviceInformation deviceInformation;

	public PushApi(PushOperations pushOperations, AsyncPushOperations asyncPushOperations, DeviceInformation deviceInformation) {
		this.pushOperations = pushOperations;
		this.asyncPushOperations = asyncPushOperations;
	}

	public void registerDevice(String token) throws AppGluRestClientException {
		this.registerDevice(token, null);
	}
	
	public void registerDevice(String token, String alias) throws AppGluRestClientException {
		Device device = this.createDevice(token, alias);
		pushOperations.registerDevice(device);
	}

	public boolean removeDevice(String token) throws AppGluRestClientException {
		return pushOperations.removeDevice(token);
	}
	
	public void registerDeviceInBackground(String token, AsyncCallback<Void> registerCallback) {
		this.registerDeviceInBackground(token, null, registerCallback);
	}
	
	public void registerDeviceInBackground(String token, String alias, AsyncCallback<Void> registerCallback) {
		Device device = this.createDevice(token, alias);
		asyncPushOperations.registerDeviceInBackground(device, registerCallback);
	}

	public void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback) {
		asyncPushOperations.removeDeviceInBackground(token, removeCallback);
	}
	
	private Device createDevice(String token, String alias) {
		Device device = new Device();
		device.setToken(token);
		device.setAlias(alias);
		device.setPlatform(DevicePlatform.ANDROID);
		device.setAppIdentifier(deviceInformation.getAppIdentifier());
		return device;
	}

}