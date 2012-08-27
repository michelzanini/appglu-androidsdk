package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.appglu.QueryParams;

public abstract class QueryParamsBodyMixin {
	
	public QueryParamsBodyMixin(@JsonProperty("params") @JsonSerialize(include=Inclusion.NON_EMPTY) QueryParams params) {
		
	}
	
}