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
 * Represents the operating system of the device. Can be {@link #IOS} or {@link #ANDROID}.
 * 
 * @since 1.0.0
 */
public enum DevicePlatform {
	
	IOS,
	ANDROID;
	
	public static DevicePlatform getDevicePlatform(String type) {
		for (DevicePlatform platform : values()) {
			if (platform.toString().equalsIgnoreCase(type)) {
				return platform;
			}
		}
		return null;
	}
	
	public boolean isIos() {
		return this == IOS;
	}
	
	public boolean isAndroid() {
		return this == ANDROID;
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
