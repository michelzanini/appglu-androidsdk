package com.appglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.impl.AppGluTemplate;

public class AppGluTestRestClient {
	
	private AppGluTemplate appGluTemplate = new AppGluTemplate("http://localhost:8080/appglu", "173691782634", "zQO9GtlCNJjC87hQXvGcKrMyVkOHLK7f9LAaKeXSew");
	
	private CrudOperations crudOperations = appGluTemplate.crudOperations();
	
	private SavedQueriesOperations savedQueriesOperations = appGluTemplate.savedQueriesOperations();
	
	private PushOperations pushOperations = appGluTemplate.pushOperations();
	
	private AnalyticsOperations analyticsOperations = appGluTemplate.analyticsOperations();
	
	public static void main(String[] args) {
		AppGluTestRestClient restClient = new AppGluTestRestClient();
		
		Object id = restClient.crud_create();
		restClient.crud_delete(id);
		restClient.crud_update();
		restClient.crud_read();
		restClient.crud_readAll();
		
		restClient.savedQueries_executeQuery();
		
		restClient.push_registerDevice();
		restClient.push_readDevice();
		restClient.push_removeDevice();
		
		restClient.analytics_createSession();
	}
	
	private Object crud_create() {
		Row row = new Row();
		row.put("name", "test");
		
		Object id = crudOperations.create("other_table", row);
		System.out.println("crud_create: " + id);
		
		return id;
	}
	
	private void crud_update() {
		Row row = new Row();
		row.put("name", "new value");
		
		boolean success = crudOperations.update("other_table", 1, row);
		System.out.println("crud_update: " + success);
	}
	
	private void crud_delete(Object id) {
		boolean success = crudOperations.delete("other_table", id);
		System.out.println("crud_delete: " + success);
	}
	
	private void crud_read() {
		Row row = crudOperations.read("other_table", 1);
		System.out.println("crud_read: " + row);
	}
	
	private void crud_readAll() {
		Rows rows = crudOperations.readAll("other_table");
		System.out.println("crud_readAll: " + rows);
	}
	
	private void savedQueries_executeQuery() {
		QueryParams params = new QueryParams();
		
		QueryResult result = savedQueriesOperations.executeQuery("queryName1", params);
		System.out.println("savedQueries_executeQuery: " + result);
	}
	
	private void push_registerDevice() {
		Device device = new Device();
		
		device.setToken("123-0a98-48f7-9acd-456");
		device.setAlias("alias");
		device.setPlatform(DevicePlatform.ANDROID);
		
		pushOperations.registerDevice(device);
		System.out.println("push_readDevice: executed");
	}
	
	private void push_readDevice() {
		Device device = pushOperations.readDevice("123-0a98-48f7-9acd-456");
		System.out.println("push_readDevice: " + device);
	}

	private void push_removeDevice() {
		boolean success = pushOperations.removeDevice("123-0a98-48f7-9acd-456");
		System.out.println("push_removeDevice: " + success);
	}
	
	private void analytics_createSession() {
		AnalyticsSession session = new AnalyticsSession();
		session.setClientUUID("123");
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("key", "value");
		
		session.setParameters(parameters);
		
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		
		event.setName("event");
		event.setParameters(parameters);
		
		List<AnalyticsSessionEvent> events = new ArrayList<AnalyticsSessionEvent>();
		events.add(event);
		
		session.setEvents(events);
		
		analyticsOperations.createSession(session);
		System.out.println("analytics_createSession: executed");
	}

}
