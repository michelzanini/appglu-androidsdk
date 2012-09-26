package com.appglu;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

@SuppressWarnings("serial")
public class AppGluHttpException extends RestClientException {

	private final HttpStatus statusCode;

	private final Error error;
	
	public AppGluHttpException(HttpStatus statusCode, Error error) {
		super(null);
		this.statusCode = statusCode;
		this.error = error;
	}

	public HttpStatus getStatusCode() {
		return this.statusCode;
	}

	public Error getError() {
		return error;
	}
	
	public boolean hasError() {
		return error != null;
	}
	
	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder();
		
		if (statusCode != null) {
			message.append("StatusCode [" + statusCode.value() + " " + statusCode.name() + "]");
		} else  {
			message.append("StatusCode [null]");
		}
		
		message.append(" ");
		
		if (error != null) {
			message.append(error.toString());
		} else  {
			message.append("Error [null]");
		}
		
		return message.toString();
	}
	
}