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
package com.appglu;

/**
 * {@code CrudOperations} contains create, read, update and delete operations to be applied on your tables.<br>
 * 
 * @see AsyncCrudOperations
 * @since 1.0.0
 */
public interface CrudOperations {
	
	/**
	 * Creates a new row for the specified table.
	 * @param tableName the table where the row will be created
	 * @param row contains the column names and values to be used when inserting this row on the table
	 */
	Object create(String tableName, Row row) throws AppGluRestClientException;

	/**
	 * Search the table for the specified primary key value and return the {@link Row} or return <code>null</code> if not found.
	 * @param tableName the table name to read from
	 * @param id the primary key value of the {@link Row} you want to read
	 * @return the {@link Row} or <code>null</code> if not found
	 */
	Row read(String tableName, Object id) throws AppGluRestClientException;
	
	/**
	 * Search the table for the specified primary key value and return the {@link Row} or return <code>null</code> if not found.
	 * @param tableName the table name to read from
	 * @param id the primary key value of the {@link Row} you want to read
	 * @param expandRelationships If <code>true</code> {@link Row} will also contain the rows of relationships this table has with other tables (many-to-one, many-to-many, etc)
	 * @return the {@link Row} or <code>null</code> if not found
	 */
	Row read(String tableName, Object id, boolean expandRelationships) throws AppGluRestClientException;
	
	/**
	 * Read all {@link Rows} of the specified table.
	 * @param tableName the table name to read from
	 * @return {@link Rows} containing a list of rows - <code>List&lt;Row&gt;</code>
	 */
	Rows readAll(String tableName) throws AppGluRestClientException;
	
	/**
	 * Read all {@link Rows} of the specified table.
	 * @param tableName the table name to read from
	 * @param expandRelationships If <code>true</code>, for each {@link Row} in <code>List&lt;Row&gt;</code>, it will also contain the rows of relationships this table has with other tables (many-to-one, many-to-many, etc)
	 * @return {@link Rows} containing a list of rows - <code>List&lt;Row&gt;</code>
	 */
	Rows readAll(String tableName, boolean expandRelationships) throws AppGluRestClientException;
	
	/**
	 * Read {@link Rows} of the specified table following the defined filter arguments.
	 * @param tableName the table name to read from
	 * @param expandRelationships If <code>true</code>, for each {@link Row} in <code>List&lt;Row&gt;</code>, it will also contain the rows of relationships this table has with other tables (many-to-one, many-to-many, etc)
	 * @param arguments define limit, offset, filter and sort arguments to be applied on the rows returned
	 * @return {@link Rows} containing a list of rows - <code>List&lt;Row&gt;</code>
	 */
	Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments) throws AppGluRestClientException;
	
	/**
	 * Updates a row for the specified table where the primary key matches the one passed as argument.
	 * @param tableName the table where the row will be updated
	 * @param id the primary key value of the {@link Row} you want to update
	 * @param row contains the column names and values to be used when updating this row on the table
	 * @return <code>true</code> if the row was update or <code>false</code> if not
	 */
	boolean update(String tableName, Object id, Row row) throws AppGluRestClientException;
	
	/**
	 * Deletes a row for the specified table where the primary key matches the one passed as argument.
	 * @param tableName the table where the row will be deleted
	 * @param id the primary key value of the {@link Row} you want to delete
	 * @return <code>true</code> if the row was deleted or <code>false</code> if not
	 */
	boolean delete(String tableName, Object id) throws AppGluRestClientException;
	
}
