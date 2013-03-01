package com.appglu;

import java.util.Map;

/**
 * {@code UserOperations} has methods to login / logout and sign up new mobile app users.<br>
 * {@code UserOperations} depends on a implementation of {@link UserSessionPersistence} to keep track of the current logged in {@link User}.
 * @see AsyncUserOperations
 * @since 1.0.0
 */
public interface UserOperations {

	/**
	 * Signs up a new {@link User} for your mobile application.<br>
	 * If the sign up succeeds, the {@link User} will be saved using a {@link UserSessionPersistence} implementation.
	 * @param user {@link User} contains a username and a password
	 * @return If {@link AuthenticationResult#succeed()} is <code>false</code> then {@link AuthenticationResult#getError()} will contain an error code and a message
	 */
	AuthenticationResult signup(User user) throws AppGluRestClientException;
	
	/**
	 * Logs in a previously signed up {@link User} if the credentials are correct.<br>
	 * If the log in succeeds, the {@link User} will be saved using a {@link UserSessionPersistence} implementation.
	 * @param username the username used on sign up
	 * @param password the password used on sign up
	 * @return If {@link AuthenticationResult#succeed()} is <code>false</code> then {@link AuthenticationResult#getError()} will contain an error code and a message
	 */
	AuthenticationResult login(String username, String password) throws AppGluRestClientException;
	
	/**
	 * Calls the REST API to refresh the {@link User} object saved using a {@link UserSessionPersistence} implementation.
	 */
	void refreshUserProfile() throws AppGluRestClientException;
	
	/**
	 * Removes the logged in user from the {@link UserSessionPersistence} implementation.
	 * @return <code>false</code> if the user was already logged out
	 */
	boolean logout() throws AppGluRestClientException;
	
	/**
	 * Returns custom user data previously saved to AppGlu using {@link UserOperations#writeData(Map)}.
	 */
	Map<String, Object> readData() throws AppGluRestClientException;
	
	/**
	 * Saves custom user data to AppGlu. This data can later be retrieved back using {@link UserOperations#readData()}.
	 */
	void writeData(Map<String, Object> data) throws AppGluRestClientException;
	
}