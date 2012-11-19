package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncCrudOperations;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.Rows;

public final class CrudApi implements CrudOperations, AsyncCrudOperations {
	
	private CrudOperations crudOperations;
	
	private AsyncCrudOperations asyncCrudOperations;

	public CrudApi(CrudOperations crudOperations, AsyncCrudOperations asyncCrudOperations) {
		this.crudOperations = crudOperations;
		this.asyncCrudOperations = asyncCrudOperations;
	}

	public Object create(String tableName, Row row) throws AppGluRestClientException {
		return crudOperations.create(tableName, row);
	}

	public Row read(String tableName, Object id) throws AppGluRestClientException {
		return crudOperations.read(tableName, id);
	}

	public Row read(String tableName, Object id, boolean expandRelationships) throws AppGluRestClientException {
		return crudOperations.read(tableName, id, expandRelationships);
	}

	public Rows readAll(String tableName) throws AppGluRestClientException {
		return crudOperations.readAll(tableName);
	}

	public Rows readAll(String tableName, boolean expandRelationships) throws AppGluRestClientException {
		return crudOperations.readAll(tableName, expandRelationships);
	}

	public Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments) throws AppGluRestClientException {
		return crudOperations.readAll(tableName, expandRelationships, arguments);
	}

	public boolean update(String tableName, Object id, Row row) throws AppGluRestClientException {
		return crudOperations.update(tableName, id, row);
	}

	public boolean delete(String tableName, Object id) throws AppGluRestClientException {
		return crudOperations.delete(tableName, id);
	}
	
	public <T> T read(Class<T> clazz, Object id) throws AppGluRestClientException {
		return crudOperations.read(clazz, id);
	}

	public <T> T read(Class<T> clazz, Object id, RowMapper<T> rowMapper) throws AppGluRestClientException {
		return crudOperations.read(clazz, id, rowMapper);
	}
	
	public <T> boolean delete(Object entity) throws AppGluRestClientException {
		return crudOperations.delete(entity);
	}

	public <T> boolean delete(Class<T> clazz, Object id) throws AppGluRestClientException {
		return crudOperations.delete(clazz, id);
	}

	public void createInBackground(String tableName, Row row, AsyncCallback<Object> primaryKeyCallback) {
		asyncCrudOperations.createInBackground(tableName, row, primaryKeyCallback);
	}

	public void readInBackground(String tableName, Object id, AsyncCallback<Row> rowCallback) {
		asyncCrudOperations.readInBackground(tableName, id, rowCallback);
	}

	public void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> rowCallback) {
		asyncCrudOperations.readInBackground(tableName, id, expandRelationships, rowCallback);
	}

	public void readAllInBackground(String tableName, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, rowsCallback);
	}

	public void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, rowsCallback);
	}

	public void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, arguments, rowsCallback);
	}

	public void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> updateCallback) {
		asyncCrudOperations.updateInBackground(tableName, id, row, updateCallback);
	}

	public void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncCrudOperations.deleteInBackground(tableName, id, deleteCallback);
	}

	public <T> void readInBackground(Class<T> clazz, Object id, AsyncCallback<T> objectCallback) throws AppGluRestClientException {
		asyncCrudOperations.readInBackground(clazz, id, objectCallback);
	}

	public <T> void readInBackground(Class<T> clazz, Object id, RowMapper<T> rowMapper, AsyncCallback<T> objectCallback) throws AppGluRestClientException {
		asyncCrudOperations.readInBackground(clazz, id, rowMapper, objectCallback);
	}

}