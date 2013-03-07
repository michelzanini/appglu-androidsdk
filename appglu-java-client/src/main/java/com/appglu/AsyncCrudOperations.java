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
 * {@code AsyncCrudOperations} has all methods that {@link CrudOperations} has but they execute <strong>asynchronously</strong>. 
 * @see CrudOperations
 * @since 1.0.0
 */
public interface AsyncCrudOperations {

	/**
	 * Asynchronous version of {@link CrudOperations#create(String, Row)}.
	 * @see CrudOperations#create(String, Row)
	 */
	void createInBackground(String tableName, Row row, AsyncCallback<Object> primaryKeyCallback);

	/**
	 * Asynchronous version of {@link CrudOperations#read(String, Object)}.
	 * @see CrudOperations#read(String, Object)
	 */
	void readInBackground(String tableName, Object id, AsyncCallback<Row> rowCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#read(String, Object, boolean)}.
	 * @see CrudOperations#read(String, Object, boolean)
	 */
	void readInBackground(String tableName, Object id, boolean expandRelationships, AsyncCallback<Row> rowCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#readAll(String)}.
	 * @see CrudOperations#readAll(String)
	 */
	void readAllInBackground(String tableName, AsyncCallback<Rows> rowsCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#readAll(String, boolean)}.
	 * @see CrudOperations#readAll(String, boolean)
	 */
	void readAllInBackground(String tableName, boolean expandRelationships, AsyncCallback<Rows> rowsCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#readAll(String, boolean, ReadAllFilterArguments)}.
	 * @see CrudOperations#readAll(String, boolean, ReadAllFilterArguments)
	 */
	void readAllInBackground(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments, AsyncCallback<Rows> rowsCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#update(String, Object, Row)}.
	 * @see CrudOperations#update(String, Object, Row)
	 */
	void updateInBackground(String tableName, Object id, Row row, AsyncCallback<Boolean> updateCallback);
	
	/**
	 * Asynchronous version of {@link CrudOperations#delete(String, Object)}.
	 * @see CrudOperations#delete(String, Object)
	 */
	void deleteInBackground(String tableName, Object id, AsyncCallback<Boolean> deleteCallback);
	
}
