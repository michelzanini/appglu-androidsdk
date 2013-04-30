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
package com.appglu.android.push;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a push notification received from Google Cloud Message service.
 * @since 1.0.0
 */
public class PushNotification {
	
	private String content;
	
	private Map<String, String> parameters = new HashMap<String, String>();

	public String getContent() {
		return content;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
