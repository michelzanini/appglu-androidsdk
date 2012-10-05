package com.appglu;

import org.springframework.http.HttpStatus;

public class ExceptionWrapper {

	private Exception exception;

	public ExceptionWrapper(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public Integer getHttpStatusCode() {
		if (this.isHttpStatusCodeException()) {
			AppGluHttpStatusCodeException httpStatusCodeException = this.getHttpStatusCodeException();
			return httpStatusCodeException.getStatusCode();
		}
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}
	
	public Error getError() {
		if (this.isHttpStatusCodeException()) {
			AppGluHttpStatusCodeException httpStatusCodeException = this.getHttpStatusCodeException();
			return httpStatusCodeException.getError();
		}
		return new Error(ErrorCode.GENERIC_SERVER_ERROR, "An unexpected error occurred while processing your request. Please try again later.");
	}
	
	public ErrorCode getErrorCode() {
		Error error = this.getError();
		return error.getCode();
	}
	
	public String getErrorMessage() {
		Error error = this.getError();
		return error.getMessage();
	}
	
	public boolean isHttpStatusCodeException() {
		return exception instanceof AppGluHttpStatusCodeException;
	}
	
	public boolean isHttpClientException() {
		return exception instanceof AppGluHttpClientException;
	}
	
	public boolean isHttpServerException() {
		return exception instanceof AppGluHttpServerException;
	}
	
	public AppGluHttpStatusCodeException getHttpStatusCodeException() {
		return (AppGluHttpStatusCodeException) this.exception;
	}
	
	public AppGluHttpClientException getHttpClientException() {
		return (AppGluHttpClientException) this.exception;
	}
	
	public AppGluHttpServerException getHttpServerException() {
		return (AppGluHttpServerException) this.exception;
	}
	
}