package com.appglu;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an event that happened during the usage of the app. An event could be, for example, the tap of a button or the appearance of a dialog, etc.<br>
 * An event can contain parameters and is used to collect mobile app usage statistics.
 * 
 * @see AnalyticsSession
 * @since 1.0.0
 */
public class AnalyticsSessionEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private Date date;

	private Map<String, String> parameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	
	public void addParameter(String name, String value) {
		if (name != null) {
			this.getParameters().put(name, value);
		}
	}

	@Override
	public String toString() {
		return "AnalyticsSessionEvent [name=" + name + ", date=" + date + ", parameters=" + parameters + "]";
	}

}
