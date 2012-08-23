package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.AppgluHttpClientException;
import com.appglu.AppgluHttpServerException;
import com.appglu.CrudOperations;
import com.appglu.Error;
import com.appglu.ErrorCode;

@SuppressWarnings("deprecation")
public class ApiExceptionsTest extends AbstractAppgluApiTest {
	
	private CrudOperations crudOperations;
	
	@Before
	public void setup() {
		super.setup();
		crudOperations = appgluTemplate.crudOperations();
	}
	
	@Test
	public void badRequest() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withResponse(compactedJson("data/error_bad_request"), responseHeaders, HttpStatus.BAD_REQUEST, ""));
		
		try {
			crudOperations.delete("user", 2);
			Assert.fail("Should had caused a exception");
		} catch (AppgluHttpClientException e) {
			Error error = e.getError();
			Assert.assertEquals(ErrorCode.EMPTY_REQUEST_BODY, error.getCode());
			Assert.assertEquals("The request body is empty.", error.getMessage());
		}
	}
	
	@Test
	public void serverError() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withResponse(compactedJson("data/error_server_error"), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR, ""));
		
		try {
			crudOperations.delete("user", 2);
			Assert.fail("Should had caused a exception");
		} catch (AppgluHttpServerException e) {
			Error error = e.getError();
			Assert.assertEquals(ErrorCode.GENERIC_SERVER_ERROR, error.getCode());
			Assert.assertEquals("An unexpected error occurred while processing your request. Please try again later.", error.getMessage());
		}
	}

}