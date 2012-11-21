package com.appglu.impl;

import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.UserSessionPersistenceException;

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