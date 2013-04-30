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

import com.appglu.AsyncCallback;
import com.appglu.AsyncPushOperations;
import com.appglu.Device;
import com.appglu.DevicePlatform;
import com.appglu.android.DeviceInstallation;
import com.appglu.impl.util.StringUtils;
import com.google.android.gcm.GCMRegistrar;

public class PushService {
	
	private AsyncPushOperations asyncPushOperations;
	
	private DeviceInstallation deviceInstallation;

	public PushService(AsyncPushOperations asyncPushOperations, DeviceInstallation deviceInstallation) {
		this.asyncPushOperations = asyncPushOperations;
		this.deviceInstallation = deviceInstallation;
	}
	
	public void registerForPushNotifications(Context context, String gcmSenderId, boolean checkManifest) {
		GCMRegistrar.checkDevice(context);
		
		if (checkManifest) {
        	GCMRegistrar.checkManifest(context);
		}
		
        boolean isRegistered = GCMRegistrar.isRegistered(context);
        boolean isRegisteredOnServer = GCMRegistrar.isRegisteredOnServer(context);
        
		if (!isRegistered) {
        	GCMRegistrar.register(context, gcmSenderId);
        }
        
		if (isRegistered && !isRegisteredOnServer) {
			String registrationId = GCMRegistrar.getRegistrationId(context);
        	this.registerOnAppGluServer(context, registrationId);
        }
	}
	
	public void unregister(Context context) {
		GCMRegistrar.unregister(context);
	}
	
	protected void registerOnAppGluServer(final Context context, String registrationId) {
		if (StringUtils.isEmpty(registrationId)) {
			return;
		}
		this.registerDeviceInBackground(registrationId, new AsyncCallback<Void>() {
			public void onResult(Void result) {
				
			}
			
			public void onNoInternetConnection() {
				this.onFinish(false);
			}
			
			public void onFinish(boolean wasSuccessful) {
				GCMRegistrar.setRegisteredOnServer(context, wasSuccessful);
				if (!wasSuccessful) {
					GCMRegistrar.unregister(context);
				}
			}
		});
	}
	
	protected void unregisterFromAppGluServer(final Context context, String registrationId) {
		if (StringUtils.isEmpty(registrationId)) {
			return;
		}
		this.removeDeviceInBackground(registrationId, new AsyncCallback<Boolean>() {
			public void onResult(Boolean result) {
				
			}
			
			public void onNoInternetConnection() {
				this.onFinish(false);
			}
			
			public void onFinish(boolean wasSuccessful) {
				GCMRegistrar.setRegisteredOnServer(context, false);
			}
		});
	}
	
	private void registerDeviceInBackground(String token, AsyncCallback<Void> registerCallback) {
		this.registerDeviceInBackground(token, null, registerCallback);
	}
	
	private void registerDeviceInBackground(String token, String alias, AsyncCallback<Void> registerCallback) {
		Device device = this.createDevice(token, alias);
		asyncPushOperations.registerDeviceInBackground(device, registerCallback);
	}

	private void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback) {
		asyncPushOperations.removeDeviceInBackground(token, removeCallback);
	}
	
	private Device createDevice(String token, String alias) {
		Device device = new Device();
		device.setToken(token);
		device.setAlias(alias);
		device.setPlatform(DevicePlatform.ANDROID);
		device.setAppIdentifier(deviceInstallation.getAppIdentifier());
		return device;
	}

}
