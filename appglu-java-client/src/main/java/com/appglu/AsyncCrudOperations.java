package com.appglu;

public interface AsyncCrudOperations {

	void createInBackground(String tableName, Row row, AsyncCallback<Object> primaryKeyCallback);

	void readInBackground(String tableName, Object id, AsyncCallback<Row> rowCallback);
	
	void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> rowCallback);
	
	void readAllInBackground(String tableName, AsyncCallback<Rows> rowsCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> rowsCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback);
	
	void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> updateCallback);
	
	void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> deleteCallback);
	
	<T> void readInBackground(Class<T> clazz, Object id, AsyncCallback<T> objectCallback) throws AppGluRestClientException;
	
	<T> void readInBackground(Class<T> clazz, Object id, RowMapper<T> rowMapper, AsyncCallback<T> objectCallback) throws AppGluRestClientException;
	
}