package com.appglu.android.sync.sqlite;

import java.util.HashMap;

import com.appglu.impl.util.StringUtils;

public class TableColumns extends HashMap<String, Column> {

	private static final long serialVersionUID = 1L;
	
	private String primaryKeyName;
	
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public boolean hasPrimaryKey() {
		return StringUtils.isNotEmpty(this.primaryKeyName);
	}

}