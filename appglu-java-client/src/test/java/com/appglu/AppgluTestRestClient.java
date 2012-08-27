package com.appglu;

import com.appglu.CrudOperations;
import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.SavedQueriesOperations;
import com.appglu.impl.AppgluTemplate;

public class AppgluTestRestClient {
	
	private AppgluTemplate appgluTemplate = new AppgluTemplate("http://localhost:8080/appglu", "173691782634", "zQO9GtlCNJjC87hQXvGcKrMyVkOHLK7f9LAaKeXSew");
	
	private CrudOperations crudOperations = appgluTemplate.crudOperations();
	
	private SavedQueriesOperations savedQueriesOperations = appgluTemplate.savedQueriesOperations();
	
	public static void main(String[] args) {
		AppgluTestRestClient restClient = new AppgluTestRestClient();
		
		Object id = restClient.crudOperations_create();
		restClient.crudOperations_delete(id);
		restClient.crudOperations_update();
		restClient.crudOperations_read();
		restClient.crudOperations_readAll();
		restClient.savedQueries_executeQuery();
	}
	
	private Object crudOperations_create() {
		Row row = new Row();
		row.put("name", "test");
		
		Object id = crudOperations.create("other_table", row);
		System.out.println("crudOperations_create: " + id);
		
		return id;
	}
	
	private void crudOperations_update() {
		Row row = new Row();
		row.put("name", "new value");
		
		boolean success = crudOperations.update("other_table", 1, row);
		System.out.println("crudOperations_update: " + success);
	}
	
	private void crudOperations_delete(Object id) {
		boolean success = crudOperations.delete("other_table", id);
		System.out.println("crudOperations_delete: " + success);
	}
	
	private void crudOperations_read() {
		Row row = crudOperations.read("other_table", 1);
		System.out.println("crudOperations_read: " + row);
	}
	
	private void crudOperations_readAll() {
		Rows rows = crudOperations.readAll("other_table");
		System.out.println("crudOperations_readAll: " + rows);
	}
	
	private void savedQueries_executeQuery() {
		QueryParams params = new QueryParams();
		
		QueryResult result = savedQueriesOperations.executeQuery("queryName5", params);
		System.out.println("savedQueries_executeQuery: " + result);
	}

}
