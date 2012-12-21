package com.appglu.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.TableChanges;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableChangesJsonParser;
import com.appglu.impl.json.TableVersionBody;

public final class SyncTemplate implements SyncOperations {
	
	static final String CHANGES_FOR_TABLES_URL = "/v1/sync/changes";
	
	static final String CHANGES_FOR_TABLE_URL = "/v1/sync/changes/{table}?from_version={from_version}";
	
	static final String VERSIONS_FOR_TABLES_URL = "/v1/sync/versions/{tables}";
	
	private RestOperations restOperations;
	
	private TableChangesJsonParser tableChangesJsonParser;
	
	public SyncTemplate(RestOperations restOperations, TableChangesJsonParser tableChangesJsonParser) {
		this.restOperations = restOperations;
		this.tableChangesJsonParser = tableChangesJsonParser;
	}

	public List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException {
		try {
			TableVersionBody body = new TableVersionBody(tables);
			TableChangesBody response = this.restOperations.postForObject(CHANGES_FOR_TABLES_URL, body, TableChangesBody.class);
			return response.getTables();
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	public List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException {
		return this.changesForTables(Arrays.asList(tables));
	}

	public TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		try {
			return this.restOperations.getForObject(CHANGES_FOR_TABLE_URL, TableChanges.class, tableName, version);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException {
		try {
			String tablesParameter = StringUtils.collectionToCommaDelimitedString(tables);
			TableVersionBody response = this.restOperations.getForObject(VERSIONS_FOR_TABLES_URL, TableVersionBody.class, tablesParameter);
			return response.getTables();
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException {
		return this.versionsForTables(Arrays.asList(tables));
	}
	
	public void changesForTables(TableChangesCallback tableChangesCallback, TableVersion... tables) throws AppGluRestClientException {
		this.changesForTables(Arrays.asList(tables), tableChangesCallback);
	}
	
	public void changesForTables(List<TableVersion> tables, TableChangesCallback tableChangesCallback) throws AppGluRestClientException {
		try {
			TableVersionBody body = new TableVersionBody(tables);
			InputStream inputStream = this.restOperations.postForObject(CHANGES_FOR_TABLES_URL, body, InputStream.class);
			
			this.tableChangesJsonParser.parseTableChanges(inputStream, tableChangesCallback);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

}