package com.appglu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Row extends HashMap<String, Object> implements Tuple {
	
	public Boolean getBoolean(String columnName) {
		return null;
	}

	public Short getShort(String columnName) {
		return null;
	}
	
	public Byte getByte(String columnName) {
		return null;
	}
	
	public byte[] getByteArray(String columnName) {
		return null;
	}

	public Date getDate(String columnName) {
		return null;
	}
	
	public Float getFloat(String columnName) {
		return null;
	}
	
	public Double getDouble(String columnName) {
		return null;
	}

	public Integer getInt(String columnName) {
		return null;
	}

	public Long getLong(String columnName) {
		return null;
	}

	public Number getNumber(String columnName) {
		return null;
	}

	public String getString(String columnName) {
		return null;
	}

	public BigDecimal getBigDecimal(String columnName) {
		return null;
	}
	
	public void addManyToOneRelationship(String relationshipName, Tuple row) {
		this.put(relationshipName, row);
	}
	
	public void addManyToManyRelationship(String relationshipName, List<Row> rows) {
		List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
		for (Row row : rows) {
			entries.add(row);	
		}
		this.put(relationshipName, entries);
	}
	
	public Row getManyToOneRelationship(String relationshipName) {
		Object relationship = this.get(relationshipName);
		if (relationship == null) {
			return null;
		}
		
		Row row = new Row();
		row.putAll(this.extractRowColumns(relationship));
		return row;
	}
	
	public List<Row> getManyToManyRelationship(String relationshipName) {
		Object relationship = this.get(relationshipName);
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
				Row row = new Row();
				row.putAll(this.extractRowColumns(item));
				rows.add(row);
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

}
