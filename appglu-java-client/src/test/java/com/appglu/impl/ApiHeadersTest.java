package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.header;
import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withSuccess;

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