package com.appglu.android.sync;

import java.util.Date;
import java.util.Map.Entry;

import android.content.ContentValues;

import com.appglu.DataTypeConversionException;
import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.RowMapperException;
import com.appglu.impl.util.StringUtils;

public class ContentValuesRowMapper implements RowMapper<ContentValues> {
	
	private TableColumns tableColumns;
	
	private ContentValues values = new ContentValues();
	
	public ContentValuesRowMapper(TableColumns tableColumns) {
		this.tableColumns = tableColumns;
	}
	
	public TableColumns getTableColumns() {
		return tableColumns;
	}
	
	public ContentValues mapRow(Row row) throws RowMapperException {
		values.clear();
		
		for (Entry<String, Object> rowProperty : row.entrySet()) {
			String columnName = rowProperty.getKey();
			String escapedColumnName = StringUtils.escapeColumn(columnName);
			
			Column column = tableColumns.get(columnName);
			
			if (column != null) {
				if (column.getType().contains("int")) {
					values.put(escapedColumnName, row.getInt(columnName));
					continue;
				}
				
				if (column.getType().contains("char") || column.getType().contains("clob") || column.getType().contains("text")) {
					values.put(escapedColumnName, row.getString(columnName));
					continue;
				}
				
				if (column.getType().contains("blob")) {
					values.put(escapedColumnName, row.getByteArray(columnName));
					continue;
				}
				
				if (column.getType().contains("real") || column.getType().contains("floa") || column.getType().contains("doub")) {
					values.put(escapedColumnName, row.getDouble(columnName));
					continue;
				}
				
				if (column.getType().equals("date") || column.getType().equals("datetime")) {
					try {
						Date date = row.getDate(columnName);
						if (date == null) {
							values.putNull(escapedColumnName);
						} else {
							long time = date.getTime();
							values.put(escapedColumnName, time);
						}
						continue;
					} catch (DataTypeConversionException e) {
						throw new RowMapperException(ContentValues.class, "Cannot convert " + columnName + " to date", e);
					}
				}
				
				if (column.getType().equals("boolean")) {
					values.put(escapedColumnName, row.getBoolean(columnName));
					continue;
				}
				
				if (column.getType().contains("numeric") || column.getType().contains("decimal")) {
					values.put(escapedColumnName, row.getLong(columnName));
					continue;
				}
				
				values.putNull(columnName);
			}
		}
		
		return values;
	}

}