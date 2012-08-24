package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Tuple;

public abstract class QueryResultMixin {
	
	@JsonProperty("rows")
	List<Tuple> tuples;
	
	@JsonProperty("rowsAffected")
	Integer rowsAffected;

}