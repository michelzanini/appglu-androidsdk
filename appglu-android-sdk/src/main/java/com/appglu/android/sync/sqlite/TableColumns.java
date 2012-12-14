package com.appglu.android.sync.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableColumns extends HashMap<String, Column> {

	private static final long serialVersionUID = 1L;
	
	private List<String> primaryKeys = new ArrayList<String>();
	
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