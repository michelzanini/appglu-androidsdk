package com.appglu;

public interface AsyncUserOperations {
	
	void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback);
	
	void logoutInBackground(AsyncCallback<Boolean> logoutCallback);

}