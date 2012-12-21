package com.appglu.impl.json.jackson;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonPropertyOrder({"name", "date", "parameters"})
public abstract class AnalyticsSessionEventMixin {
	
	@JsonProperty("name")
	String name;
	
	@JsonProperty("date")
	Date date;

	@JsonProperty("parameters")
	@JsonSerialize(include=Inclusion.NON_EMPTY)
	Map<String, String> parameters;

}
