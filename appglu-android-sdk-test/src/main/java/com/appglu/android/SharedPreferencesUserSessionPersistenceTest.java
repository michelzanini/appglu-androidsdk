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

import com.appglu.User;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public class SharedPreferencesUserSessionPersistenceTest extends AndroidTestCase {

	private SharedPreferencesUserSessionPersistence userSessionPersistence;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.userSessionPersistence = new SharedPreferencesUserSessionPersistence(getContext());
		this.userSessionPersistence.logout();
	}
	
	public void testLogout() {
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
		
		this.userSessionPersistence.saveSessionId("sessionId");
		this.userSessionPersistence.saveAuthenticatedUser(new User("test", "test"));
		
		Assert.assertTrue(this.userSessionPersistence.isUserAuthenticated());
		Assert.assertNotNull(userSessionPersistence.getSessionId());
		Assert.assertNotNull(userSessionPersistence.getAuthenticatedUser());
		
		this.userSessionPersistence.logout();
		
		Assert.assertFalse(this.userSessionPersistence.isUserAuthenticated());
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
		
		this.userSessionPersistence = new SharedPreferencesUserSessionPersistence(getContext());
		
		Assert.assertFalse(this.userSessionPersistence.isUserAuthenticated());
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
	}
	
	public void testSaveAndRestoreSessionId() {
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
		
		this.userSessionPersistence.saveSessionId("sessionId");
		
		Assert.assertEquals("sessionId", userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
		
		this.userSessionPersistence = new SharedPreferencesUserSessionPersistence(getContext());
		
		Assert.assertEquals("sessionId", this.userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
	}
	
	public void testSaveAndRestoreAuthenticatedUser() {
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser());
		
		User user = new User("test", "test");
		user.setId(1L);
		
		this.userSessionPersistence.saveAuthenticatedUser(user);
		
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertEquals(Long.valueOf(1L), userSessionPersistence.getAuthenticatedUser().getId());
		Assert.assertEquals("test", userSessionPersistence.getAuthenticatedUser().getUsername());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser().getPassword());
		
		this.userSessionPersistence = new SharedPreferencesUserSessionPersistence(getContext());
		
		Assert.assertNull(userSessionPersistence.getSessionId());
		Assert.assertEquals(Long.valueOf(1L), userSessionPersistence.getAuthenticatedUser().getId());
		Assert.assertEquals("test", userSessionPersistence.getAuthenticatedUser().getUsername());
		Assert.assertNull(userSessionPersistence.getAuthenticatedUser().getPassword());
	}
	
}
