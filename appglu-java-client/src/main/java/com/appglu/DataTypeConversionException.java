package com.appglu;

@SuppressWarnings("serial")
public class DataTypeConversionException extends RuntimeException {
	
	public DataTypeConversionException(Exception e) {
		super(e);
	}

	@Override
	public String getMessage() {
		if (getCause() == null) {
			return super.getMessage();
		}
		return getCause().getMessage();
	}
	
}