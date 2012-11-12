package com.appglu;

public interface RowMapper<T> {
	
	T mapRow(Row row) throws RowMapperException;

}