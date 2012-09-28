package com.appglu.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class DefaultHeadersHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private String baseUrl;
	
	private Map<String, List<String>> defaultHeaders = new HashMap<String, List<String>>();
	
	public DefaultHeadersHttpRequestInterceptor(String baseUrl) {
		this.baseUrl = baseUrl;
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
				headers.putAll(defaultHeaders);
				return headers;
			}
			
		};
		
		return execution.execute(wrapper, body);
	}

}