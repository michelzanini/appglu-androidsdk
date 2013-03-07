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
