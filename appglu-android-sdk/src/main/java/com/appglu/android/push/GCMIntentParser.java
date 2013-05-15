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

import android.content.Intent;

/**
 * Notifications are received as an Intent. Implementations of this interface are able to convert Intent's to a more friendly {@link PushNotification} object.
 * @since 1.0.0
 */
public interface GCMIntentParser {
	
	/**
	 * Will return <code>true</code> if this implementation is able to parse the intent.
	 */
	public boolean supportsIntent(Intent intent);
	
	/**
	 * Parses the Intent and returns a {@link PushNotification} object.
	 */
	public PushNotification parseIntent(Intent intent);

}