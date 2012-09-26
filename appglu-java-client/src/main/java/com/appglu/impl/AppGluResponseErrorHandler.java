package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpException;
import com.appglu.AppGluHttpServerException;
import com.appglu.AppGluNotFoundException;
import com.appglu.Error;
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
			throw new AppGluNotFoundException(error);
		}
		
		switch (statusCode.series()) {
			case CLIENT_ERROR:
				throw new AppGluHttpClientException(statusCode, error);
			case SERVER_ERROR:
				throw new AppGluHttpServerException(statusCode, error);
			default:
				throw new AppGluHttpException(statusCode, error);
		}
	}

	private Error readErrorFromResponse(ClientHttpResponse response) {
		try {
			ErrorResponse errorResponse = (ErrorResponse) jsonMessageConverter.read(ErrorResponse.class, response);
			return errorResponse.getError();
		} catch (IOException e) {
			throw new HttpMessageNotReadableException("Could not parse error response", e);
		}
	}

}