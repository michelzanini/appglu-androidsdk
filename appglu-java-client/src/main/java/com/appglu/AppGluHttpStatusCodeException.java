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

/**
 * Happens when an HTTP status error of the client or server series is received (anything between 400-499 or 500-599).
 * 
 * @see AppGluHttpClientException
 * @see AppGluHttpServerException
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpStatusCodeException extends AppGluRestClientException {
	
	private final int statusCode;

	private final Error error;
	
	public AppGluHttpStatusCodeException(int statusCode, Error error) {
		super("");
		this.statusCode = statusCode;
		this.error = error;
	}

	public int getStatusCode() {
		return this.statusCode;
	}
	
	public boolean hasStatusCode() {
		return this.statusCode != 0;
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
		
		if (this.hasStatusCode()) {
			message.append("StatusCode [" + statusCode + "]");
		} else  {
			message.append("StatusCode [null]");
		}
		
		message.append(" ");
		
		if (this.hasError()) {
			message.append(error.toString());
		} else  {
			message.append("Error [null]");
		}
		
		return message.toString();
	}

}
