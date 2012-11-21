package com.appglu.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.UserSessionPersistenceException;

public class SharedPreferencesUserSessionPersistence implements UserSessionPersistence {
	
	static final String SHARED_PREFERENCES_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.SHARED_PREFERENCES_KEY";
	
	static final String SESSION_ID_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.SESSION_ID_KEY";
	
	static final String USER_EXIST_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_EXIST_KEY";
	
	static final String USER_ID_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_ID_KEY";
	
	static final String USER_USERNAME_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_USERNAME_KEY";
	
	static final String USER_PASSWORD_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_PASSWORD_KEY";
	
	private Context context;
	
	private String currentSessionId;
	
	private User loggedInUser;
	
	public SharedPreferencesUserSessionPersistence(Context context) {
		this.context = context;
		this.restoreSession();
	}

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
			Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
	    	editor.putString(SESSION_ID_KEY, sessionId);
	    	editor.commit();
	    	
	    	this.currentSessionId = sessionId;
		}
	}

	public void saveAuthenticatedUser(User user) throws UserSessionPersistenceException {
		if (user != null) {
			Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
	    	editor.putLong(USER_ID_KEY, user.getId() != null ? user.getId() : 0);
	    	editor.putString(USER_USERNAME_KEY, user.getUsername());
	    	editor.putString(USER_PASSWORD_KEY, user.getPassword());
	    	editor.putBoolean(USER_EXIST_KEY, true);
	    	editor.commit();
	    	
	    	this.loggedInUser = user;
		}
	}

	public void logout() throws UserSessionPersistenceException {
		Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
		
		this.currentSessionId = null;
		this.loggedInUser = null;
	}
	
	protected void restoreSession() {
		this.restoreSessionId();
		this.restoreUserProfile();
	}
	
	protected void restoreSessionId() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
		this.currentSessionId = sharedPreferences.getString(SESSION_ID_KEY, null);
	}
	
	protected void restoreUserProfile() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
		
		boolean userExist = sharedPreferences.getBoolean(USER_EXIST_KEY, false);
		if (userExist) {
			User user = new User();
			
			user.setId(sharedPreferences.getLong(USER_ID_KEY, 0));
			user.setUsername(sharedPreferences.getString(USER_USERNAME_KEY, null));
			user.setPassword(sharedPreferences.getString(USER_PASSWORD_KEY, null));
			
			this.loggedInUser = user;
		}
	}

}