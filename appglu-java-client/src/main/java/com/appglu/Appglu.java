package com.appglu;

import org.springframework.web.client.RestOperations;

public interface Appglu {
	
	CrudOperations crudOperations();
	
	SavedQueriesOperations savedQueriesOperations();
	
	RestOperations restOperations();

}
