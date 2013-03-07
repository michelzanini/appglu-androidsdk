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
package com.appglu.impl.json.jackson;

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
