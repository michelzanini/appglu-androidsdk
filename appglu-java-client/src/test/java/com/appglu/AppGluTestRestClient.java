/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.impl.AppGluTemplate;

public class AppGluTestRestClient {
	
	private AppGluTemplate appGluTemplate = new AppGluTemplate("https://dashboard.appglu.com", "2856G3EX7p1042m", "YE79wRR2e81RW977AT563UP25o2ctd");
	
	private CrudOperations crudOperations = appGluTemplate.crudOperations();
	
	private SavedQueriesOperations savedQueriesOperations = appGluTemplate.savedQueriesOperations();
	
	private PushOperations pushOperations = appGluTemplate.pushOperations();
	
	private AnalyticsOperations analyticsOperations = appGluTemplate.analyticsOperations();
	
	private UserOperations userOperations = appGluTemplate.userOperations();
	
	private SyncOperations syncOperations = appGluTemplate.syncOperations();
	
	public static void main(String[] args) {
		AppGluTestRestClient restClient = new AppGluTestRestClient();
		restClient.callApiEndpoints();
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
		
		this.sync_changesForTables();
		this.sync_changesForTable();
		this.sync_versionsForTables();
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

	private void sync_changesForTables() {
		TableVersion loggedTable = new TableVersion("logged_table");
		TableVersion otherTable = new TableVersion("other_table");
		
		List<TableChanges> changes = this.syncOperations.changesForTables(loggedTable, otherTable);
		System.out.println("sync_changesForTables: " + changes);
	}
	
	private void sync_changesForTable() {
		TableChanges changes = this.syncOperations.changesForTable("logged_table", 2);
		System.out.println("sync_changesForTable: " + changes);
	}

	private void sync_versionsForTables() {
		List<TableVersion> versions = this.syncOperations.versionsForTables("logged_table", "other_table");
		System.out.println("sync_versionsForTables: " + versions);
	}

}
