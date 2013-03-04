package com.appglu.android;

import java.util.Map;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncUserOperations;
import com.appglu.AuthenticationResult;
import com.appglu.User;
import com.appglu.UserOperations;

/**
 * {@code UserApi} has methods to login / logout and sign up new mobile app users.<br>
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.UserOperations
 * @see com.appglu.AsyncUserOperations
 * @since 1.0.0
 */
public final class UserApi implements UserOperations, AsyncUserOperations {
	
	private UserOperations userOperations;
	
	private AsyncUserOperations asyncUserOperations;
	
	public UserApi(UserOperations userOperations, AsyncUserOperations asyncUserOperations) {
		this.userOperations = userOperations;
		this.asyncUserOperations = asyncUserOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public AuthenticationResult signup(User user) throws AppGluRestClientException {
		return this.userOperations.signup(user);
	}

	/**
	 * {@inheritDoc}
	 */
	public AuthenticationResult login(String username, String password) throws AppGluRestClientException {
		return this.userOperations.login(username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshUserProfile() throws AppGluRestClientException {
		this.userOperations.refreshUserProfile();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean logout() throws AppGluRestClientException {
		return this.userOperations.logout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> readData() throws AppGluRestClientException {
		return this.userOperations.readData();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void writeData(Map<String, Object> data) throws AppGluRestClientException {
		this.userOperations.writeData(data);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		this.asyncUserOperations.signupInBackground(user, authenticationResultCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		this.asyncUserOperations.loginInBackground(username, password, authenticationResultCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback) {
		this.asyncUserOperations.refreshUserProfileInBackground(refreshCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logoutInBackground(AsyncCallback<Boolean> logoutCallback) {
		this.asyncUserOperations.logoutInBackground(logoutCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback) {
		this.asyncUserOperations.readDataInBackground(readDataCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void writeDataInBackground(Map<String, Object> data, AsyncCallback<Void> writeDataCallback) {
		this.asyncUserOperations.writeDataInBackground(data, writeDataCallback);
	}

}