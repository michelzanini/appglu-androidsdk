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

/**
 * {@code AsyncPushOperations} has all methods that {@link PushOperations} has but they execute <strong>asynchronously</strong>. 
 * @see PushOperations
 * @since 1.0.0
 */
public interface AsyncPushOperations {
	
	/**
	 * Asynchronous version of {@link PushOperations#registerDevice(Device)}
	 * @see PushOperations#registerDevice(Device)
	 */
	void registerDeviceInBackground(Device device, AsyncCallback<Void> registerCallback);
	
	/**
	 * Asynchronous version of {@link PushOperations#readDevice(String)}
	 * @see PushOperations#readDevice(String)
	 */
	void readDeviceInBackground(String token, AsyncCallback<Device> readCallback);
	
	/**
	 * Asynchronous version of {@link PushOperations#removeDevice(String)}
	 * @see PushOperations#removeDevice(String)
	 */
	void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback);

}
