package com.appglu;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AnalyticsSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date startDate;
	
	private Date endDate;
	
	private String clientUUID;
	
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
	
	public String getClientUUID() {
		return clientUUID;
	}

	public void setClientUUID(String clientUUID) {
		this.clientUUID = clientUUID;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public List<AnalyticsSessionEvent> getEvents() {
		return events;
	}

	public void setEvents(List<AnalyticsSessionEvent> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return "AnalyticsSession [clientUUID=" + clientUUID + ", startDate="
				+ startDate + ", endDate=" + endDate + ", parameters="
				+ parameters + "]";
	}
	
}