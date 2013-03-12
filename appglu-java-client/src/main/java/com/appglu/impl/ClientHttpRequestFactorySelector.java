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

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ClassUtils;

/**
 * Decides which HTTP client will be used at runtime. Will try first Apache HTTP Commons Client, and if not found, the standard JSE HTTPURLConnection.
 * @since 1.0.0
 */
public class ClientHttpRequestFactorySelector {
	
	/**
	 * Default timeout for HTTP operations: 20 seconds by default.
	 */
	public static final int HTTP_CLIENT_TIMEOUT = 20 * 1000;
	
	private static final boolean ANDROID_ENVIRONMENT = ClassUtils.isPresent("android.os.Build", AppGluTemplate.class.getClassLoader());

	private static final boolean HTTP_COMPONENTS_AVAILABLE = 
		ClassUtils.isPresent("org.apache.http.client.HttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	private static final boolean DECOMPRESSING_HTTP_CLIENT_AVAILABLE = 
		ClassUtils.isPresent("org.apache.http.impl.client.DecompressingHttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	public static boolean supportsGzipCompression() {
		if (DECOMPRESSING_HTTP_CLIENT_AVAILABLE) {
			return true;
		}
		if (HTTP_COMPONENTS_AVAILABLE) {
			return false;
		}
		if (ANDROID_ENVIRONMENT) {
			return true;
		}
		return false;
	}
	
	public static ClientHttpRequestFactory getRequestFactory() {
		if (DECOMPRESSING_HTTP_CLIENT_AVAILABLE) {
			return DecompressingHttpClientRequestFactoryCreator.createRequestFactory();
		}
		if (HTTP_COMPONENTS_AVAILABLE) {
			return DefaultHttpClientRequestFactoryCreator.createRequestFactory();
		}
		return createSimpleClientHttpRequestFactory();
	}

	public static SimpleClientHttpRequestFactory createSimpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		
		simpleClientHttpRequestFactory.setConnectTimeout(HTTP_CLIENT_TIMEOUT);
		simpleClientHttpRequestFactory.setReadTimeout(HTTP_CLIENT_TIMEOUT);
		
		return simpleClientHttpRequestFactory;
	}
	
	private static class DefaultHttpClientRequestFactoryCreator {
		
		@SuppressWarnings("deprecation")
		public static DefaultHttpClient createDefaultHttpClient() {
			HttpParams params = new BasicHttpParams();

	        HttpConnectionParams.setStaleCheckingEnabled(params, false);

	        HttpConnectionParams.setConnectionTimeout(params, HTTP_CLIENT_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(params, HTTP_CLIENT_TIMEOUT);
	        HttpConnectionParams.setSocketBufferSize(params, 8192);

	        HttpClientParams.setRedirecting(params, false);

	        SchemeRegistry schemeRegistry = new SchemeRegistry();
	        
	        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	        ClientConnectionManager manager = 
	        	new org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager(params, schemeRegistry);
	        
	        return new DefaultHttpClient(manager, params);
		}

		public static ClientHttpRequestFactory createRequestFactory() {
			return new HttpComponentsClientHttpRequestFactory(createDefaultHttpClient());
		}
		
	}
	
	private static class DecompressingHttpClientRequestFactoryCreator {

		public static ClientHttpRequestFactory createRequestFactory() {
			DefaultHttpClient httpClient = DefaultHttpClientRequestFactoryCreator.createDefaultHttpClient();
			return new HttpComponentsClientHttpRequestFactory(new DecompressingHttpClient(httpClient));
		}
		
	}

}
