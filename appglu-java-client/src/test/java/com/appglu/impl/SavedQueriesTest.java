package com.appglu.impl;

import static org.springframework.test.web.client.RequestMatchers.body;
import static org.springframework.test.web.client.RequestMatchers.header;
import static org.springframework.test.web.client.RequestMatchers.method;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withResponse;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;
import com.appglu.Tuple;

@SuppressWarnings("deprecation")
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
	
	private void assertTuple(Tuple tuple) {
		Assert.assertEquals(true, tuple.get("first"));
		Assert.assertEquals(new Integer(10), tuple.get("second"));
		Assert.assertEquals("string", tuple.get("third"));
		Assert.assertEquals(null, tuple.get("forth"));
		Assert.assertEquals(new Double(1.5), tuple.get("fifth"));
	}
	
	@Test
	public void executeQuery() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/saved_queries_params")))
			.andRespond(withResponse(compactedJson("data/saved_queries_result"), responseHeaders));
		
		QueryResult result = savedQueriesOperations.executeQuery("queryName", queryParams());
		
		Assert.assertEquals(3, result.getTuples().size());
		Assert.assertNull(result.getRowsAffected());
		
		assertTuple(result.getTuples().get(0));
		assertTuple(result.getTuples().get(1));
		assertTuple(result.getTuples().get(2));
		
		mockServer.verify();
	}

	@Test
	public void executeUpdateQuery() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/saved_queries_params")))
			.andRespond(withResponse(compactedJson("data/saved_queries_update_result"), responseHeaders));
		
		QueryResult result = savedQueriesOperations.executeQuery("queryName", queryParams());
		
		Assert.assertNull(result.getTuples());
		Assert.assertEquals(new Integer(10), result.getRowsAffected());
		
		mockServer.verify();
	}
	
	@Test
	public void executeQueryNullQueryParams() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/saved_queries_params_empty")))
			.andRespond(withResponse(compactedJson("data/saved_queries_update_result"), responseHeaders));
	
		savedQueriesOperations.executeQuery("queryName");
		
		mockServer.verify();
	}
	
	@Test
	public void executeQueryEmptyQueryParams() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/queries/queryName/run"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(body(compactedJson("data/saved_queries_params_empty")))
			.andRespond(withResponse(compactedJson("data/saved_queries_update_result"), responseHeaders));

		savedQueriesOperations.executeQuery("queryName", new QueryParams());
		
		mockServer.verify();
	}

}
