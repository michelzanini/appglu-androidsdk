package com.appglu;

import org.springframework.http.HttpStatus;

/**
 * TODO
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