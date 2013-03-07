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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import com.appglu.impl.util.StringUtils;

public class DefaultHeadersHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private String baseUrl;
	
	private String applicationEnvironment;
	
	private Map<String, List<String>> defaultHeaders = new HashMap<String, List<String>>();
	
	public DefaultHeadersHttpRequestInterceptor(String baseUrl, String applicationEnvironment) {
		this.baseUrl = baseUrl;
		this.applicationEnvironment = applicationEnvironment;
	}
	
	public Map<String, List<String>> getDefaultHeaders() {
		return defaultHeaders;
	}

	public void setDefaultHeaders(Map<String, List<String>> defaultHeaders) {
		if (this.defaultHeaders != null) {
			this.defaultHeaders = defaultHeaders;
		}
	}

	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request) {

			@Override
			public URI getURI() {
				URI uri = super.getURI();
				String fragment = uri.toString();
				String url = baseUrl + fragment;
				try {
					return new URI(url);
				} catch (URISyntaxException ex) {
					throw new IllegalArgumentException("Could not create HTTP URL from [" + url + "]: " + ex, ex);
				}
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = super.getHeaders();
				
				if (StringUtils.isNotEmpty(applicationEnvironment)) {
					headers.put("X-AppGlu-Environment", Arrays.asList(applicationEnvironment));
				}
				
				headers.putAll(defaultHeaders);
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}
