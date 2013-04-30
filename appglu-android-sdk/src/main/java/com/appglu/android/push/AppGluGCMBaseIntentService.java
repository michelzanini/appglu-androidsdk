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
import android.content.Intent;

import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * Extends Google's <code>GCMBaseIntentService</code> to provide automatic register/unregister of device tokens with AppGlu.
 * @since 1.0.0
 */
public abstract class AppGluGCMBaseIntentService extends GCMBaseIntentService {
	
	protected Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	@Override
	protected final void onMessage(Context context, Intent intent) {
		logger.debug("New push notification received");
		
		if (!GCMRegistrar.isRegistered(context)) {
			return;
		}
		
		GCMIntentParser xtifyIntentParser = new XtifyGCMIntentParser();
		
		PushNotification pushNotification = null;
		
		if (xtifyIntentParser.supportsIntent(intent)) {
			pushNotification = xtifyIntentParser.parseIntent(intent);
		}
		
		if (pushNotification != null) {
			this.onNotificationReceived(context, pushNotification);
		} else {
			this.onNotificationError(context, intent);
		}
	}
	
	/**
     * Called when a cloud message has been received.
     *
	 * @param context application's context.
	 * @param pushNotification {@link PushNotification} object containing message's body and parameters.
	 */
	protected abstract void onNotificationReceived(Context context, PushNotification pushNotification);
	
	/**
	 * Called when a cloud message has been received and it's Intent could not be parsed to a {@link PushNotification} object.
	 * 
	 * @param context application's context.
	 * @param intent the original intent received.
	 */
	protected void onNotificationError(Context context, Intent intent) {
		logger.error("Error while processing the push notification intent");
	}
	
	/**
	 * Called when the device token was not able to be registered.
	 * 
	 * @param context application's context.
	 * @param errorId GCM error id
	 */
	protected void onRegistrationError(Context context, String errorId) {
		logger.error("Error while registering or unregistering the device with GCM. ErrorId: " + errorId);
	}
	
	@Override
	protected final void onError(Context context, String errorId) {
		this.onRegistrationError(context, errorId);
	}

	@Override
	protected final void onRegistered(Context context, String registrationId) {
		AppGlu.pushApi().registerOnAppGluServer(context, registrationId);
	}

	@Override
	protected final void onUnregistered(Context context, String registrationId) {
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			AppGlu.pushApi().unregisterFromAppGluServer(context, registrationId);
		}
	}

}