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
package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncCrudOperations;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.Rows;

/**
 * {@code CrudApi} contains create, read, update and delete operations to be applied on your tables.<br>
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.CrudOperations
 * @see com.appglu.AsyncCrudOperations
 * @since 1.0.0
 */
public final class CrudApi implements CrudOperations, AsyncCrudOperations {
	
	private CrudOperations crudOperations;
	
	private AsyncCrudOperations asyncCrudOperations;

	public CrudApi(CrudOperations crudOperations, AsyncCrudOperations asyncCrudOperations) {
		this.crudOperations = crudOperations;
		this.asyncCrudOperations = asyncCrudOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object create(String tableName, Row row) throws AppGluRestClientException {
		return crudOperations.create(tableName, row);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Row read(String tableName, Object id) throws AppGluRestClientException {
		return crudOperations.read(tableName, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Row read(String tableName, Object id, boolean expandRelationships) throws AppGluRestClientException {
		return crudOperations.read(tableName, id, expandRelationships);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(Class<T> clazz, Object id) throws AppGluRestClientException {
		return crudOperations.read(clazz, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T read(Class<T> clazz, Object id, RowMapper<T> rowMapper) throws AppGluRestClientException {
		return crudOperations.read(clazz, id, rowMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName) throws AppGluRestClientException {
		return crudOperations.readAll(tableName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName, boolean expandRelationships) throws AppGluRestClientException {
		return crudOperations.readAll(tableName, expandRelationships);
	}

	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments) throws AppGluRestClientException {
		return crudOperations.readAll(tableName, expandRelationships, arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean update(String tableName, Object id, Row row) throws AppGluRestClientException {
		return crudOperations.update(tableName, id, row);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean delete(String tableName, Object id) throws AppGluRestClientException {
		return crudOperations.delete(tableName, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> boolean delete(Object entity) throws AppGluRestClientException {
		return crudOperations.delete(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> boolean delete(Class<T> clazz, Object id) throws AppGluRestClientException {
		return crudOperations.delete(clazz, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void createInBackground(String tableName, Row row, AsyncCallback<Object> primaryKeyCallback) {
		asyncCrudOperations.createInBackground(tableName, row, primaryKeyCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readInBackground(String tableName, Object id, AsyncCallback<Row> rowCallback) {
		asyncCrudOperations.readInBackground(tableName, id, rowCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> rowCallback) {
		asyncCrudOperations.readInBackground(tableName, id, expandRelationships, rowCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> void readInBackground(Class<T> clazz, Object id, AsyncCallback<T> objectCallback) throws AppGluRestClientException {
		asyncCrudOperations.readInBackground(clazz, id, objectCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> void readInBackground(Class<T> clazz, Object id, RowMapper<T> rowMapper, AsyncCallback<T> objectCallback) throws AppGluRestClientException {
		asyncCrudOperations.readInBackground(clazz, id, rowMapper, objectCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(String tableName, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, rowsCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, rowsCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback) {
		asyncCrudOperations.readAllInBackground(tableName, expandRelationships, arguments, rowsCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> updateCallback) {
		asyncCrudOperations.updateInBackground(tableName, id, row, updateCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> deleteCallback) {
		asyncCrudOperations.deleteInBackground(tableName, id, deleteCallback);
	}

}