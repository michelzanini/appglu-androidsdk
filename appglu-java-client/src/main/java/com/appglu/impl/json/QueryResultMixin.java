package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.appglu.Tuple;

public abstract class QueryResultMixin {
	
	@JsonProperty("rows")
	@JsonSerialize(include=Inclusion.NON_NULL)
	List<Tuple> tuples;
	
	@JsonProperty("rowsAffected")
	@JsonSerialize(include=Inclusion.NON_NULL)
	Integer rowsAffected;

}
