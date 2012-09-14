package com.appglu.impl;

import java.util.ArrayList;

import org.springframework.web.client.RestOperations;

import com.appglu.AppGluNotFoundException;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.impl.json.RowBody;

public final class CrudTemplate implements CrudOperations {
	
	static final String CRUD_TABLE_URL = "/v1/tables/{table}";
	
	static final String CRUD_ENTITY_URL = "/v1/tables/{table}/{id}";
	
	private final RestOperations restOperations;
	
	public CrudTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
	public Object create(String tableName, Row row) {
		RowBody primaryKey = this.restOperations.postForObject(CRUD_TABLE_URL, new RowBody(row), RowBody.class, tableName);
		return this.extractPrimaryKeyValue(primaryKey.getRow());
	}

	private Object extractPrimaryKeyValue(Row primaryKey) {
		ArrayList<String> keySet = new ArrayList<String>(primaryKey.keySet());
		
		if (keySet.isEmpty()) {
			return null;
		}
		
		return primaryKey.get(keySet.get(0));
	}
	
	public Row read(String tableName, Object id) {
		return this.read(tableName, id, false);
	}
	
	public Row read(String tableName, Object id, boolean expandRelationships) {
		try {
			String url = CRUD_ENTITY_URL + ("?expand_relationships=" + expandRelationships);
			RowBody rowBody = this.restOperations.getForObject(url, RowBody.class, tableName, id);
			return rowBody.getRow();
		} catch (AppGluNotFoundException e) {
			return null;
		}
	}
	
	public Rows readAll(String tableName) {
		return this.readAll(tableName, false);
	}
	
	public Rows readAll(String tableName, boolean expandRelationships) {
		return this.readAll(tableName, expandRelationships, new ReadAllFilterArguments());
	}
	
	public Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments) {
		String readAllUrl = this.buildReadAllUrl(expandRelationships, arguments);
		return this.restOperations.getForObject(readAllUrl, Rows.class, tableName);
	}
	
	private String buildReadAllUrl(boolean expandRelationships, ReadAllFilterArguments arguments) {
		StringBuilder url = new StringBuilder();
		
		url.append(CRUD_TABLE_URL);
		
		url.append("?");
		if (arguments.hasLimit()) {
			url.append("limit=" + arguments.getLimit());
			url.append("&");
		}
		if (arguments.hasOffset()) {
			url.append("offset=" + arguments.getOffset());
			url.append("&");
		}
		if (arguments.hasFilterColumn()) {
			url.append("filter_column=" + arguments.getFilterColumn());
			url.append("&");
		}
		if (arguments.hasFilterQuery()) {
			url.append("filter_query=" + arguments.getFilterQuery());
			url.append("&");
		}
		if (arguments.hasSortColumn()) {
			url.append("sort_column=" + arguments.getSortColumn());
			url.append("&");
		}
		if (arguments.hasSortDirection()) {
			url.append("sort_direction=" + arguments.getSortDirection());
			url.append("&");
		}
		url.append("expand_relationships=" + expandRelationships);
		
		return url.toString();
	}

	public boolean update(String tableName, Object id, Row row) {
		try {
			this.restOperations.put(CRUD_ENTITY_URL, new RowBody(row), tableName, id);
			return true;
		} catch (AppGluNotFoundException e) {
			return false;
		}
	}

	public boolean delete(String tableName, Object id) {
		try {
			this.restOperations.delete(CRUD_ENTITY_URL, tableName, id);
			return true;
		} catch (AppGluNotFoundException e) {
			return false;
		}
	}

}