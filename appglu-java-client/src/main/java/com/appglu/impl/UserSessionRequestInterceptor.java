package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import com.appglu.UserSessionPersistence;

public class UserSessionRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private UserSessionPersistence userSessionPersistence;
	
	public UserSessionRequestInterceptor(UserSessionPersistence userSessionPersistence) {
		this.userSessionPersistence = userSessionPersistence;
	}
	
	public void setUserSessionPersistence(UserSessionPersistence userSessionPersistence) {
		this.userSessionPersistence = userSessionPersistence;
	}

	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request) {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = super.getHeaders();
				
				if (userSessionPersistence.isUserAuthenticated()) {
					String sessionId = userSessionPersistence.getSessionId();
					headers.add(UserSessionPersistence.X_APPGLU_SESSION_HEADER, sessionId);					
				}
				
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}