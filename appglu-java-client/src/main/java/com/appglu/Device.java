package com.appglu;

import java.io.Serializable;

/**
 * Represents a unique device installation for the purpose of receiving push notifications.
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