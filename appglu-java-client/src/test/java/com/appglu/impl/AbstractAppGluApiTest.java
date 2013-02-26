package com.appglu.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.TimeZone;

import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

public abstract class AbstractAppGluApiTest {
	
	protected AppGluTemplate appGluTemplate;
	
	protected MockRestServiceServer mockServer;
	
	protected HttpHeaders responseHeaders;

	protected MediaType jsonMediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
	
	@Before
	public void setup() {
		/* Setting time zone to GMT+0 avoiding time zone issues in tests */
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		TimeZone.setDefault(gmt);
		
		appGluTemplate = createAppGluTemplate();
		
		mockServer = MockRestServiceServer.createServer(appGluTemplate.getRestTemplate());
		
		responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(jsonMediaType);
	}

	protected AppGluTemplate createAppGluTemplate() {
		AppGluTemplate appgluTemplate = new AppGluTemplate("http://localhost/appglu", "applicationKey", "applicationSecret");
		appgluTemplate.setDefaultHeaders(this.createDefaultHttpHeaders());
		return appgluTemplate;
	}

	protected HttpHeaders createDefaultHttpHeaders() {
		return new HttpHeaders();
	}

	protected Resource jsonResource(String filename) {
		return new ClassPathResource(filename + ".json", getClass());
	}

	protected String compactedJson(String jsonFilename) {
		return readCompactedJsonResource(jsonResource(jsonFilename));
	}
	
	protected String readCompactedJsonResource(Resource resource) {
		StringBuilder resourceText = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			while (reader.ready()) {
				resourceText.append(reader.readLine().trim().replace("\n", ""));
			}
		} catch (IOException e) {
		}		
		return resourceText.toString();
	}

}
