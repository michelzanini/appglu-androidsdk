package com.appglu;

import org.springframework.util.StringUtils;

public class ReadAllFilterArguments {
	
	private int limit;
	
	private int offset;
	
	private String filterColumn;
	
	private String filterQuery;
	
	public ReadAllFilterArguments() { 
		
	}

	public ReadAllFilterArguments(int limit, int offset, String filterColumn, String filterQuery) {
		super();
		this.limit = limit;
		this.offset = offset;
		this.filterColumn = filterColumn;
		this.filterQuery = filterQuery;
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
		return StringUtils.hasText(getFilterColumn());
	}
	
	public boolean hasFilterQuery() {
		return StringUtils.hasText(getFilterQuery());
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

	@Override
	public String toString() {
		return "ReadAllFilterArguments [limit=" + limit + ", offset=" + offset
			+ ", filterColumn=" + filterColumn + ", filterQuery="
			+ filterQuery + "]";
	}
	
}