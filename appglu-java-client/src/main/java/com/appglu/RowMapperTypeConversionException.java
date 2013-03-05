package com.appglu;

/**
 * Happens when trying to convert a column value of a {@link Row} class to a incorrect type.<br>
 * For example, if the column type is <code>String</code> and you try to convert it to <code>Integer</code> using {@link Row#getInt(String)}.
 * 
 * @see Row
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class RowMapperTypeConversionException extends RowMapperException {
	
	public RowMapperTypeConversionException(String msg) {
		super(msg);
	}
	
	public RowMapperTypeConversionException(Exception e) {
		super(e);
	}
	
	public RowMapperTypeConversionException(String msg, Exception e) {
		super(msg, e);
	}
	
}