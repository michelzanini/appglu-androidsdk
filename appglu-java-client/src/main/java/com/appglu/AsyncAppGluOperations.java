package com.appglu;

import org.springframework.web.client.RestOperations;


/**
 * Interface that describes all the <strong>asynchronous</strong> APIs available on AppGlu.
 * @see AppGluOperations
 * @since 1.0.0
 */
public interface AsyncAppGluOperations {
	
	AsyncCrudOperations asyncCrudOperations();
	
	AsyncSavedQueriesOperations asyncSavedQueriesOperations();
	
	AsyncPushOperations asyncPushOperations();
	
	AsyncAnalyticsOperations asyncAnalyticsOperations();
	
	AsyncUserOperations asyncUserOperations();
	
	AsyncSyncOperations asyncSyncOperations();
	
	AsyncStorageOperations asyncStorageOperations();
	
	RestOperations restOperations();

}