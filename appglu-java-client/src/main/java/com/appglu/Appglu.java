package com.appglu;

import org.springframework.web.client.RestOperations;

public interface Appglu {

	String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	String DATE_FORMAT = "yyyy-MM-dd";
	
	String TIME_FORMAT = "'T'HH:mm:ssZ";
	
	CrudOperations crudOperations();
	
	SavedQueriesOperations savedQueriesOperations();
	
	RestOperations restOperations();

}
