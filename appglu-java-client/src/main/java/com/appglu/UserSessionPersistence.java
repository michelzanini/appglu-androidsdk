package com.appglu;

public interface UserSessionPersistence {
	
	static final String X_APPGLU_SESSION_HEADER = "X-AppGlu-User-Session";
	
	boolean isUserAuthenticated() throws UserSessionPersistenceException;
	
	String getSessionId() throws UserSessionPersistenceException;
	
	User getAuthenticatedUser() throws UserSessionPersistenceException;
	
	void saveSessionId(String sessionId) throws UserSessionPersistenceException;
	
	void saveAuthenticatedUser(User user) throws UserSessionPersistenceException;
	
	void logout() throws UserSessionPersistenceException;

}