package com.appglu;

import com.appglu.impl.MemoryUserSessionPersistence;

/**
 * Strategy for keeping track of the authenticated mobile application {@link User}.<br>
 * When {@link UserOperations#signup(User)} or {@link UserOperations#login(String, String)} are called the {@link User} will be saved using {@link #saveAuthenticatedUser(User)}.<br>
 * When {@link UserOperations#logout()} is called then the {@link User} will be removed using {@link #logout()}.

 * @see UserOperations
 * @see MemoryUserSessionPersistence
 * @since 1.0.0
 */
public interface UserSessionPersistence {
	
	static final String X_APPGLU_SESSION_HEADER = "X-AppGlu-User-Session";
	
	boolean isUserAuthenticated() throws UserSessionPersistenceException;
	
	String getSessionId() throws UserSessionPersistenceException;
	
	User getAuthenticatedUser() throws UserSessionPersistenceException;
	
	void saveSessionId(String sessionId) throws UserSessionPersistenceException;
	
	void saveAuthenticatedUser(User user) throws UserSessionPersistenceException;
	
	void logout() throws UserSessionPersistenceException;

}