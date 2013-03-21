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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.appglu.impl.AppGluTemplate;
import com.appglu.impl.util.IOUtils;

public class AppGluTestRestClient {
	
	private AppGluTemplate appGluTemplate = new AppGluTemplate("2856G3EX7p1042m", "YE79wRR2e81RW977AT563UP25o2ctd", AppGluTemplate.STAGING_ENVIRONMENT);
	
	private CrudOperations crudOperations = appGluTemplate.crudOperations();
	
	private SavedQueriesOperations savedQueriesOperations = appGluTemplate.savedQueriesOperations();
	
	private PushOperations pushOperations = appGluTemplate.pushOperations();
	
	private AnalyticsOperations analyticsOperations = appGluTemplate.analyticsOperations();
	
	private UserOperations userOperations = appGluTemplate.userOperations();
	
	private SyncOperations syncOperations = appGluTemplate.syncOperations();
	
	private StorageOperations storageOperations = appGluTemplate.storageOperations();
	
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
		
		this.savedQueries_executeQuery_noParams();
		this.savedQueries_executeQuery_withParams();
		
		this.push_registerDevice();
		this.push_readDevice();
		this.push_removeDevice();
		
		this.analytics_uploadSession();
		
		User user = this.user_signup();
		this.user_logout();
		this.user_login(user);
		this.user_refreshUserProfile();
		
		this.sync_changesForTables();
		this.sync_changesForTable();
		this.sync_versionsForTables();
		
		this.storage_streamStorageFile();
	}

	private Object crud_create() {
		Row row = new Row();
		
		row.put("string", "string");
		row.put("integer", 10);
		row.put("boolean", true);
		row.put("date", new Date());
		row.put("dateTime", new Date());
		
		Object id = crudOperations.create("crud_tests", row);
		System.out.println("crud_create: " + id);
		
		return id;
	}
	
	private void crud_update(Object id) {
		Row row = new Row();
		
		row.put("string", "new-string");
		row.put("string", 20);
		row.put("boolean", false);
		row.put("date", new Date());
		row.put("dateTime", new Date());
		
		boolean success = crudOperations.update("crud_tests", id, row);
		System.out.println("crud_update: " + success);
	}
	
	private void crud_delete(Object id) {
		boolean success = crudOperations.delete("crud_tests", id);
		System.out.println("crud_delete: " + success);
	}
	
	private void crud_read(Object id) {
		Row row = crudOperations.read("crud_tests", id);
		System.out.println("crud_read: " + row);
	}
	
	private void crud_readAll() {
		Rows rows = crudOperations.readAll("crud_tests");
		System.out.println("crud_readAll: " + rows);
	}
	
	private void savedQueries_executeQuery_noParams() {
		QueryParams params = new QueryParams();
		
		QueryResult result = savedQueriesOperations.runQuery("no_params", params);
		System.out.println("savedQueries_executeQuery_noParams: " + result);
	}
	
	private void savedQueries_executeQuery_withParams() {
		QueryParams params = new QueryParams();
		params.add("boolean", "false");
		
		QueryResult result = savedQueriesOperations.runQuery("with_params", params);
		System.out.println("savedQueries_executeQuery_withParams: " + result);
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
		event.setDataTable("dataTable");
		event.setDataId("dataId");
		event.setParameters(parameters);
		
		List<AnalyticsSessionEvent> events = new ArrayList<AnalyticsSessionEvent>();
		events.add(event);
		
		session.setEvents(events);
		
		analyticsOperations.uploadSession(session);
		System.out.println("analytics_uploadSession: executed");
	}
	
	private User user_signup() {
		User user = new User(UUID.randomUUID().toString(), "password");
		
		AuthenticationResult result = userOperations.signup(user);
		System.out.println("user_signup: " + result);
		
		return user;
	}

	private void user_logout() {
		boolean succeed = userOperations.logout();
		System.out.println("user_logout: " + succeed);
	}
	
	private void user_login(User user) {
		AuthenticationResult result = userOperations.login(user.getUsername(), user.getPassword());
		System.out.println("user_login: " + result);
	}

	private void user_refreshUserProfile() {
		userOperations.refreshUserProfile();
		System.out.println("user_refreshUserProfile: executed");
	}

	private void sync_changesForTables() {
		TableVersion crudTable = new TableVersion("crud_tests");
		TableVersion productTable = new TableVersion("products");
		
		List<TableChanges> changes = this.syncOperations.changesForTables(crudTable, productTable);
		System.out.println("sync_changesForTables: " + changes);
	}
	
	private void sync_changesForTable() {
		TableChanges changes = this.syncOperations.changesForTable("crud_tests", 1);
		System.out.println("sync_changesForTable: " + changes);
	}

	private void sync_versionsForTables() {
		List<TableVersion> versions = this.syncOperations.versionsForTables("crud_tests", "products");
		System.out.println("sync_versionsForTables: " + versions);
	}
	
	private void storage_streamStorageFile() {
		StorageFile icon = new StorageFile("https://d27pqr4ityvq45.cloudfront.net/appglu_testapp1/7311a9bd-5b2b-4159-8213-86cf5f3ba852-device-nav-n4.png");
		this.storageOperations.streamStorageFile(icon, new InputStreamCallback() {
			@Override
			public void doWithInputStream(InputStream inputStream) throws IOException {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				IOUtils.copy(inputStream, outputStream);
				System.out.println("storage_streamStorageFile: " + outputStream.size());
			}
		});
	}

}
