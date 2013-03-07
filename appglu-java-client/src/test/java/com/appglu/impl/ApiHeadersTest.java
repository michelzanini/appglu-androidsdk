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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.appglu.CrudOperations;

public class ApiHeadersTest extends AbstractAppGluApiTest {
	
	private CrudOperations crudOperations;
	
	@Before
	public void setup() {
		super.setup();
		crudOperations = appGluTemplate.crudOperations();
	}
	
	@Override
	protected HttpHeaders createDefaultHttpHeaders() {
		HttpHeaders defaultHttpHeaders = super.createDefaultHttpHeaders();
		defaultHttpHeaders.set("ApiVersion", "2.0");
		defaultHttpHeaders.set("AppgluVersion", "1.0");
		return defaultHttpHeaders;
	}

	@Test
	public void defaultAndAuthHeadersArePresent() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(header("ApiVersion", "2.0"))
			.andExpect(header("AppgluVersion", "1.0"))
			.andExpect(header("Authorization", "Basic YXBwbGljYXRpb25LZXk6YXBwbGljYXRpb25TZWNyZXQ="))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withSuccess().headers(responseHeaders));
		
		crudOperations.delete("user", 2);
		
		mockServer.verify();
	}

}
