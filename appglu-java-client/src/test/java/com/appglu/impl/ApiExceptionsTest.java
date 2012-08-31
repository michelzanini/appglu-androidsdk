package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withBadRequest;
import static org.springframework.test.web.client.ResponseCreators.withServerError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpServerException;
import com.appglu.CrudOperations;
import com.appglu.Error;
import com.appglu.ErrorCode;

public class ApiExceptionsTest extends AbstractAppGluApiTest {
	
	private CrudOperations crudOperations;
	
	@Before
	public void setup() {
		super.setup();
		crudOperations = appGluTemplate.crudOperations();
	}
	
	@Test
	public void badRequest() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withBadRequest().body(compactedJson("data/error_bad_request")).headers(responseHeaders));
		
		try {
			crudOperations.delete("user", 2);
			Assert.fail("Should had caused a exception");
		} catch (AppGluHttpClientException e) {
			Error error = e.getError();
			Assert.assertEquals(ErrorCode.EMPTY_REQUEST_BODY, error.getCode());
			Assert.assertEquals("The request body is empty.", error.getMessage());
		}
		
		mockServer.verify();
	}
	
	@Test
	public void serverError() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withServerError().body(compactedJson("data/error_server_error")).headers(responseHeaders));
		
		try {
			crudOperations.delete("user", 2);
			Assert.fail("Should had caused a exception");
		} catch (AppGluHttpServerException e) {
			Error error = e.getError();
			Assert.assertEquals(ErrorCode.GENERIC_SERVER_ERROR, error.getCode());
			Assert.assertEquals("An unexpected error occurred while processing your request. Please try again later.", error.getMessage());
		}
		
		mockServer.verify();
	}

}