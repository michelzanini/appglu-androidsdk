package com.appglu;

public interface AsyncCrudOperations {

	void createInBackground(String tableName, Row row, AsyncCallback<Object> createCallback);

	void readInBackground(String tableName, Object id, AsyncCallback<Row> createCallback);
	
	void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> createCallback);
	
	void readAllInBackground(String tableName, AsyncCallback<Rows> createCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> createCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> createCallback);
	
	void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> createCallback);
	
	void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> createCallback);
	
}