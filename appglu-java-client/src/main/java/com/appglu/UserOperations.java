package com.appglu;

import java.util.Map;

public interface UserOperations {

	AuthenticationResult signup(User user) throws AppGluRestClientException;
	
	AuthenticationResult login(String username, String password) throws AppGluRestClientException;
	
	void refreshUserProfile() throws AppGluRestClientException;
	
	boolean logout() throws AppGluRestClientException;
	
	Map<String, Object> readData() throws AppGluRestClientException;
	
	void writeData(Map<String, Object> data) throws AppGluRestClientException;
	
}