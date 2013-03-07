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

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ClassUtils;

public class ClientHttpRequestFactorySelector {
	
	private static final boolean HTTP_COMPONENTS_AVAILABLE = 
		ClassUtils.isPresent("org.apache.http.client.HttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	private static final boolean DECOMPRESSING_HTTP_CLIENT_AVAILABLE = 
		ClassUtils.isPresent("org.apache.http.impl.client.DecompressingHttpClient", ClientHttpRequestFactorySelector.class.getClassLoader());
	
	public static ClientHttpRequestFactory getRequestFactory() {
		if (DECOMPRESSING_HTTP_CLIENT_AVAILABLE) {
			return DecompressingHttpClientRequestFactoryCreator.createRequestFactory();
		}
		if (HTTP_COMPONENTS_AVAILABLE) {
			return DefaultHttpClientRequestFactoryCreator.createRequestFactory();
		}
		return new SimpleClientHttpRequestFactory();
	}
	
	private static class DefaultHttpClientRequestFactoryCreator {

		public static ClientHttpRequestFactory createRequestFactory() {
			return new HttpComponentsClientHttpRequestFactory(new DefaultHttpClient());
		}
		
	}
	
	private static class DecompressingHttpClientRequestFactoryCreator {

		public static ClientHttpRequestFactory createRequestFactory() {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			return new HttpComponentsClientHttpRequestFactory(new DecompressingHttpClient(httpClient));
		}
		
	}

}
