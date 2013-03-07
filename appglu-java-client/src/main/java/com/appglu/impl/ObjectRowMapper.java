package com.appglu.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.appglu.Column;
import com.appglu.Ignore;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.RowMapperTypeConversionException;
import com.appglu.impl.util.StringUtils;

public class ObjectRowMapper<T> implements RowMapper<T> {

	private Class<T> mappedClass;
	
	public ObjectRowMapper(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}

	public T mapRow(final Row row) {
		final T object = this.instantiate();
		
		FieldCallback fieldCallback = new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				try {
					doWithField(row, object, field);
				} catch (RowMapperTypeConversionException e) {
					throw new ObjectRowMapperException(mappedClass, e);
				}
			}
		};
		
		FieldFilter fieldFilter = new FieldFilter() {
			public boolean matches(Field field) {
				if (!mappedClass.equals(field.getDeclaringClass())) {
					return false;
				}
				
				if (field.isAnnotationPresent(Ignore.class)) {
					return false;
				}
				
				return ReflectionUtils.COPYABLE_FIELDS.matches(field);
			}
		};
		
		ReflectionUtils.doWithFields(this.mappedClass, fieldCallback, fieldFilter);
		return object;
	}
	
	protected T instantiate() throws IllegalStateException {
		if (mappedClass.isInterface()) {
			throw new ObjectRowMapperException(mappedClass, "Specified class is an interface");
		}
		try {
			return mappedClass.newInstance();
		} catch (InstantiationException ex) {
			throw new ObjectRowMapperException(mappedClass, "Is it an abstract class?", ex);
		} catch (IllegalAccessException ex) {
			throw new ObjectRowMapperException(mappedClass, "Is there an empty and accessible constructor?", ex);
		}
	}
	
	protected void doWithField(Row row, T object, Field field) {
		String key = StringUtils.underscoreName(field.getName());
		boolean required = false;
		
		if (field.isAnnotationPresent(Column.class)) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (StringUtils.isNotEmpty(columnAnnotation.columnName())) {
				key = columnAnnotation.columnName();
			}
			required = columnAnnotation.required();
		}
		
		if (required && !row.containsKey(key)) {
			throw new ObjectRowMapperException(mappedClass, "Column " + key + " is required but not present on the response");
		}
		
		Class<?> type = field.getType();
		Object value = null;
		
		if (type.equals(boolean.class) || type.equals(Boolean.class)) {
			value = row.getBoolean(key);
		} else if (type.equals(short.class) || type.equals(Short.class)) {
			value = row.getShort(key);
		} else if (type.equals(byte.class) || type.equals(Byte.class)) {
			value = row.getByte(key);
		} else if (type.equals(float.class) || type.equals(Float.class)) {
			value = row.getFloat(key);
		} else if (type.equals(double.class) || type.equals(Double.class)) {
			value = row.getDouble(key);
		} else if (type.equals(int.class) || type.equals(Integer.class)) {
			value = row.getInt(key);
		} else if (type.equals(long.class) || type.equals(Long.class)) {
			value = row.getLong(key);
		} else if (type.equals(String.class)) {
			value = row.getString(key);
		} else if (type.equals(BigInteger.class)) {
			value = row.getBigInteger(key);
		} else if (type.equals(BigDecimal.class)) {
			value = row.getBigDecimal(key);
		} else if (type.equals(byte[].class)) {
			value = row.getByteArray(key);
		} else if (type.equals(Date.class)) {
			value = row.getDate(key);
		} else {
			throw new ObjectRowMapperException(mappedClass, "Not supported field of " + type + ". To ignore it, add the annotation @Ignore.");
		}
		
		if (value != null) {
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, object, value);
		}
	}
	
}