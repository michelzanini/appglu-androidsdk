package com.appglu.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;
import com.appglu.impl.json.VersionedTableBody;
import com.appglu.impl.json.VersionedTableChangesBody;

public final class SyncTemplate implements SyncOperations {
	
	static final String CHANGES_FOR_TABLES_URL = "/v1/sync/changes";
	
	static final String CHANGES_FOR_TABLE_URL = "/v1/sync/changes/{table}?from_version={from_version}";
	
	static final String VERSIONS_FOR_TABLES_URL = "/v1/sync/versions/{tables}";
	
	private RestOperations restOperations;
	
	public SyncTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	public List<VersionedTableChanges> changesForTables(List<VersionedTable> tables) throws AppGluRestClientException {
		try {
			VersionedTableBody body = new VersionedTableBody(tables);
			VersionedTableChangesBody response = this.restOperations.postForObject(CHANGES_FOR_TABLES_URL, body, VersionedTableChangesBody.class);
			return response.getTables();
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	public List<VersionedTableChanges> changesForTables(VersionedTable... tables) throws AppGluRestClientException {
		return this.changesForTables(Arrays.asList(tables));
	}

	public VersionedTableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		try {
			return this.restOperations.getForObject(CHANGES_FOR_TABLE_URL, VersionedTableChanges.class, tableName, version);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public List<VersionedTable> versionsForTables(List<String> tables) throws AppGluRestClientException {
		try {
			String tablesParameter = StringUtils.collectionToCommaDelimitedString(tables);
			VersionedTableBody response = this.restOperations.getForObject(VERSIONS_FOR_TABLES_URL, VersionedTableBody.class, tablesParameter);
			return response.getTables();
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public List<VersionedTable> versionsForTables(String... tables) throws AppGluRestClientException {
		return this.versionsForTables(Arrays.asList(tables));
	}

}