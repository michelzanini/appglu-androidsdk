package com.appglu;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.appglu.impl.util.Base64;
import com.appglu.impl.util.DateUtils;

@SuppressWarnings("serial")
public class Row extends HashMap<String, Object> implements Tuple {
	
	public Boolean getBoolean(String columnName) {
		try {
			return (Boolean) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public Short getShort(String columnName) {
		Integer value = this.getInt(columnName);
		if (value == null) {
			return null;
		}
		return value.shortValue();
	}
	
	public Byte getByte(String columnName) {
		Integer value = this.getInt(columnName);
		if (value == null) {
			return null;
		}
		return value.byteValue();
	}
	
	public Float getFloat(String columnName) {
		Double value = this.getDouble(columnName);
		if (value == null) {
			return null;
		}
		return value.floatValue();
	}
	
	public Double getDouble(String columnName) {
		try {
			return (Double) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public Integer getInt(String columnName) {
		try {
			return (Integer) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public Long getLong(String columnName) {
		try {
			return (Long) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public String getString(String columnName) {
		try {
			return (String) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}
	
	public BigInteger getBigInteger(String columnName) {
		try {
			return (BigInteger) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public BigDecimal getBigDecimal(String columnName) {
		try {
			return (BigDecimal) this.get(columnName);
		} catch (ClassCastException e) {
			throw new DataTypeConversionException(e);
		}
	}
	
	public byte[] getByteArray(String columnName) {
		String string = this.getString(columnName);
		if (string == null) {
			return null;
		}
		if (!StringUtils.hasText(string)) {
			return new byte[0];
		}
		try {
			return Base64.decode(string);
		} catch (IOException e) {
			throw new DataTypeConversionException(e);
		}
	}

	public Date getDate(String columnName) {
		String string = this.getString(columnName);
		if (!StringUtils.hasText(string)) {
			return null;
		}
		try {
			return DateUtils.parseDate(string);
		} catch (ParseException e) {
			throw new DataTypeConversionException(e);
		}
	}
	
	public void addManyToOneRelationship(String relationshipName, Row row) {
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