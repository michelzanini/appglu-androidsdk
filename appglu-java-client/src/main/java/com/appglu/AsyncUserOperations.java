package com.appglu;

import java.util.Map;

public interface AsyncUserOperations {
	
	void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback);
	
	void logoutInBackground(AsyncCallback<Boolean> logoutCallback);
	
	void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback);
	
	void writeDataInBackground(Map<String, Object> data, AsyncCallback<Void> writeDataCallback);

}