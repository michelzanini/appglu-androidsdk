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
 * {@code AsyncSavedQueriesOperations} has all methods that {@link SavedQueriesOperations} has but they execute <strong>asynchronously</strong>. 
 * @see SavedQueriesOperations
 * @since 1.0.0
 */
public interface AsyncSavedQueriesOperations {
	
	/**
	 * Asynchronous version of {@link SavedQueriesOperations#runQuery(String)}.
	 * @see SavedQueriesOperations#runQuery(String)
	 */
	void runQueryInBackground(String queryName, AsyncCallback<QueryResult> queryResultCallback);
	
	/**
	 * Asynchronous version of {@link SavedQueriesOperations#runQuery(String, QueryParams)}.
	 * @see SavedQueriesOperations#runQuery(String, QueryParams)
	 */
	void runQueryInBackground(String queryName, QueryParams params, AsyncCallback<QueryResult> queryResultCallback);

}
