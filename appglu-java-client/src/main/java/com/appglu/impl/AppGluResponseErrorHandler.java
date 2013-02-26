package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpInvalidUserSignupException;
import com.appglu.AppGluHttpNotFoundException;
import com.appglu.AppGluHttpServerException;
import com.appglu.AppGluHttpStatusCodeException;
import com.appglu.AppGluHttpUserUnauthorizedException;
import com.appglu.Error;
import com.appglu.ErrorCode;
import com.appglu.ErrorResponse;

public class AppGluResponseErrorHandler extends DefaultResponseErrorHandler {
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	public AppGluResponseErrorHandler(HttpMessageConverter<Object> httpMessageConverter) {
		this.jsonMessageConverter = httpMessageConverter;
	}
	
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		
		Error error = this.readErrorFromResponse(response);
		
		if (statusCode == HttpStatus.NOT_FOUND) {
			throw new AppGluHttpNotFoundException(error);
		}
		
		if (error.getCode() == ErrorCode.APP_USER_UNAUTHORIZED) {
			throw new AppGluHttpUserUnauthorizedException(error);
		}
		
		if (error.getCode() == ErrorCode.APP_USER_USERNAME_ALREADY_USED) {
			throw new AppGluHttpInvalidUserSignupException(statusCode.value(), error);
		}
		
		switch (statusCode.series()) {
			case CLIENT_ERROR:
				throw new AppGluHttpClientException(statusCode.value(), error);
			case SERVER_ERROR:
				throw new AppGluHttpServerException(statusCode.value(), error);
			default:
				throw new AppGluHttpStatusCodeException(statusCode.value(), error);
		}
	}

	private Error readErrorFromResponse(ClientHttpResponse response) throws IOException {
		try {
			ErrorResponse errorResponse = (ErrorResponse) jsonMessageConverter.read(ErrorResponse.class, response);
			return errorResponse.getError();
		} catch (HttpMessageConversionException e) {
			return new Error(ErrorCode.CANNOT_PARSE_RESPONSE_BODY, "Cannot parse response body");
		}
	}

}