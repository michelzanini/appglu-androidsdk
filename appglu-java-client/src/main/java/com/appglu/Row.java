package com.appglu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Row {
	
	private Map<String, Object> rowColumns;
	
	public Row() {
		this.rowColumns = new HashMap<String, Object>();
	}
	
	public Row(Map<String, Object> rowColumns) {
		this.rowColumns = rowColumns;
	}
	
	public Map<String, Object> getRowColumns() {
		return rowColumns;
	}
	
	public boolean isEmpty() {
		return rowColumns.isEmpty();
	}
	
	public boolean containsKey(Object key) {
		return rowColumns.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return rowColumns.containsValue(value);
	}

	public Set<String> keySet() {
		return rowColumns.keySet();
	}

	public Collection<Object> values() {
		return rowColumns.values();
	}

	public Object get(Object key) {
		return rowColumns.get(key);
	}

	public Object put(String key, Object value) {
		return rowColumns.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		rowColumns.putAll(m);
	}

	@Override
	public String toString() {
		return "Row [columns=" + rowColumns + "]";
	}

}