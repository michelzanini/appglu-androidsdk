package com.appglu;

public interface CrudOperations {
	
	Object create(String tableName, Row row);

	Row read(String tableName, Object id);
	
	Row read(String tableName, Object id, boolean expandRelationships);
	
	Rows readAll(String tableName);
	
	Rows readAll(String tableName, boolean expandRelationships);
	
	Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments argumetns);
	
	boolean update(String tableName, Object id, Row row);
	
	boolean delete(String tableName, Object id);
	
}