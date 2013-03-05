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