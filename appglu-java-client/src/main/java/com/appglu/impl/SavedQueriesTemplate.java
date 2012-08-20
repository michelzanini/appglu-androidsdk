package com.appglu.impl;

import org.springframework.web.client.RestOperations;

import com.appglu.SavedQueriesOperations;

public class SavedQueriesTemplate implements SavedQueriesOperations {

	@SuppressWarnings("unused")
	private final RestOperations restOperations;
	
	public SavedQueriesTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
}
