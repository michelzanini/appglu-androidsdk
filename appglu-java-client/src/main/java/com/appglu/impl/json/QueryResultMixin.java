package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.appglu.Tuple;

@JsonPropertyOrder({"rows", "rowsAffected"})
public abstract class QueryResultMixin {
	
	@JsonProperty("rows")
	List<Tuple> tuples;
	
	@JsonProperty("rowsAffected")
	Integer rowsAffected;

}