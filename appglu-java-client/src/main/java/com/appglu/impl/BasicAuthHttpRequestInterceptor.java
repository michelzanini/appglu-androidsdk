package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import com.appglu.impl.util.Base64;

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
				String basicHeaderValue = "Basic " + Base64.encodeBytes(credentials.getBytes());
				headers.set("Authorization", basicHeaderValue);
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}