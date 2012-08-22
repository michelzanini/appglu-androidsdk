package com.appglu.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class AppgluClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private String baseUrl;
	
	private HttpHeaders defaultHeaders;
	
	public AppgluClientHttpRequestInterceptor(String baseUrl, HttpHeaders defaultHeaders) {
		this.baseUrl = baseUrl;
		this.defaultHeaders = defaultHeaders;
	}

	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request) {

			@Override
			public URI getURI() {
				URI uri = super.getURI();
				String url = uri.toString();
				try {
					return new URI(baseUrl + url);
				} catch (URISyntaxException e) {
					return uri;
				}
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = super.getHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.putAll(defaultHeaders);
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}
