package com.appglu;

import org.springframework.web.client.RestOperations;

/**
 * Interface that describes all the APIs available on AppGlu.
 * @since 1.0.0
 */
public interface AppGluOperations {

	CrudOperations crudOperations();
	
	SavedQueriesOperations savedQueriesOperations();
	
	PushOperations pushOperations();
	
	AnalyticsOperations analyticsOperations();
	
	UserOperations userOperations();
	
	SyncOperations syncOperations();
	
	StorageOperations storageOperations();
	
	RestOperations restOperations();

}