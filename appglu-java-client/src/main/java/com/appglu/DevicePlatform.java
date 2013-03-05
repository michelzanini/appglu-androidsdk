package com.appglu;

/**
 * Represents the operating system of the device. Can be {@link IOS} or {@link ANDROID}.
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