package com.appglu.android;

import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;

public interface CrudApi extends CrudOperations {

	void createInBackground(String tableName, Row row, AsyncCallback<Object> createCallback);

	void readInBackground(String tableName, Object id, AsyncCallback<Row> createCallback);
	
	void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> createCallback);
	
	void readAllInBackground(String tableName, AsyncCallback<Rows> createCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> createCallback);
	
	void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments argumetns, AsyncCallback<Rows> createCallback);
	
	void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> createCallback);
	
	void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> createCallback);
	
}