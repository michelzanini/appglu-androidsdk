package com.appglu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public interface Tuple extends Map<String, Object> {

	BigInteger getBigInteger(String columnName);
	
	BigDecimal getBigDecimal(String columnName);

	String getString(String columnName);

	Long getLong(String columnName);

	Integer getInt(String columnName);

	Double getDouble(String columnName);

	Float getFloat(String columnName);

	Date getDate(String columnName);

	byte[] getByteArray(String columnName);

	Byte getByte(String columnName);

	Short getShort(String columnName);

	Boolean getBoolean(String columnName);

}