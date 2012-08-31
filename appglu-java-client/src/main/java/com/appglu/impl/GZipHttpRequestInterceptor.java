package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class GZipHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request) {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = super.getHeaders();
				headers.add("Accept-Encoding", "gzip");
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}