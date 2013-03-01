package com.appglu;

/**
 * {@code AsyncPushOperations} has all methods that {@link PushOperations} has but they execute <strong>asynchronously</strong>. 
 * @see PushOperations
 * @since 1.0.0
 */
public interface AsyncPushOperations {
	
	/**
	 * Asynchronous version of {@link PushOperations#registerDevice(Device)}
	 * @see PushOperations#registerDevice(Device)
	 */
	void registerDeviceInBackground(Device device, AsyncCallback<Void> registerCallback);
	
	/**
	 * Asynchronous version of {@link PushOperations#readDevice(String)}
	 * @see PushOperations#readDevice(String)
	 */
	void readDeviceInBackground(String token, AsyncCallback<Device> readCallback);
	
	/**
	 * Asynchronous version of {@link PushOperations#removeDevice(String)}
	 * @see PushOperations#removeDevice(String)
	 */
	void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback);

}