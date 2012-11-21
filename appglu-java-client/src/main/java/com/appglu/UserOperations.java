package com.appglu;

public interface UserOperations {

	AuthenticationResult signup(User user) throws AppGluRestClientException;
	
	AuthenticationResult login(String username, String password) throws AppGluRestClientException;
	
	void refreshUserProfile() throws AppGluRestClientException;
	
	boolean logout() throws AppGluRestClientException;
	
}