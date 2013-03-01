package com.appglu;

/**
 * {@code PushOperations} is used to register / unregister the device making it eligible to receive push notifications.<br>
 * 
 * @see AsyncPushOperations
 * @since 1.0.0
 */
public interface PushOperations {
	
	/**
	 * Register a device installation, making it eligible to receive push notifications.
	 * @param device represents a unique device installation for the purpose of receiving push notifications.
	 */
	void registerDevice(Device device) throws AppGluRestClientException;
	
	/**
	 * Return more information about a specific device token.
	 * @param token the push token is unique for each device installation
	 * @return {@link Device} if found or <code>null</code>
	 */
	Device readDevice(String token) throws AppGluRestClientException;
	
	/**
	 * Unregister a device identified by his token.
	 * @param token the push token is unique for each device installation
	 * @return <code>true</code> if removed with success, <code>false</code> if the token was not found
	 */
	boolean removeDevice(String token) throws AppGluRestClientException;

}