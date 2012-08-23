package com.appglu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	
	public void addManyToOneRelationship(String relationshipName, Row row) {
		rowColumns.put(relationshipName, row.getRowColumns());
	}
	
	public void addManyToManyRelationship(String relationshipName, List<Row> rows) {
		List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
		for (Row row : rows) {
			entries.add(row.getRowColumns());	
		}
		rowColumns.put(relationshipName, entries);
	}
	
	public Row getManyToOneRelationship(String relationshipName) {
		Object relationship = rowColumns.get(relationshipName);
		if (relationship == null) {
			return null;
		}
		return new Row(this.extractRowColumns(relationship));
	}
	
	public List<Row> getManyToManyRelationship(String relationshipName) {
		Object relationship = rowColumns.get(relationshipName);
		if (relationship == null) {
			return null;
		}
		
		if (!(relationship instanceof Collection<?>)) {
			throw new InvalidRelationshipException();
		}
		Collection<?> relationshipColumns = (Collection<?>) relationship;
		
		List<Row> rows = new ArrayList<Row>();
		
		for (Object item : relationshipColumns) {
			if (item != null) {
				Map<String, Object> rowColumns = this.extractRowColumns(item);
				rows.add(new Row(rowColumns));
			}
		}
		
		return rows;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> extractRowColumns(Object relationship) {
		if (!(relationship instanceof Map<?,?>)) {
			throw new InvalidRelationshipException();
		}
		return (Map<String, Object>) relationship;
	}

	@Override
	public String toString() {
		return "Row [columns=" + rowColumns + "]";
	}

}