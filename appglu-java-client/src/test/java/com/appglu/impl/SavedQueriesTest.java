package com.appglu.impl;

import static org.springframework.test.web.client.match.RequestMatchers.content;
import static org.springframework.test.web.client.match.RequestMatchers.header;
import static org.springframework.test.web.client.match.RequestMatchers.method;
import static org.springframework.test.web.client.match.RequestMatchers.requestTo;
import static org.springframework.test.web.client.response.ResponseCreators.withSuccess;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.Row;
import com.appglu.SavedQueriesOperations;

public class SavedQueriesTest extends AbstractAppGluApiTest {
	
	private SavedQueriesOperations savedQueriesOperations;
	
	@Before
	public void setup() {
		super.setup();
		savedQueriesOperations = appGluTemplate.savedQueriesOperations();
	}
	
	private QueryParams queryParams() {
		QueryParams params = new QueryParams();
		
		params.put("first", true);
		params.put("second", 10);
		params.put("third", "string");
		params.put("forth", null);
		params.put("fifth", 1.5);
		
		return params;
	}
	
	private void assertRow(Row row) {
		Assert.assertEquals(true, row.get("first"));
		Assert.assertEquals(new Integer(10), row.get("second"));
		Assert.assertEquals("string", row.get("third"));
		Assert.assertEquals(null, row.get("forth"));
		Assert.assertEquals(new Double(1.5), row.get("fifth"));
	}
	
	@Test
	public void runQuery() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/saved_queries_params")))
			.andRespond(withSuccess().body(compactedJson("data/saved_queries_result")).headers(responseHeaders));
		
		QueryResult result = savedQueriesOperations.runQuery("queryName", queryParams());
		
		Assert.assertEquals(3, result.getRows().size());
		Assert.assertNull(result.getRowsAffected());
		
		assertRow(result.getRows().get(0));
		assertRow(result.getRows().get(1));
		assertRow(result.getRows().get(2));
		
		mockServer.verify();
	}

	@Test
	public void runUpdateQuery() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/saved_queries_params")))
			.andRespond(withSuccess().body(compactedJson("data/saved_queries_update_result")).headers(responseHeaders));
		
		QueryResult result = savedQueriesOperations.runQuery("queryName", queryParams());
		
		Assert.assertNull(result.getRows());
		Assert.assertEquals(new Integer(10), result.getRowsAffected());
		
		mockServer.verify();
	}
	
	@Test
	public void runQueryNullQueryParams() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/saved_queries_params_empty")))
			.andRespond(withSuccess().body(compactedJson("data/saved_queries_update_result")).headers(responseHeaders));
	
		savedQueriesOperations.runQuery("queryName");
		
		mockServer.verify();
	}
	
	@Test
	public void runQueryEmptyQueryParams() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/saved_queries_params_empty")))
			.andRespond(withSuccess().body(compactedJson("data/saved_queries_update_result")).headers(responseHeaders));

		savedQueriesOperations.runQuery("queryName", new QueryParams());
		
		mockServer.verify();
	}

}