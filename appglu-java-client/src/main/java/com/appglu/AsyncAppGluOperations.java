package com.appglu;

import org.springframework.web.client.RestOperations;

public interface AsyncAppGluOperations {
	
	AsyncCrudOperations asyncCrudOperations();
	
	AsyncSavedQueriesOperations asyncSavedQueriesOperations();
	
	AsyncPushOperations asyncPushOperations();
	
	AsyncAnalyticsOperations asyncAnalyticsOperations();
	
	RestOperations restOperations();

}
