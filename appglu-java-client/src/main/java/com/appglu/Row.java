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

import com.appglu.impl.util.Base64Utils;
import com.appglu.impl.util.DateUtils;
import com.appglu.impl.util.StringUtils;

/**
 * <p>Represents a row in the database. It extends <code>java.util.HashMap</code> where the keys are the column names and the values are objects.<br>
 * <p>Casting methods are provided to convert these objects to the type you would except.<br>
 * You can also associate a {@code Row} object with another {@code Row} object representing a relationship between two tables.<br>
 * 
 * @since 1.0.0
 */
public class Row extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;

	public Boolean getBoolean(String columnName) {
		try {
			return (Boolean) this.get(columnName);
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
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
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}

	public Integer getInt(String columnName) {
		try {
			return (Integer) this.get(columnName);
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}

	public Long getLong(String columnName) {
		try {
			Object number = this.get(columnName);
			if (number instanceof Integer) {
				Integer integer = (Integer) number;
				return integer.longValue();
			}
			return (Long) number;
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}

	public String getString(String columnName) {
		try {
			return (String) this.get(columnName);
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}
	
	public BigInteger getBigInteger(String columnName) {
		try {
			Object number = this.get(columnName);
			String numberAsString = String.valueOf(number);
			return new BigInteger(numberAsString);
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}

	public BigDecimal getBigDecimal(String columnName) {
		try {
			return (BigDecimal) this.get(columnName);
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}
	
	public byte[] getByteArray(String columnName) {
		String string = this.getString(columnName);
		if (string == null) {
			return null;
		}
		if (StringUtils.isEmpty(string)) {
			return new byte[0];
		}
		try {
			return Base64Utils.decode(string);
		} catch (IOException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}

	public Date getDate(String columnName) {
		String string = this.getString(columnName);
		if (StringUtils.isEmpty(string)) {
			return null;
		}
		try {
			return DateUtils.parseDate(string);
		} catch (ParseException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}
	
	public Row getRow(String relationshipName) {
		Object relationship = this.get(relationshipName);
		if (relationship == null) {
			return null;
		}
		
		Row row = new Row();
		row.putAll(this.getMap(relationship));
		return row;
	}
	
	public List<Row> getRows(String relationshipName) {
		Collection<Object> relationshipColumns = this.getCollection(relationshipName);
		if (relationshipColumns == null) {
			return null;
		}
		
		List<Row> rows = new ArrayList<Row>();
		
		for (Object item : relationshipColumns) {
			if (item != null) {
				Row row = new Row();
				row.putAll(this.getMap(item));
				rows.add(row);
			}
		}
		
		return rows;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Object> getCollection(String relationshipName) {
		Object relationship = this.get(relationshipName);
		if (relationship == null) {
			return null;
		}
		try {
			return (Collection<Object>) relationship;
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getMap(Object relationship) {
		try {
			return (Map<String, Object>) relationship;
		} catch (ClassCastException e) {
			throw new RowMapperTypeConversionException(e.getMessage(), e);
		}
	}
	
	public void putNull(String key) {
		this.put(key, (Object) null);
	}
	
	public void put(String key, Row row) {
		this.put(key, (Map<String, Object>) row);
	}
	
	public void put(String key, List<Row> rows) {
		List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
		for (Row row : rows) {
			entries.add(row);	
		}
		this.put(key, entries);
	}
	
	public void put(String key, Date date) {
		this.putDatetime(key, date);
	}
	
	public void putDatetime(String key, Date date) {
		String value = DateUtils.formatDatetime(date);
		this.put(key, value);
	}
	
	public void putDate(String key, Date date) {
		String value = DateUtils.formatDate(date);
		this.put(key, value);
	}

	public void putTime(String key, Date date) {
		String value = DateUtils.formatTime(date);
		this.put(key, value);
	}

}