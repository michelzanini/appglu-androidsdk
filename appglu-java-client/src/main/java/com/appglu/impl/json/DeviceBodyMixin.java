package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Device;

public abstract class DeviceBodyMixin {
	
	public DeviceBodyMixin(@JsonProperty("device") Device device) {
		
	}

}
