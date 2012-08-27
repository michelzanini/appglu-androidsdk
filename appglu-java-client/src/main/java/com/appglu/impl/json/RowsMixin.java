package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.appglu.Row;

@JsonPropertyOrder({"rows", "totalRows"})
public abstract class RowsMixin {
	
	@JsonProperty("rows")
	List<Row> rows;

	@JsonProperty("totalRows")
	Integer totalRows;

}
