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