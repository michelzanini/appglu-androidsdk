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

import com.appglu.impl.util.Base64Utils;

public class BasicAuthHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private String applicationKey;
	
	private String applicationSecret;
	
	public BasicAuthHttpRequestInterceptor(String applicationKey, String applicationSecret) {
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
	}

	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request) {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = super.getHeaders();
				this.addBasicAuthHeader(headers);
				return headers;
			}

			private void addBasicAuthHeader(HttpHeaders headers) {
				String credentials = applicationKey + ":" + applicationSecret;
				String basicHeaderValue = "Basic " + Base64Utils.encodeBytes(credentials.getBytes());
				headers.set("Authorization", basicHeaderValue);
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}
