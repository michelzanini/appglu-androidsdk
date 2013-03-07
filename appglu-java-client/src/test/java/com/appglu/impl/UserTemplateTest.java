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
package com.appglu.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.AppGluHttpUserUnauthorizedException;
import com.appglu.AuthenticationResult;
import com.appglu.ErrorCode;
import com.appglu.User;
import com.appglu.UserOperations;
import com.appglu.UserSessionPersistence;

public class UserTemplateTest extends AbstractAppGluApiTest {
	
	private UserOperations userOperations;
	
	@Before
	public void setup() {
		super.setup();
		userOperations = appGluTemplate.userOperations();
	}
	
	@Test
	public void signup() {
		responseHeaders.add(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId");
		
		mockServer.expect(requestTo("http://localhost/appglu/v1/users"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/user_signup")))
			.andRespond(withStatus(HttpStatus.CREATED).body(compactedJson("data/user_auth_response")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		User user = new User();
		
		user.setUsername("username");
		user.setPassword("password");
		
		AuthenticationResult result = userOperations.signup(user);
		Assert.assertTrue(result.succeed());
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals(new Long(1L), appGluTemplate.getAuthenticatedUser().getId());
		Assert.assertEquals("username", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser().getPassword());
		
		mockServer.verify();
	}
	
	@Test
	public void signupUsernameExists() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/user_signup")))
			.andRespond(withStatus(HttpStatus.CONFLICT).body(compactedJson("data/user_signup_username_exists")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		User user = new User();
		
		user.setUsername("username");
		user.setPassword("password");
		
		AuthenticationResult result = userOperations.signup(user);
		Assert.assertFalse(result.succeed());
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals(ErrorCode.APP_USER_USERNAME_ALREADY_USED, result.getError().getCode());
		Assert.assertEquals("There is already an user with this username.", result.getError().getMessage());
		
		mockServer.verify();
	}
	
	@Test
	public void login() {
		responseHeaders.add(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId");
		
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/login"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/user_login")))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/user_auth_response")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		AuthenticationResult result = userOperations.login("username", "password");
		Assert.assertTrue(result.succeed());
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals(new Long(1L), appGluTemplate.getAuthenticatedUser().getId());
		Assert.assertEquals("username", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser().getPassword());
		
		mockServer.verify();
	}
	
	@Test
	public void loginUnauthorized() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/login"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/user_login")))
			.andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(compactedJson("data/user_unauthorized")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		AuthenticationResult result = userOperations.login("username", "password");
		Assert.assertFalse(result.succeed());
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals(ErrorCode.APP_USER_UNAUTHORIZED, result.getError().getCode());
		Assert.assertEquals("Unauthorized to access resource because credentials are wrong or missing.", result.getError().getMessage());
		
		mockServer.verify();
	}
	
	@Test
	public void refreshUserProfile() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me"))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/user_auth_response")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
		
		userOperations.refreshUserProfile();
		
		Assert.assertEquals(new Long(1L), appGluTemplate.getAuthenticatedUser().getId());
		Assert.assertEquals("username", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser().getPassword());
		
		mockServer.verify();
	}
	
	@Test
	public void refreshUserProfileUnauthorized() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me"))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(compactedJson("data/user_unauthorized")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		try {
			userOperations.refreshUserProfile();
			Assert.fail("An unauthorized response should throw an AppGluHttpUserUnauthorizedException exception");
		} catch (AppGluHttpUserUnauthorizedException e) {
			
		}
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		mockServer.verify();
	}
	
	@Test
	public void logout() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/logout"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.OK).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
		
		boolean succeed = userOperations.logout();
		Assert.assertTrue(succeed);
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		mockServer.verify();
	}
	
	@Test
	public void logoutUnauthorized() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/logout"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(compactedJson("data/user_unauthorized")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		boolean succeed = userOperations.logout();
		Assert.assertFalse(succeed);
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		mockServer.verify();
	}
	
	@Test
	public void changingUserSessionPersistenceWillNotForgetAuthenticatedUser() {
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new MemoryUserSessionPersistence());
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void readData() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me/data"))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/user_data")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Map<String, Object> data = userOperations.readData();
		
		Assert.assertFalse(data.isEmpty());
		Assert.assertEquals("valueOne", data.get("entryOne"));
		
		Assert.assertTrue(data.get("entryTwo") instanceof Map<?, ?>);
		Map<String, Object> entryTwo = (Map<String, Object>) data.get("entryTwo");
		
		Assert.assertEquals("test", entryTwo.get("data"));
		Assert.assertEquals("test", entryTwo.get("moreData"));
		
		Assert.assertTrue(entryTwo.get("nestedData") instanceof Map<?, ?>);
		Map<String, Object> nested = (Map<String, Object>) entryTwo.get("nestedData");
		
		Assert.assertEquals(1, nested.get("value"));
		
		Assert.assertEquals("valueOne", data.get("entryOne"));
		
		Assert.assertTrue(data.get("entryThree") instanceof List<?>);
		List<Object> list = (List<Object>) data.get("entryThree");
		
		Assert.assertEquals(1, list.get(0));
		Assert.assertEquals(2, list.get(1));
		Assert.assertEquals(3, list.get(2));
		
		mockServer.verify();
	}
	
	@Test
	public void readDataUnauthorized() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me/data"))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(compactedJson("data/user_unauthorized")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		try {
			userOperations.readData();
			Assert.fail("An unauthorized response should throw an AppGluHttpUserUnauthorizedException exception");
		} catch (AppGluHttpUserUnauthorizedException e) {
			
		}
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		mockServer.verify();
	}
	
	@Test
	public void writeData() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me/data"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(content().string(compactedJson("data/user_data")))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.OK).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("entryOne", "valueOne");
		
		Map<String, Object> entryTwo = new HashMap<String, Object>();
		entryTwo.put("data", "test");
		entryTwo.put("moreData", "test");
		
		Map<String, Object> nested = new HashMap<String, Object>();
		nested.put("value", 1);
		entryTwo.put("nestedData", nested);
		
		data.put("entryTwo", entryTwo);
		data.put("entryThree", new int[] { 1,2,3 } );
		
		userOperations.writeData(data);
		
		mockServer.verify();
	}
	
	@Test
	public void writeDataUnauthorized() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/me/data"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(content().string(compactedJson("data/user_data_single_entry")))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(compactedJson("data/user_unauthorized")).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("key", "value");
			
			userOperations.writeData(data);
			Assert.fail("An unauthorized response should throw an AppGluHttpUserUnauthorizedException exception");
		} catch (AppGluHttpUserUnauthorizedException e) {
			
		}
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		mockServer.verify();
	}
	
	class LoggedInUserSessionPersistence extends MemoryUserSessionPersistence {
		
		public LoggedInUserSessionPersistence(String sessionId, User user) {
			this.saveSessionId(sessionId);
			this.saveAuthenticatedUser(user);
		}
		
	}

}
