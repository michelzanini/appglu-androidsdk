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
package com.appglu.impl;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluHttpNotFoundException;
import com.appglu.AppGluRestClientException;
import com.appglu.Device;
import com.appglu.PushOperations;
import com.appglu.impl.json.DeviceBody;

public final class PushTemplate implements PushOperations {
	
	static final String DEVICE_REGISTRATION_URL = "/v1/push/device";
	
	static final String DEVICE_TOKEN_URL = "/v1/push/device/{token}";
	
	private RestOperations restOperations;
	
	public PushTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerDevice(Device device) throws AppGluRestClientException {
		try {
			this.restOperations.put(DEVICE_REGISTRATION_URL, new DeviceBody(device));
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Device readDevice(String token) throws AppGluRestClientException {
		try {
			DeviceBody deviceBody = this.restOperations.getForObject(DEVICE_TOKEN_URL, DeviceBody.class, token);
			return deviceBody.getDevice();
		} catch (AppGluHttpNotFoundException e) {
			return null;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeDevice(String token) throws AppGluRestClientException {
		try {
			this.restOperations.delete(DEVICE_TOKEN_URL, token);
			return true;
		} catch (AppGluHttpNotFoundException e) {
			return false;
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

}
