package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.body;
import static org.springframework.test.web.client.RequestMatchers.header;
import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.Device;
import com.appglu.DevicePlatform;
import com.appglu.PushOperations;

@SuppressWarnings("deprecation")
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
		return device;
	}
	
	private void assertDevice(Device device) {
		Assert.assertEquals("f3f71c5a-0a98-48f7-9acd-d38d714d76ad", device.getToken());
		Assert.assertEquals("alias", device.getAlias());
		Assert.assertEquals(DevicePlatform.ANDROID, device.getPlatform());
	}
	
	@Test
	public void registerDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/push_device")))
			.andRespond(withResponse("", responseHeaders));
		
		pushOperations.registerDevice(device());
		
		mockServer.verify();
	}
	
	@Test
	public void readDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withResponse(compactedJson("data/push_device"), responseHeaders));
		
		Device device = pushOperations.readDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		this.assertDevice(device);
		
		mockServer.verify();
	}
	
	@Test
	public void readDeviceNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withResponse(compactedJson("data/error_not_found"), responseHeaders, HttpStatus.NOT_FOUND, ""));
		
		Device device = pushOperations.readDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertNull(device);
		
		mockServer.verify();
	}
	
	@Test
	public void removeDevice() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withResponse("", responseHeaders));
		
		boolean success = pushOperations.removeDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertTrue(success);
		
		mockServer.verify();
	}
	
	@Test
	public void removeDeviceNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/push/device/f3f71c5a-0a98-48f7-9acd-d38d714d76ad"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withResponse(compactedJson("data/error_not_found"), responseHeaders, HttpStatus.NOT_FOUND, ""));
		
		boolean success = pushOperations.removeDevice("f3f71c5a-0a98-48f7-9acd-d38d714d76ad");
		Assert.assertFalse(success);
		
		mockServer.verify();
	}

}
