package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.body;
import static org.springframework.test.web.client.RequestMatchers.header;
import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withResponse;
import junit.framework.Assert;

import org.junit.Before;
import org.springframework.http.HttpMethod;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;

@SuppressWarnings("deprecation")
public class SavedQueriesTest extends AbstractAppgluApiTest {
	
	private SavedQueriesOperations savedQueriesOperations;
	
	@Before
	public void setup() {
		super.setup();
		savedQueriesOperations = appgluTemplate.savedQueriesOperations();
	}
	
	//@Test
	public void executeQuery() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/crud_row_relationships")))
			.andRespond(withResponse(compactedJson("data/crud_create_response"), responseHeaders));
		
		QueryParams params = new QueryParams();
		
		QueryResult result = savedQueriesOperations.executeQuery("queryName", params);
		
		Assert.assertNotNull(result);
		
		mockServer.verify();
	}

}
