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
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.Row;
import com.appglu.SavedQueriesOperations;

public class SavedQueriesTemplateTest extends AbstractAppGluApiTest {
	
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
