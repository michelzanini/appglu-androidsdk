package com.appglu;

/**
 * Maps a {@link Row} of the database to another object.
 * 
 * @see StorageFileRowMapper
 * @since 1.0.0
 */
public interface RowMapper<T> {
	
	T mapRow(Row row) throws RowMapperException;

}