package com.appglu;

public interface CrudOperations {
	
	Object create(String tableName, Row row) throws AppGluRestClientException;

	Row read(String tableName, Object id) throws AppGluRestClientException;
	
	Row read(String tableName, Object id, boolean expandRelationships) throws AppGluRestClientException;
	
	Rows readAll(String tableName) throws AppGluRestClientException;
	
	Rows readAll(String tableName, boolean expandRelationships) throws AppGluRestClientException;
	
	Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments argumetns) throws AppGluRestClientException;
	
	boolean update(String tableName, Object id, Row row) throws AppGluRestClientException;
	
	boolean delete(String tableName, Object id) throws AppGluRestClientException;
	
}