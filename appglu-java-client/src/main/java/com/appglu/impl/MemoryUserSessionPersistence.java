package com.appglu.impl;

import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.UserSessionPersistenceException;

/**
 * Implementation of {@link UserSessionPersistence} strategy to keep track of the authenticated mobile application {@link User} using <strong>memory</strong>.<br>
 * This implementation will only keep track of the {@link User} as long as the reference to this class is kept. If the reference is lost the {@link User} will be automatically logged out.<br>
 * This implementation is only recommended for testing purposes.
 * 
 * @see UserSessionPersistence
 * @since 1.0.0
 */
public class MemoryUserSessionPersistence implements UserSessionPersistence {
	
	private String currentSessionId;
	
	private User loggedInUser;

	public boolean isUserAuthenticated() throws UserSessionPersistenceException {
		return currentSessionId != null && loggedInUser != null;
	}

	public String getSessionId() throws UserSessionPersistenceException {
		return currentSessionId;
	}

	public User getAuthenticatedUser() throws UserSessionPersistenceException {
		return loggedInUser;
	}

	public void saveSessionId(String sessionId) throws UserSessionPersistenceException {
		if (sessionId != null) {
			this.currentSessionId = sessionId;
		}
	}
	
	public void saveAuthenticatedUser(User user) throws UserSessionPersistenceException {
		if (user != null) {
			this.loggedInUser = user;
		}
	}

	public void logout() throws UserSessionPersistenceException {
		this.currentSessionId = null;
		this.loggedInUser = null;
	}

}