package com.appglu.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.impl.json.JsonMessageConverterSelector;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableChangesJsonParser;
import com.appglu.impl.json.TableVersionBody;

public final class SyncTemplate implements SyncOperations {
	
	static final String CHANGES_FOR_TABLES_URL = "/v1/sync/changes";
	
	static final String CHANGES_FOR_TABLE_URL = "/v1/sync/changes/{table}?from_version={from_version}";
	
	static final String VERSIONS_FOR_TABLES_URL = "/v1/sync/versions/{tables}";
	
	private RestOperations restOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	private TableChangesJsonParser tableChangesJsonParser;
	
	public SyncTemplate(RestOperations restOperations, HttpMessageConverter<Object> jsonMessageConverter) {
		this.restOperations = restOperations;
		this.jsonMessageConverter = jsonMessageConverter;
		this.tableChangesJsonParser = JsonMessageConverterSelector.getTableChangesJsonParser();
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
	
	public void changesForTables(TableChangesCallback tableChangesCallback, TableVersion... tables) throws AppGluRestClientException {
		this.changesForTables(Arrays.asList(tables), tableChangesCallback);
	}
	
	public void changesForTables(final List<TableVersion> tables, final TableChangesCallback tableChangesCallback) throws AppGluRestClientException {
		
		RequestCallback requestCallback = new RequestCallback() {
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				HttpEntity<Object> requestEntity = new HttpEntity<Object>(new TableVersionBody(tables));
				
				HttpHeaders requestHeaders = requestEntity.getHeaders();
				if (!requestHeaders.isEmpty()) {
					request.getHeaders().putAll(requestHeaders);
				}
				
				jsonMessageConverter.write(requestEntity.getBody(), requestHeaders.getContentType(), request);
			}
		};
		
		ResponseExtractor<Object> responseExtractor = new ResponseExtractor<Object>() {
			public Object extractData(ClientHttpResponse response) throws IOException {
				tableChangesJsonParser.parseTableChanges(response.getBody(), tableChangesCallback);
				return null;
			}
		};
		
		try {
			this.restOperations.execute(CHANGES_FOR_TABLES_URL, HttpMethod.POST, requestCallback, responseExtractor);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
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

}