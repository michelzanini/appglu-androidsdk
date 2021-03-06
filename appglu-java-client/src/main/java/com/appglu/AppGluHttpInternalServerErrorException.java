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

import org.springframework.http.HttpStatus;

/**
 * Represents an HTTP status error code of 500 (Generic Server Error).
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpInternalServerErrorException extends AppGluHttpServerException {
	
	static final int STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	static final String ERROR_MESSAGE = "An unexpected error occurred while processing your request. Please try again later.";
	
	static final Error GENERIC_SERVER_ERROR = new Error(ErrorCode.GENERIC_SERVER_ERROR, ERROR_MESSAGE);

	public AppGluHttpInternalServerErrorException() {
		super(STATUS_CODE, GENERIC_SERVER_ERROR);
	}

}
