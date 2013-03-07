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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.Device;
import com.appglu.DevicePlatform;
import com.appglu.PushOperations;

public class PushTemplateTest extends AbstractAppGluApiTest {
	
	private PushOperations pushOperations;
	
	@Before
	public void setup() {
		super.setup();
		pushOperations = appGluTemplate.pushOperations();
	}
	
	private Device device() {
		Device device = new Device();
		device.setToken("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		device.setAlias("alias");
		device.setPlatform(DevicePlatform.ANDROID);
		device.setAppIdentifier("com.appglu");
		return device;
	}
	
	private void assertDevice(Device device) {
		Assert.assertEquals("f3f71c5a-0a98-48f7-9acd-d38d714d76ad", device.getToken());
		Assert.assertEquals("alias", device.getAlias());
		Assert.assertEquals(DevicePlatform.ANDROID, device.getPlatform());
		Assert.assertEquals("com.appglu", device.getAppIdentifier());
	}
	
	@Test
	public void registerDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/push_device")))
			.andRespond(withSuccess().headers(responseHeaders));
		
		pushOperations.registerDevice(device());
		
		mockServer.verify();
	}
	
	@Test
	public void readDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/push_device")).headers(responseHeaders));
		
		Device device = pushOperations.readDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		this.assertDevice(device);
		
		mockServer.verify();
	}
	
	@Test
	public void readDeviceNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.NOT_FOUND).body(compactedJson("data/error_not_found")).headers(responseHeaders));
		
		Device device = pushOperations.readDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertNull(device);
		
		mockServer.verify();
	}
	
	@Test
	public void removeDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withSuccess().headers(responseHeaders));
		
		boolean success = pushOperations.removeDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertTrue(success);
		
		mockServer.verify();
	}
	
	@Test
	public void removeDeviceNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withStatus(HttpStatus.NOT_FOUND).body(compactedJson("data/error_not_found")).headers(responseHeaders));
		
		boolean success = pushOperations.removeDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertFalse(success);
		
		mockServer.verify();
	}

}
