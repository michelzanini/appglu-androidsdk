package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import com.appglu.AppgluHttpClientException;
import com.appglu.AppgluHttpServerException;
import com.appglu.AppgluNotFoundException;
import com.appglu.Error;
import com.appglu.ErrorResponse;

public class AppgluResponseErrorHandler extends DefaultResponseErrorHandler {
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	public AppgluResponseErrorHandler(HttpMessageConverter<Object> httpMessageConverter) {
		this.jsonMessageConverter = httpMessageConverter;
	}
	
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		
		Error error = this.readErrorFromResponse(response);
		
		if (statusCode == HttpStatus.NOT_FOUND) {
			throw new AppgluNotFoundException(error);
		}
		
		switch (statusCode.series()) {
			case CLIENT_ERROR:
				throw new AppgluHttpClientException(statusCode, error);
			case SERVER_ERROR:
				throw new AppgluHttpServerException(statusCode, error);
			default:
				throw new RestClientException("Unknown status code [" + statusCode + "]");
		}
	}

	private Error readErrorFromResponse(ClientHttpResponse response) {
		try {
			ErrorResponse errorResponse = (ErrorResponse) jsonMessageConverter.read(ErrorResponse.class, response);
			return errorResponse.getError();
		} catch (HttpMessageNotReadableException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}