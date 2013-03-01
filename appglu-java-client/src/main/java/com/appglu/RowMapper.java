package com.appglu;

/**
 * TODO
 */
public interface RowMapper<T> {
	
	T mapRow(Row row) throws RowMapperException;

}