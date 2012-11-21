package com.appglu.impl;

import static org.springframework.test.web.client.match.RequestMatchers.content;
import static org.springframework.test.web.client.match.RequestMatchers.header;
import static org.springframework.test.web.client.match.RequestMatchers.method;
import static org.springframework.test.web.client.match.RequestMatchers.requestTo;
import static org.springframework.test.web.client.response.ResponseCreators.withStatus;
import junit.framework.Assert;

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
		Assert.assertEquals("password", appGluTemplate.getAuthenticatedUser().getPassword());
		
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
		Assert.assertEquals("password", appGluTemplate.getAuthenticatedUser().getPassword());
		
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
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test", "test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getPassword());
		
		userOperations.refreshUserProfile();
		
		Assert.assertEquals(new Long(1L), appGluTemplate.getAuthenticatedUser().getId());
		Assert.assertEquals("username", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertEquals("password", appGluTemplate.getAuthenticatedUser().getPassword());
		
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
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test", "test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		try {
			userOperations.refreshUserProfile();
			Assert.fail("An unauthorized response should throw an AppGluHttpUserUnauthorizedException exception");
		} catch (AppGluHttpUserUnauthorizedException e) {
			
		}
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
	}
	
	@Test
	public void logout() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/users/logout"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header(UserSessionPersistence.X_APPGLU_SESSION_HEADER, "sessionId"))
			.andRespond(withStatus(HttpStatus.OK).headers(responseHeaders));
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test", "test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getPassword());
		
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
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test", "test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		boolean succeed = userOperations.logout();
		Assert.assertFalse(succeed);
		
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
	}
	
	@Test
	public void changingUserSessionPersistenceWillNotForgetAuthenticatedUser() {
		Assert.assertFalse(appGluTemplate.isUserAuthenticated());
		Assert.assertNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new LoggedInUserSessionPersistence("sessionId", new User("test", "test")));
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		appGluTemplate.setUserSessionPersistence(new MemoryUserSessionPersistence());
		
		Assert.assertTrue(appGluTemplate.isUserAuthenticated());
		Assert.assertNotNull(appGluTemplate.getAuthenticatedUser());
		
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getUsername());
		Assert.assertEquals("test", appGluTemplate.getAuthenticatedUser().getPassword());
	}
	
	class LoggedInUserSessionPersistence extends MemoryUserSessionPersistence {
		
		public LoggedInUserSessionPersistence(String sessionId, User user) {
			this.saveSessionId(sessionId);
			this.saveAuthenticatedUser(user);
		}
		
	}

}