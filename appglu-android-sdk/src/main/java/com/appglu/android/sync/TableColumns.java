package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableColumns extends HashMap<String, Column> {

	private static final long serialVersionUID = 1L;
	
	private String tableName;
	
	private List<String> primaryKeys = new ArrayList<String>();
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSinglePrimaryKeyName() {
		if (this.hasSinglePrimaryKey()) {
			return primaryKeys.get(0);
		}
		return null;
	}

	public void addPrimaryKey(String primaryKey) {
		primaryKeys.add(primaryKey);
	}
	
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public boolean hasSinglePrimaryKey() {
		return primaryKeys.size() == 1;
	}
	
	public boolean hasComposePrimaryKey() {
		return primaryKeys.size() > 1;
	}

}