package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Row;

public abstract class RowsMixin {
	
	@JsonProperty("rows")
	List<Row> rows;

	@JsonProperty("totalRows")
	Integer totalRows;

}
