package com.appglu.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

public abstract class AbstractAppgluApiTest {
	
	protected AppgluTemplate appgluTemplate;
	protected MockRestServiceServer mockServer;
	protected HttpHeaders responseHeaders;

	@Before
	public void setup() {
		appgluTemplate = createAppgluTemplate();
		mockServer = MockRestServiceServer.createServer(appgluTemplate.getRestTemplate());
		responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	protected AppgluTemplate createAppgluTemplate() {
		return new AppgluTemplate("applicationKey", "applicationSecret");
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
