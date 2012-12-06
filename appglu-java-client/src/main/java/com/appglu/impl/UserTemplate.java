package com.appglu.impl;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluHttpInternalServerErrorException;
import com.appglu.AppGluHttpInvalidUserSignupException;
import com.appglu.AppGluHttpUserUnauthorizedException;
import com.appglu.AppGluRestClientException;
import com.appglu.AuthenticationResult;
import com.appglu.User;
import com.appglu.UserOperations;
import com.appglu.UserSessionPersistence;
import com.appglu.impl.json.UserBody;

public final class UserTemplate implements UserOperations {
	
	static final String SIGN_UP_URL = "/v1/users";
	
	static final String LOGIN_URL = "/v1/users/login";
	
	static final String LOGOUT_URL = "/v1/users/logout";
	
	static final String ME_URL = "/v1/users/me";
	
	static final String DATA_URL = "/v1/users/me/data";
	
	private RestOperations restOperations;
	
	private UserSessionPersistence userSessionPersistence;
	
	public UserTemplate(RestOperations restOperations, UserSessionPersistence userSessionPersistence) {
		this.restOperations = restOperations;
		this.userSessionPersistence = userSessionPersistence;
	}
	
	public void setUserSessionPersistence(UserSessionPersistence userSessionPersistence) {
		this.userSessionPersistence = userSessionPersistence;
	}

	private AuthenticationResult authenticate(String url, UserBody user) {
		try {
			ResponseEntity<UserBody> response = this.restOperations.exchange(url, HttpMethod.POST, new HttpEntity<UserBody>(user), UserBody.class);
			this.saveSessionId(response);
			this.saveAuthenticatedUser(response);
			return new AuthenticationResult(true);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	public AuthenticationResult signup(User user) throws AppGluRestClientException {
		try {
			return authenticate(SIGN_UP_URL, new UserBody(user));
		} catch (AppGluHttpInvalidUserSignupException e) {
			return new AuthenticationResult(false, e.getError());
		}
	}

	public AuthenticationResult login(String username, String password) throws AppGluRestClientException {
		try{
			return authenticate(LOGIN_URL, new UserBody(username, password));
		} catch (AppGluHttpUserUnauthorizedException e) {
			return new AuthenticationResult(false, e.getError());
		}
	}
	
	public void refreshUserProfile() throws AppGluRestClientException {
		try {
			ResponseEntity<UserBody> response = this.restOperations.exchange(ME_URL, HttpMethod.GET, null, UserBody.class);
			this.saveAuthenticatedUser(response);
		} catch (AppGluHttpUserUnauthorizedException e) {
			this.userSessionPersistence.logout();
			throw e;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public boolean logout() throws AppGluRestClientException {
		try {
			this.restOperations.exchange(LOGOUT_URL, HttpMethod.POST, null, null);
			this.userSessionPersistence.logout();
			return true;
		} catch (AppGluHttpUserUnauthorizedException e) {
			this.userSessionPersistence.logout();
			return false;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> readData() throws AppGluRestClientException {
		try {
			return (Map<String, Object>) this.restOperations.getForObject(DATA_URL, Map.class);
		} catch (AppGluHttpUserUnauthorizedException e) {
			this.userSessionPersistence.logout();
			throw e;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public void writeData(Map<String, Object> data) throws AppGluRestClientException {
		try {
			this.restOperations.exchange(DATA_URL, HttpMethod.PUT, new HttpEntity<Map<String, Object>>(data), null);
		} catch (AppGluHttpUserUnauthorizedException e) {
			this.userSessionPersistence.logout();
			throw e;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	private void saveSessionId(ResponseEntity<UserBody> response) {
		String sessionId = response.getHeaders().getFirst(UserSessionPersistence.X_APPGLU_SESSION_HEADER);
		this.userSessionPersistence.saveSessionId(sessionId);
	}
	
	private void saveAuthenticatedUser(ResponseEntity<UserBody> response) {
		UserBody body = response.getBody();
		if (body == null) {
			throw new AppGluHttpInternalServerErrorException();
		}
		User user = body.getUser();
		if (user == null) {
			throw new AppGluHttpInternalServerErrorException();
		}
		this.userSessionPersistence.saveAuthenticatedUser(user);
	}
	
}