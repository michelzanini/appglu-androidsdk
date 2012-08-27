package com.appglu.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class DefaultHeadersHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private String baseUrl;
	
	private HttpHeaders defaultHeaders;
	
	public DefaultHeadersHttpRequestInterceptor(String baseUrl, HttpHeaders defaultHeaders) {
		this.baseUrl = baseUrl;
		this.defaultHeaders = defaultHeaders;
		if (this.defaultHeaders == null) {
			this.defaultHeaders = new HttpHeaders();
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
				headers.putAll(defaultHeaders);
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}