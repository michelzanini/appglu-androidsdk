package com.appglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.impl.AppGluTemplate;

public class AppGluTestRestClient {
	
	private AppGluOperations appGluTemplate = new AppGluTemplate("http://192.168.40.78:8080/appglu", "173691782634", "zQO9GtlCNJjC87hQXvGcKrMyVkOHLK7f9LAaKeXSew");
	
	private CrudOperations crudOperations = appGluTemplate.crudOperations();
	
	private SavedQueriesOperations savedQueriesOperations = appGluTemplate.savedQueriesOperations();
	
	private PushOperations pushOperations = appGluTemplate.pushOperations();
	
	private AnalyticsOperations analyticsOperations = appGluTemplate.analyticsOperations();
	
	private UserOperations userOperations = appGluTemplate.userOperations();
	
	private SyncOperations syncOperations = appGluTemplate.syncOperations();
	
	public static void main(String[] args) {
		AppGluTestRestClient restClient = new AppGluTestRestClient();
		//restClient.callApiEndpoints();
		
		restClient.sync_changesForTables();
	}
	
	private void sync_changesForTables() {
		VersionedTable loggedTable = new VersionedTable("logged_table");
		VersionedTable otherTable = new VersionedTable("other_table");
		
		List<VersionedTableChanges> changes = this.syncOperations.changesForTables(loggedTable, otherTable);
		
		System.out.println(changes);
	}

	public void callApiEndpoints() {
		Object id = this.crud_create();
		this.crud_update(id);
		this.crud_read(id);
		this.crud_delete(id);
		this.crud_readAll();
		
		this.savedQueries_executeQuery();
		
		this.push_registerDevice();
		this.push_readDevice();
		this.push_removeDevice();
		
		this.analytics_uploadSession();
		
		this.user_signup();
		this.user_logout();
		this.user_login();
		this.user_refreshUserProfile();
	}
	
	private Object crud_create() {
		Row row = new Row();
		row.put("username", "username1");
		row.put("password", "password1");
		
		Object id = crudOperations.create("appglu_app_users", row);
		System.out.println("crud_create: " + id);
		
		return id;
	}
	
	private void crud_update(Object id) {
		Row row = new Row();
		row.put("username", "username2");
		row.put("password", "password2");
		
		boolean success = crudOperations.update("appglu_app_users", id, row);
		System.out.println("crud_update: " + success);
	}
	
	private void crud_delete(Object id) {
		boolean success = crudOperations.delete("appglu_app_users", id);
		System.out.println("crud_delete: " + success);
	}
	
	private void crud_read(Object id) {
		Row row = crudOperations.read("appglu_app_users", id);
		System.out.println("crud_read: " + row);
	}
	
	private void crud_readAll() {
		Rows rows = crudOperations.readAll("appglu_app_users");
		System.out.println("crud_readAll: " + rows);
	}
	
	private void savedQueries_executeQuery() {
		QueryParams params = new QueryParams();
		
		QueryResult result = savedQueriesOperations.runQuery("queryName1", params);
		System.out.println("savedQueries_executeQuery: " + result);
	}
	
	private void push_registerDevice() {
		Device device = new Device();
		
		device.setToken("123-0a98-48f7-9acd-456");
		device.setAlias("alias");
		device.setPlatform(DevicePlatform.ANDROID);
		device.setAppIdentifier("appIdentifier");
		
		pushOperations.registerDevice(device);
		System.out.println("push_registerDevice: executed");
	}
	
	private void push_readDevice() {
		Device device = pushOperations.readDevice("123-0a98-48f7-9acd-456");
		System.out.println("push_readDevice: " + device);
	}

	private void push_removeDevice() {
		boolean success = pushOperations.removeDevice("123-0a98-48f7-9acd-456");
		System.out.println("push_removeDevice: " + success);
	}
	
	private void analytics_uploadSession() {
		AnalyticsSession session = new AnalyticsSession();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("key", "value");
		
		session.setParameters(parameters);
		
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		
		event.setName("event");
		event.setParameters(parameters);
		
		List<AnalyticsSessionEvent> events = new ArrayList<AnalyticsSessionEvent>();
		events.add(event);
		
		session.setEvents(events);
		
		analyticsOperations.uploadSession(session);
		System.out.println("analytics_uploadSession: executed");
	}
	
	private void user_signup() {
		AuthenticationResult result = userOperations.signup(new User("username", "password"));
		System.out.println("user_signup: " + result);
	}

	private void user_logout() {
		boolean succeed = userOperations.logout();
		System.out.println("user_logout: " + succeed);
	}
	
	private void user_login() {
		AuthenticationResult result = userOperations.login("username", "password");
		System.out.println("user_login: " + result);
	}

	private void user_refreshUserProfile() {
		userOperations.refreshUserProfile();
		System.out.println("user_refreshUserProfile: executed");
	}

}