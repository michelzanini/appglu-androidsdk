package com.appglu;

/**
 * Exception that can happen when mapping a {@link Row} object using a implementation of {@link RowMapper}.
 * 
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class RowMapperException extends AppGluRestClientException {
	
	public RowMapperException(String msg) {
		super(msg);
	}
	
	public RowMapperException(Throwable e) {
		super(e);
	}
	
	public RowMapperException(String msg, Exception e) {
		super(msg, e);
	}

}