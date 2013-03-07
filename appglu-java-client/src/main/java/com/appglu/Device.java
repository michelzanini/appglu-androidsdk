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

/**
 * Represents a unique device installation for the purpose of receiving push notifications.
 * @since 1.0.0
 */
public class Device implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String token;
	
	private String alias;
	
	private DevicePlatform platform;
	
	private String appIdentifier;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public DevicePlatform getPlatform() {
		return platform;
	}

	public void setPlatform(DevicePlatform platform) {
		this.platform = platform;
	}
	
	public String getAppIdentifier() {
		return appIdentifier;
	}

	public void setAppIdentifier(String appIdentifier) {
		this.appIdentifier = appIdentifier;
	}
	
	public boolean isIos() {
		return DevicePlatform.IOS.equals(platform);
	}
	
	public boolean isAndroid() {
		return DevicePlatform.ANDROID.equals(platform);
	}

	@Override
	public String toString() {
		return "Device [" + token + ", " + platform + ", " + appIdentifier + "]";
	}

	@Override
	public int hashCode() {
		return (token == null) ? 0 : token.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
}
