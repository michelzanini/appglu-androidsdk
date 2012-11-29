package com.appglu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date startDate;
	
	private Date endDate;
	
	private Map<String, String> parameters;
	
	private List<AnalyticsSessionEvent> events;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Map<String, String> getParameters() {
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public List<AnalyticsSessionEvent> getEvents() {
		if (events == null) {
			events = new ArrayList<AnalyticsSessionEvent>();
		}
		return events;
	}

	public void setEvents(List<AnalyticsSessionEvent> events) {
		this.events = events;
	}
	
	public void addParameter(String name, String value) {
		if (name != null) {
			this.getParameters().put(name, value);
		}
	}
	
	public void addEvent(AnalyticsSessionEvent event) {
		this.getEvents().add(event);
	}

	@Override
	public String toString() {
		return "AnalyticsSession [startDate="
				+ startDate + ", endDate=" + endDate + ", parameters="
				+ parameters + "]";
	}
	
}