package com.appglu.impl;

import com.appglu.AsyncCallback;
import com.appglu.AsyncCrudOperations;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;

public final class AsyncCrudTemplate implements AsyncCrudOperations {

	private final AsyncExecutor asyncExecutor;
	
	private final CrudOperations crudOperations;
	
	public AsyncCrudTemplate(AsyncExecutor asyncExecutor, CrudOperations crudOperations) {
		this.asyncExecutor = asyncExecutor;
		this.crudOperations = crudOperations;
	}

	public void createInBackground(final String tableName, final Row row, AsyncCallback<Object> primaryKeyCallback) {
		asyncExecutor.execute(primaryKeyCallback, new AsyncExecutorCallback<Object>() {
			public Object doExecute() {
				return crudOperations.create(tableName, row);
			}
		});
	}

	public void readInBackground(final String tableName, final Object id, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new AsyncExecutorCallback<Row>() {
			public Row doExecute() {
				return crudOperations.read(tableName, id);
			}
		});
	}

	public void readInBackground(final String tableName, final Object id, final boolean expandRelationships, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new AsyncExecutorCallback<Row>() {
			public Row doExecute() {
				return crudOperations.read(tableName, id, expandRelationships);
			}
		});
	}

	public void readAllInBackground(final String tableName, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new AsyncExecutorCallback<Rows>() {
			public Rows doExecute() {
				return crudOperations.readAll(tableName);
			}
		});
	}

	public void readAllInBackground(final String tableName, final boolean expandRelationships, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new AsyncExecutorCallback<Rows>() {
			public Rows doExecute() {
				return crudOperations.readAll(tableName, expandRelationships);
			}
		});
	}

	public void readAllInBackground(final String tableName, final boolean expandRelationships, final ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new AsyncExecutorCallback<Rows>() {
			public Rows doExecute() {
				return crudOperations.readAll(tableName, expandRelationships, arguments);
			}
		});
	}

	public void updateInBackground(final String tableName, final Object id, final Row row, AsyncCallback<Boolean> updateCallback) {
		asyncExecutor.execute(updateCallback, new AsyncExecutorCallback<Boolean>() {
			public Boolean doExecute() {
				return crudOperations.update(tableName, id, row);
			}
		});
	}

	public void deleteInBackground(final String tableName, final Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncExecutor.execute(deleteCallback, new AsyncExecutorCallback<Boolean>() {
			public Boolean doExecute() {
				return crudOperations.delete(tableName, id);
			}
		});
	}

}