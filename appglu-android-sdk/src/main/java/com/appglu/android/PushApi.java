package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncPushOperations;
import com.appglu.Device;
import com.appglu.DevicePlatform;
import com.appglu.PushOperations;

/**
 * {@code PushApi} is used to register / unregister the device making it eligible to receive push notifications.<br>
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.PushOperations
 * @see com.appglu.AsyncPushOperations
 * @since 1.0.0
 */
public final class PushApi {
	
	private PushOperations pushOperations;
	
	private AsyncPushOperations asyncPushOperations;
	
	private DeviceInstallation deviceInstallation;

	public PushApi(PushOperations pushOperations, AsyncPushOperations asyncPushOperations, DeviceInstallation deviceInstallation) {
		this.pushOperations = pushOperations;
		this.asyncPushOperations = asyncPushOperations;
		this.deviceInstallation = deviceInstallation;
	}

	/**
	 * Register a device identified by his token, making it eligible to receive push notifications.
	 * @param token the push token is unique for each device installation
	 */
	public void registerDevice(String token) throws AppGluRestClientException {
		this.registerDevice(token, null);
	}
	
	/**
	 * Register a device identified by his token, making it eligible to receive push notifications.
	 * @param token the push token is unique for each device installation
	 * @param alias an <code>alias</code> is a name that can be used to group several installation in one logical unit. 
	 * For example, the <code>alias</code> could be the user name grouping all devices a user may have.
	 */
	public void registerDevice(String token, String alias) throws AppGluRestClientException {
		Device device = this.createDevice(token, alias);
		pushOperations.registerDevice(device);
	}

	/**
	 * Unregister a device identified by his token.
	 * @param token the push token is unique for each device installation
	 * @return <code>true</code> if removed with success, <code>false</code> if the token was not found
	 */
	public boolean removeDevice(String token) throws AppGluRestClientException {
		return pushOperations.removeDevice(token);
	}
	
	/**
	 * Asynchronous version of {@link PushApi#registerDevice(String)}
	 * @see PushApi#registerDevice(String)
	 */
	public void registerDeviceInBackground(String token, AsyncCallback<Void> registerCallback) {
		this.registerDeviceInBackground(token, null, registerCallback);
	}
	
	/**
	 * Asynchronous version of {@link PushApi#registerDevice(String, String)}
	 * @see PushApi#registerDevice(String, String)
	 */
	public void registerDeviceInBackground(String token, String alias, AsyncCallback<Void> registerCallback) {
		Device device = this.createDevice(token, alias);
		asyncPushOperations.registerDeviceInBackground(device, registerCallback);
	}

	/**
	 * Asynchronous version of {@link PushApi#removeDevice(String)}
	 * @see PushApi#removeDevice(String)
	 */
	public void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback) {
		asyncPushOperations.removeDeviceInBackground(token, removeCallback);
	}
	
	private Device createDevice(String token, String alias) {
		Device device = new Device();
		device.setToken(token);
		device.setAlias(alias);
		device.setPlatform(DevicePlatform.ANDROID);
		device.setAppIdentifier(deviceInstallation.getAppIdentifier());
		return device;
	}

}