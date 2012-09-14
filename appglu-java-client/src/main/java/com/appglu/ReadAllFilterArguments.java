package com.appglu;

import java.io.Serializable;

import org.springframework.util.StringUtils;

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
		return StringUtils.hasText(getFilterColumn());
	}
	
	public boolean hasFilterQuery() {
		return StringUtils.hasText(getFilterQuery());
	}
	
	public boolean hasSortColumn() {
		return StringUtils.hasText(getSortColumn());
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