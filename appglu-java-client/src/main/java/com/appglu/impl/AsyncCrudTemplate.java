/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncCrudOperations;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.Rows;

public final class AsyncCrudTemplate implements AsyncCrudOperations {

	private final AsyncExecutor asyncExecutor;
	
	private final CrudOperations crudOperations;
	
	public AsyncCrudTemplate(AsyncExecutor asyncExecutor, CrudOperations crudOperations) {
		this.asyncExecutor = asyncExecutor;
		this.crudOperations = crudOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createInBackground(final String tableName, final Row row, AsyncCallback<Object> primaryKeyCallback) {
		asyncExecutor.execute(primaryKeyCallback, new Callable<Object>() {
			public Object call() {
				return crudOperations.create(tableName, row);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readInBackground(final String tableName, final Object id, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new Callable<Row>() {
			public Row call() {
				return crudOperations.read(tableName, id);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readInBackground(final String tableName, final Object id, final boolean expandRelationships, AsyncCallback<Row> rowCallback) {
		asyncExecutor.execute(rowCallback, new Callable<Row>() {
			public Row call() {
				return crudOperations.read(tableName, id, expandRelationships);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(final String tableName, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(final String tableName, final boolean expandRelationships, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName, expandRelationships);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(final String tableName, final boolean expandRelationships, final ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback) {
		asyncExecutor.execute(rowsCallback, new Callable<Rows>() {
			public Rows call() {
				return crudOperations.readAll(tableName, expandRelationships, arguments);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateInBackground(final String tableName, final Object id, final Row row, AsyncCallback<Boolean> updateCallback) {
		asyncExecutor.execute(updateCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.update(tableName, id, row);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteInBackground(final String tableName, final Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncExecutor.execute(deleteCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.delete(tableName, id);
			}
		});
	}
	
	/*
	 * Crud Operations using classes and annotations
	 */

	/**
	 * {@inheritDoc}
	 */
	public <T> void readInBackground(final Class<T> clazz, final Object id, AsyncCallback<T> objectCallback) {
		asyncExecutor.execute(objectCallback, new Callable<T>() {
			public T call() {
				return crudOperations.read(clazz, id);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> void readInBackground(final Class<T> clazz, final Object id, final RowMapper<T> rowMapper, AsyncCallback<T> objectCallback) {
		asyncExecutor.execute(objectCallback, new Callable<T>() {
			public T call() {
				return crudOperations.read(clazz, id, rowMapper);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteInBackground(final Object entity, AsyncCallback<Boolean> deleteCallback) {
		asyncExecutor.execute(deleteCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.delete(entity);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> void deleteInBackground(final Class<T> clazz, final Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncExecutor.execute(deleteCallback, new Callable<Boolean>() {
			public Boolean call() {
				return crudOperations.delete(clazz, id);
			}
		});
	}

}