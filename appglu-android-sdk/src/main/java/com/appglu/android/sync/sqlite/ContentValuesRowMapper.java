package com.appglu.android.sync.sqlite;

import java.util.Map.Entry;

import android.content.ContentValues;

import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.RowMapperException;

public class ContentValuesRowMapper implements RowMapper<ContentValues> {
	
	private TableColumns tableColumns;
	
	public ContentValuesRowMapper(TableColumns tableColumns) {
		this.tableColumns = tableColumns;
	}

	public ContentValues mapRow(Row row) throws RowMapperException {
		ContentValues values = new ContentValues();
		
		for (Entry<String, Object> rowProperty : row.entrySet()) {
			String columnName = rowProperty.getKey();
			
			Column column = tableColumns.get(columnName);
			
			if (column != null) {
				if (column.getType().contains("int")) {
					values.put(columnName, row.getInt(columnName));
					continue;
				}
				
				if (column.getType().contains("char") || column.getType().contains("clob") || column.getType().contains("text")) {
					values.put(columnName, row.getString(columnName));
					continue;
				}
				
				if (column.getType().contains("blob")) {
					values.put(columnName, row.getByteArray(columnName));
					continue;
				}
				
				if (column.getType().contains("real") || column.getType().contains("floa") || column.getType().contains("doub")) {
					values.put(columnName, row.getDouble(columnName));
					continue;
				}
			}
		}
		
		return values;
	}

}
