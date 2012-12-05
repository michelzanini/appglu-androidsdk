package com.appglu.android;

import java.util.Map;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncUserOperations;
import com.appglu.AuthenticationResult;
import com.appglu.User;
import com.appglu.UserOperations;

public final class UserApi implements UserOperations, AsyncUserOperations {
	
	private UserOperations userOperations;
	
	private AsyncUserOperations asyncUserOperations;
	
	public UserApi(UserOperations userOperations, AsyncUserOperations asyncUserOperations) {
		this.userOperations = userOperations;
		this.asyncUserOperations = asyncUserOperations;
	}

	public AuthenticationResult signup(User user) throws AppGluRestClientException {
		return this.userOperations.signup(user);
	}

	public AuthenticationResult login(String username, String password) throws AppGluRestClientException {
		return this.userOperations.login(username, password);
	}

	public void refreshUserProfile() throws AppGluRestClientException {
		this.userOperations.refreshUserProfile();
	}

	public boolean logout() throws AppGluRestClientException {
		return this.userOperations.logout();
	}
	
	public Map<String, Object> readData() throws AppGluRestClientException {
		return this.userOperations.readData();
	}
	
	public void writeData(Map<String, Object> data) throws AppGluRestClientException {
		this.userOperations.writeData(data);
	}
	
	public void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		this.asyncUserOperations.signupInBackground(user, authenticationResultCallback);
	}

	public void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		this.asyncUserOperations.loginInBackground(username, password, authenticationResultCallback);
	}

	public void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback) {
		this.asyncUserOperations.refreshUserProfileInBackground(refreshCallback);
	}

	public void logoutInBackground(AsyncCallback<Boolean> logoutCallback) {
		this.asyncUserOperations.logoutInBackground(logoutCallback);
	}
	
	public void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback) {
		this.asyncUserOperations.readDataInBackground(readDataCallback);
	}
	
	public void writeDataInBackground(Map<String, Object> data, AsyncCallback<Void> writeDataCallback) {
		this.asyncUserOperations.writeDataInBackground(data, writeDataCallback);
	}

}