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
package com.appglu;

import java.io.Serializable;

/**
 * If an <strong>asynchronous</strong> method throws an exception, that exception is wrapped with {@code ExceptionWrapper} to provide a easy way of extracting error code and error messages.
 * 
 * @since 1.0.0
 */
public class ExceptionWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

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
		return null;
	}
	
	public Error getError() {
		if (this.isHttpStatusCodeException()) {
			AppGluHttpStatusCodeException httpStatusCodeException = this.getHttpStatusCodeException();
			return httpStatusCodeException.getError();
		}
		return null;
	}
	
	public ErrorCode getErrorCode() {
		Error error = this.getError();
		if (error == null) {
			return null;
		}
		return error.getCode();
	}
	
	public String getErrorMessage() {
		Error error = this.getError();
		if (error == null) {
			return null;
		}
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
