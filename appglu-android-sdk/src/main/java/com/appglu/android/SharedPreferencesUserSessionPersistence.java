/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.UserSessionPersistenceException;

/**
 * Implementation of {@link UserSessionPersistence} strategy to keep track of the authenticated mobile application {@link User} using Android's <strong>SharedPreferences</strong> object.<br>
 * 
 * @see com.appglu.UserSessionPersistence
 * @since 1.0.0
 */
public class SharedPreferencesUserSessionPersistence implements UserSessionPersistence {
	
	private static final String SHARED_PREFERENCES_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.SHARED_PREFERENCES_KEY";
	
	private static final String SESSION_ID_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.SESSION_ID_KEY";
	
	private static final String USER_EXIST_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_EXIST_KEY";
	
	private static final String USER_ID_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_ID_KEY";
	
	private static final String USER_USERNAME_KEY = "com.appglu.android.SharedPreferencesUserSessionPersistence.USER_USERNAME_KEY";
	
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
	    	boolean succeed = editor.commit();
	    	if (!succeed) {
	    		throw new UserSessionPersistenceException("Error when writing session id to disk");
	    	}
	    	
	    	this.currentSessionId = sessionId;
		}
	}

	public void saveAuthenticatedUser(User user) throws UserSessionPersistenceException {
		if (user != null) {
			Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
	    	editor.putLong(USER_ID_KEY, user.getId() != null ? user.getId() : 0);
	    	editor.putString(USER_USERNAME_KEY, user.getUsername());
	    	editor.putBoolean(USER_EXIST_KEY, true);
	    	boolean succeed = editor.commit();
	    	if (!succeed) {
	    		throw new UserSessionPersistenceException("Error when writing authenticated user to disk");
	    	}

	    	user.setPassword(null);
	    	this.loggedInUser = user;
		}
	}

	public void logout() throws UserSessionPersistenceException {
		Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
		editor.clear();
		boolean succeed = editor.commit();
    	if (!succeed) {
    		throw new UserSessionPersistenceException("Error when removing authentication from disk");
    	}
		
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
			
			this.loggedInUser = user;
		}
	}

}
