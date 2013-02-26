package com.appglu;

import org.springframework.web.client.RestOperations;

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