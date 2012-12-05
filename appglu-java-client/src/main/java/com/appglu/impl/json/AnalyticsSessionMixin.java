package com.appglu.impl.json;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.appglu.AnalyticsSessionEvent;

@JsonPropertyOrder({"startDate", "endDate", "parameters", "events"})
public abstract class AnalyticsSessionMixin {
	
	@JsonProperty("startDate")
	Date startDate;
	
	@JsonProperty("endDate")
	Date endDate;
	
	@JsonProperty("parameters")
	@JsonSerialize(include=Inclusion.NON_EMPTY)
	Map<String, String> parameters;
	
	@JsonProperty("events")
	@JsonSerialize(include=Inclusion.NON_EMPTY)
	List<AnalyticsSessionEvent> events;

}