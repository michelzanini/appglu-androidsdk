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
package com.appglu.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluHttpNotFoundException;
import com.appglu.AppGluRestClientException;
import com.appglu.Column;
import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.Rows;
import com.appglu.Table;
import com.appglu.impl.json.RowBody;
import com.appglu.impl.util.StringUtils;

public final class CrudTemplate implements CrudOperations {
	
	static final String CRUD_TABLE_URL = "/v1/tables/{table}";
	
	static final String CRUD_ENTITY_URL = "/v1/tables/{table}/{id}";
	
	private final RestOperations restOperations;
	
	public CrudTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object create(String tableName, Row row) throws AppGluRestClientException {
		try {
			RowBody primaryKey = this.restOperations.postForObject(CRUD_TABLE_URL, new RowBody(row), RowBody.class, tableName);
			return this.extractPrimaryKeyValue(primaryKey.getRow());
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	private Object extractPrimaryKeyValue(Row primaryKey) {
		ArrayList<String> keySet = new ArrayList<String>(primaryKey.keySet());
		
		if (keySet.isEmpty()) {
			return null;
		}
		
		return primaryKey.get(keySet.get(0));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Row read(String tableName, Object id) throws AppGluRestClientException {
		return this.read(tableName, id, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Row read(String tableName, Object id, boolean expandRelationships) throws AppGluRestClientException {
		try {
			String url = CRUD_ENTITY_URL + ("?expand_relationships=" + expandRelationships);
			RowBody rowBody = this.restOperations.getForObject(url, RowBody.class, tableName, id);
			return rowBody.getRow();
		} catch (AppGluHttpNotFoundException e) {
			return null;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName) throws AppGluRestClientException {
		return this.readAll(tableName, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName, boolean expandRelationships) throws AppGluRestClientException {
		return this.readAll(tableName, expandRelationships, new ReadAllFilterArguments());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rows readAll(String tableName, boolean expandRelationships, ReadAllFilterArguments arguments) throws AppGluRestClientException {
		try {
			String readAllUrl = this.buildReadAllUrl(expandRelationships, arguments);
			return this.restOperations.getForObject(readAllUrl, Rows.class, tableName);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
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

	/**
	 * {@inheritDoc}
	 */
	public boolean update(String tableName, Object id, Row row) throws AppGluRestClientException {
		try {
			this.restOperations.put(CRUD_ENTITY_URL, new RowBody(row), tableName, id);
			return true;
		} catch (AppGluHttpNotFoundException e) {
			return false;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean delete(String tableName, Object id) throws AppGluRestClientException {
		try {
			this.restOperations.delete(CRUD_ENTITY_URL, tableName, id);
			return true;
		} catch (AppGluHttpNotFoundException e) {
			return false;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	public <T> T read(Class<T> clazz, Object id) {
		return this.read(clazz, id, new ObjectRowMapper<T>(clazz));
	}

	public <T> T read(Class<T> clazz, Object id, RowMapper<T> rowMapper) throws AppGluRestClientException {
		String tableName = this.getTableNameForClass(clazz);
		
		Row row = this.read(tableName, id);
		return rowMapper.mapRow(row);
	}
	
	public <T> boolean delete(Object entity) throws AppGluRestClientException {
		Object id = this.extractPrimaryKeyValue(entity);
		return this.delete(entity.getClass(), id);
	}

	public <T> boolean delete(Class<T> clazz, Object id) throws AppGluRestClientException {
		String tableName = this.getTableNameForClass(clazz);
		return this.delete(tableName, id);
	}

	protected <T> String getTableNameForClass(Class<T> clazz) {
		String className = ClassUtils.getShortName(clazz);
		String tableName = StringUtils.underscoreName(className);
		
		if (clazz.isAnnotationPresent(Table.class)) {
			Table tableAnnotation = clazz.getAnnotation(Table.class);
			if (StringUtils.isNotEmpty(tableAnnotation.tableName())) {
				tableName = tableAnnotation.tableName();
			}
		}
		
		return tableName;
	}
	
	protected Object extractPrimaryKeyValue(final Object entity) {
		final Class<?> clazz = entity.getClass();
		
		final List<Object> primaryKeyValues = new ArrayList<Object>();
		
		FieldCallback fieldCallback = new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);
				Object value = ReflectionUtils.getField(field, entity);
				primaryKeyValues.add(value);
			}
		};
		
		FieldFilter fieldFilter = new FieldFilter() {
			public boolean matches(Field field) {
				if (!clazz.equals(field.getDeclaringClass())) {
					return false;
				}
				
				if (!ReflectionUtils.COPYABLE_FIELDS.matches(field)) {
					return false;
				}
				
				if (!field.isAnnotationPresent(Column.class)) {
					return false;
				}
				
				Column columnAnnotation = clazz.getAnnotation(Column.class);
				return columnAnnotation.primaryKey();
			}
		};
		
		ReflectionUtils.doWithFields(clazz, fieldCallback, fieldFilter);
		
		if (primaryKeyValues.isEmpty()) {
			throw new AppGluRestClientException("");
		}
		
		if (primaryKeyValues.size() > 1) {
			throw new AppGluRestClientException("");
		}
		
		return primaryKeyValues.get(0);
	}

}
