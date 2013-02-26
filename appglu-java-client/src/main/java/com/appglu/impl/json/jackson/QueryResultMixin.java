package com.appglu.impl.json.jackson;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.appglu.Row;

@JsonPropertyOrder({"rows", "rowsAffected"})
public abstract class QueryResultMixin {
	
	@JsonProperty("rows")
	List<Row> rows;
	
	@JsonProperty("rowsAffected")
	Integer rowsAffected;

}