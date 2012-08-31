package com.appglu.impl;

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ClassUtils;

public class ClientHttpRequestFactorySelector {
	
	private static final boolean HTTP_COMPONENTS_AVAILABLE = ClassUtils.isPresent("org.apache.http.client.HttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	private static final boolean DECOMPRESSING_HTTP_CLIENT_AVAILABLE = ClassUtils.isPresent("org.apache.http.impl.client.DecompressingHttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	public static ClientHttpRequestFactory getRequestFactory() {
		if (DECOMPRESSING_HTTP_CLIENT_AVAILABLE) {
			return DecompressingHttpClientRequestFactoryCreator.createRequestFactory();
		}
		if (HTTP_COMPONENTS_AVAILABLE) {
			return DefaultHttpClientRequestFactoryCreator.createRequestFactory();
		}
		return new SimpleClientHttpRequestFactory();
	}
	
	public static class DefaultHttpClientRequestFactoryCreator {

		public static ClientHttpRequestFactory createRequestFactory() {
			return new HttpComponentsClientHttpRequestFactory(new DefaultHttpClient());
		}
		
	}
	
	public static class DecompressingHttpClientRequestFactoryCreator {

		public static ClientHttpRequestFactory createRequestFactory() {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			return new HttpComponentsClientHttpRequestFactory(new DecompressingHttpClient(httpClient));
		}
		
	}

}