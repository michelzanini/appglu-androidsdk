package com.appglu.impl;

import java.util.concurrent.Callable;

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
		asyncExecutor.execute(primaryKeyCallback, new Callable<Object>() {
			public Object call() {
				return crudOperations.create(tableName, row);
			}
		});
	}

	public void readInBackground(final String tableName, final Object id, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new Callable<Row>() {
			public Row call() {
				return crudOperations.read(tableName, id);
			}
		});
	}

	public void readInBackground(final String tableName, final Object id, final boolean expandRelationships, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new Callable<Row>() {
			public Row call() {
				return crudOperations.read(tableName, id, expandRelationships);
			}
		});
	}

	public void readAllInBackground(final String tableName, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName);
			}
		});
	}

	public void readAllInBackground(final String tableName, final boolean expandRelationships, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName, expandRelationships);
			}
		});
	}

	public void readAllInBackground(final String tableName, final boolean expandRelationships, final ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName, expandRelationships, arguments);
			}
		});
	}

	public void updateInBackground(final String tableName, final Object id, final Row row, AsyncCallback<Boolean> updateCallback) {
		asyncExecutor.execute(updateCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.update(tableName, id, row);
			}
		});
	}

	public void deleteInBackground(final String tableName, final Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncExecutor.execute(deleteCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.delete(tableName, id);
			}
		});
	}

}