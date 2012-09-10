package com.appglu.android;

import com.appglu.AsyncCallback;
import com.appglu.AsyncCrudOperations;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;

public final class CrudApi implements CrudOperations, AsyncCrudOperations {
	
	private CrudOperations crudOperations;
	
	private AsyncCrudOperations asyncCrudOperations;

	public CrudApi(CrudOperations crudOperations, AsyncCrudOperations asyncCrudOperations) {
		this.crudOperations = crudOperations;
		this.asyncCrudOperations = asyncCrudOperations;
	}

	public Object create(String tableName, Row row) {
		return crudOperations.create(tableName, row);
	}

	public Row read(String tableName, Object id) {
		return crudOperations.read(tableName, id);
	}

	public Row read(String tableName, Object id, boolean expandRelationships) {
		return crudOperations.read(tableName, id, expandRelationships);
	}

	public Rows readAll(String tableName) {
		return crudOperations.readAll(tableName);
	}

	public Rows readAll(String tableName, boolean expandRelationships) {
		return crudOperations.readAll(tableName, expandRelationships);
	}

	public Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments argumetns) {
		return crudOperations.readAll(tableName, expandRelationships, argumetns);
	}

	public boolean update(String tableName, Object id, Row row) {
		return crudOperations.update(tableName, id, row);
	}

	public boolean delete(String tableName, Object id) {
		return crudOperations.delete(tableName, id);
	}

	public void createInBackground(String tableName, Row row, AsyncCallback<Object> createCallback) {
		asyncCrudOperations.createInBackground(tableName, row, createCallback);
	}

	public void readInBackground(String tableName, Object id, AsyncCallback<Row> createCallback) {
		asyncCrudOperations.readInBackground(tableName, id, createCallback);
	}

	public void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> createCallback) {
		asyncCrudOperations.readInBackground(tableName, id, expandRelationships, createCallback);
	}

	public void readAllInBackground(String tableName, AsyncCallback<Rows> createCallback) {
		asyncCrudOperations.readAllInBackground(tableName, createCallback);
	}

	public void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> createCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, createCallback);
	}

	public void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> createCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, arguments, createCallback);
	}

	public void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> createCallback) {
		asyncCrudOperations.updateInBackground(tableName, id, row, createCallback);
	}

	public void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> createCallback) {
		asyncCrudOperations.deleteInBackground(tableName, id, createCallback);
	}
	
}