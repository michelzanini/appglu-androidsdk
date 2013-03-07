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

import java.io.Serializable;

import com.appglu.impl.util.StringUtils;

/**
 * <p>Used to specify filters to be applied when retrieving rows from the server.
 * <p><code>limit</code> and <code>offset</code> are used to support pagination.<br>
 * <code>filterColumn</code> and <code>filterQuery</code> are used to filter rows using a filter query.<br>
 * <code>sortColumn</code> and <code>sortDirection</code> are used to sort the result.<br>
 * 
 * @see CrudOperations#readAll(String, boolean, ReadAllFilterArguments)
 * @since 1.0.0
 */
public class ReadAllFilterArguments implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int limit;
	
	private int offset;
	
	private String filterColumn;
	
	private String filterQuery;
	
	private String sortColumn;

	private SortDirection sortDirection;
	
	public ReadAllFilterArguments() { 
		
	}

	public ReadAllFilterArguments(int limit, int offset, String filterColumn, String filterQuery, String sortColumn, SortDirection sortDirection) {
		super();
		this.limit = limit;
		this.offset = offset;
		this.filterColumn = filterColumn;
		this.filterQuery = filterQuery;
		this.sortColumn = sortColumn;
		this.sortDirection = sortDirection;
	}
	
	public boolean hasArguments() {
		return hasLimit() || hasOffset() || hasFilterColumn() || hasFilterQuery();
	}
	
	public boolean hasLimit() {
		return limit > 0;
	}
	
	public boolean hasOffset() {
		return offset > 0;
	}
	
	public boolean hasFilterColumn() {
		return StringUtils.isNotEmpty(getFilterColumn());
	}
	
	public boolean hasFilterQuery() {
		return StringUtils.isNotEmpty(getFilterQuery());
	}
	
	public boolean hasSortColumn() {
		return StringUtils.isNotEmpty(getSortColumn());
	}
	
	public boolean hasSortDirection() {
		return getSortDirection() != null;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getFilterColumn() {
		return filterColumn;
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	public String getFilterQuery() {
		return filterQuery;
	}

	public void setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
	}
	
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public SortDirection getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}

	@Override
	public String toString() {
		return "ReadAllFilterArguments [limit=" + limit + ", offset=" + offset
				+ ", filterColumn=" + filterColumn + ", filterQuery="
				+ filterQuery + ", sortColumn=" + sortColumn
				+ ", sortDirection=" + sortDirection + "]";
	}

}
