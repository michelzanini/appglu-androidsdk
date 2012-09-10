package com.appglu.android.impl;

import org.springframework.web.client.RestOperations;

import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.android.AsyncCallback;
import com.appglu.android.CrudApi;
import com.appglu.impl.CrudTemplate;

public class CrudTemplateAsync extends CrudTemplate implements CrudApi {

	public CrudTemplateAsync(RestOperations restOperations) {
		super(restOperations);
	}

	@Override
	public void createInBackground(String tableName, Row row, AsyncCallback<Object> createCallback) {
		
	}

	@Override
	public void readInBackground(String tableName, Object id, AsyncCallback<Row> createCallback) {
		
	}

	@Override
	public void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> createCallback) {
		
	}

	@Override
	public void readAllInBackground(String tableName, AsyncCallback<Rows> createCallback) {
		
	}

	@Override
	public void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> createCallback) {
		
	}

	@Override
	public void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments argumetns, AsyncCallback<Rows> createCallback) {
		
	}

	@Override
	public void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> createCallback) {
		
	}

	@Override
	public void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> createCallback) {
		
	}

}
