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

import android.content.Intent;
import android.os.Bundle;

/**
 * Parses the XTify's Intent creating a PushNotification from that.
 * @since 1.0.0
 */
public class XtifyGCMIntentParser implements GCMIntentParser {

	private static final String XTIFY_PACKAGE_PREFIX = "com.xtify.sdk.";
	private static final String CONTENT_KEY = "com.xtify.sdk.NOTIFICATION_CONTENT";
	private static final String XTIFY_PARAM_PREFIX = "data.";

	@Override
	public PushNotification parseIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		
		String content = extras.getString(CONTENT_KEY);
		
		Map<String, String> parameters = new HashMap<String, String>();
		
		for (String key : extras.keySet()) {
			if (key.startsWith(XTIFY_PARAM_PREFIX)) {
				String originalKey = key.substring(XTIFY_PARAM_PREFIX.length(), key.length());
				parameters.put(originalKey, extras.getString(key));
			}
		}
		
		PushNotification pushNotification = new PushNotification();
		
		pushNotification.setContent(content);
		pushNotification.setParameters(parameters);
		
		return pushNotification;
	}

	@Override
	public boolean supportsIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		for (String key : extras.keySet()) {
			if (key.startsWith(XTIFY_PACKAGE_PREFIX)) {
				return true;
			}
		}
		return false;
	}

}