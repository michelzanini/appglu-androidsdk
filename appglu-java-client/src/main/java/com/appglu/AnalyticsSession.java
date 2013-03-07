/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a session of application usage. From the time the user has opened the app to the time it closes it.<br>
 * A session can contain parameters and events and is used to collect mobile app usage statistics.
 * 
 * @see AnalyticsSessionEvent
 * @since 1.0.0
 */
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
