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
 * {@code SavedQueriesOperations} allow you to run your previously created SQLs on AppGlu and obtain the results.
 * 
 * @see SavedQueriesOperations
 * @see AsyncSavedQueriesOperations
 * @since 1.0.0
 */
public interface SavedQueriesOperations {
	
	/**
	 * Execute a query on the server side identified by <code>queryName</code> and return the result.<br>
	 * You will first have to create the query using AppGlu's dash board web-site and give it a name. After that, using the same name, you will be able to call this method to run the query.<br>
	 * @param queryName name must match the name given when the query was created
	 * @return {@link QueryResult} containing a list of rows or the number of rows affected
	 */
	QueryResult runQuery(String queryName) throws AppGluRestClientException;
	
	/**
	 * Execute a query on the server side identified by <code>queryName</code> and return the result.<br>
	 * You will first have to create the query using AppGlu's dash board web-site and give it a name. After that, using the same name, you will be able to call this method to run the query.<br>
	 * @param queryName name must match the name given when the query was created
	 * @param params if the query has parameters, this object will have the values to replace these parameters
	 * @return {@link QueryResult} containing a list of rows or the number of rows affected
	 */
	QueryResult runQuery(String queryName, QueryParams params) throws AppGluRestClientException;

}
