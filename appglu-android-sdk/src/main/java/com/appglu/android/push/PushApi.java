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

import android.content.Context;


/**
 * {@code PushApi} is used to register / unregister the device making it eligible to receive push notifications.<br>
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.PushOperations
 * @see com.appglu.AsyncPushOperations
 * @since 1.0.0
 */
public final class PushApi {
	
	private PushService pushService;
	
	public PushApi(PushService pushService) {
		this.pushService = pushService;
	}

	/**
	 * Register this device with AppGlu to make it eligible to receive push notifications.
	 * @param context Activity or Application object
	 * @param gcmSenderId this is your Google Cloud Message project ID. More information available in GCM documentation: {@link http://developer.android.com/google/gcm/gs.html}.
	 */
	public void registerForPushNotifications(Context context, String gcmSenderId) {
		this.registerForPushNotifications(context, gcmSenderId, false);
	}
	
	/**
	 * Register this device with AppGlu to make it eligible to receive push notifications.
	 * @param context Activity or Application object
	 * @param gcmSenderId this is your Google Cloud Message project ID. More information available in GCM documentation: {@link http://developer.android.com/google/gcm/gs.html}.
	 * @param checkManifest if <code>true</code> then GCMRegistrar.checkManifest(context) will be executed
	 */
	public void registerForPushNotifications(Context context, String gcmSenderId, boolean checkManifest) {
		pushService.registerForPushNotifications(context, gcmSenderId, checkManifest);
	}
	
	/**
	 * Remove this device from AppGlu and it will not receive push notifications again.
	 * @param context Activity or Application object
	 */
	public void unregister(Context context) {
		pushService.unregister(context);
	}
	
	protected void registerOnAppGluServer(Context context, String registrationId) {
		pushService.registerOnAppGluServer(context, registrationId);
	}
	
	protected void unregisterFromAppGluServer(Context context, String registrationId) {
		pushService.unregisterFromAppGluServer(context, registrationId);
	}

}
