package com.appglu.impl;

import org.springframework.web.client.RestOperations;

import com.appglu.AppgluNotFoundException;
import com.appglu.Device;
import com.appglu.PushOperations;
import com.appglu.impl.json.DeviceBody;

public class PushTemplate implements PushOperations {
	
	static final String DEVICE_REGISTRATION_URL = "/v1/push/device";
	
	static final String DEVICE_TOKEN_URL = "/v1/push/device/{token}";
	
	private RestOperations restOperations;
	
	public PushTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	public void registerDevice(Device device) {
		this.restOperations.put(DEVICE_REGISTRATION_URL, new DeviceBody(device));
	}

	public Device readDevice(String token) {
		try {
			DeviceBody deviceBody = this.restOperations.getForObject(DEVICE_TOKEN_URL, DeviceBody.class, token);
			return deviceBody.getDevice();
		} catch (AppgluNotFoundException e) {
			return null;
		}
	}

	public boolean removeDevice(String token) {
		try {
			this.restOperations.delete(DEVICE_TOKEN_URL, token);
			return true;
		} catch (AppgluNotFoundException e) {
			return false;
		}
	}

}