package com.appglu;

import java.util.Map;

/**
 * {@code AsyncUserOperations} has all methods that {@link UserOperations} has but they execute <strong>asynchronously</strong>. 
 * @see UserOperations
 * @since 1.0.0
 */
public interface AsyncUserOperations {
	
	/**
	 * Asynchronous version of {@link UserOperations#signup(User)}.
	 * @see UserOperations#signup(User)
	 */
	void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#login(String, String)}.
	 * @see UserOperations#login(String, String)
	 */
	void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#refreshUserProfile()}.
	 * @see UserOperations#refreshUserProfile()
	 */
	void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#logout()}.
	 * @see UserOperations#logout()
	 */
	void logoutInBackground(AsyncCallback<Boolean> logoutCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#readData()}.
	 * @see UserOperations#readData()
	 */
	void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#writeData(Map)}.
	 * @see UserOperations#writeData(Map)
	 */
	void writeDataInBackground(Map<String, Object> data, AsyncCallback<Void> writeDataCallback);

}