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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;

import com.appglu.AppGluRestClientException;
import com.appglu.InputStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.StringUtils;

public class StorageTemplateTest extends AbstractAppGluApiTest {
	
	private static final String URL = "https://d27pqr4ityvq45.cloudfront.net/trekguide_staging/5cff0bf8-74ff-4a7a-9092-60841018b5ea-DS9-S01-E01-Grid-1.jpg";
	
	private static final byte[] CONTENT = "content".getBytes();
	
	private static final String ETAG = "\"9a0364b9e99bb480dd25e1f0284c8555\"";
	
	private static final long LAST_MODIFIED_DATE = 1358366278000L;

	private MockRestServiceServer downloadMockServer;
	
	private StorageOperations storageOperations;
	
	@Before
	public void setup() {
		super.setup();
		storageOperations = appGluTemplate.storageOperations();
		
		downloadMockServer = MockRestServiceServer.createServer(appGluTemplate.getDownloadRestTemplate());
		
		responseHeaders = new HttpHeaders();
		responseHeaders.setETag(ETAG);
	}
	
	@Test
	public void streamStorageFile() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		this.storageOperations.streamStorageFile(new StorageFile(URL), new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				IOUtils.copy(inputStream, outputStream);
				
				Assert.assertEquals(new String(CONTENT), new String(outputStream.toByteArray()));
			}
		});
		
		downloadMockServer.verify();
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void streamStorageFile_invalidETag() {
		String invalidETag = "\"1234567890\"";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setETag(invalidETag);
		
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		this.storageOperations.streamStorageFile(new StorageFile(URL), new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				IOUtils.copy(inputStream, new ByteArrayOutputStream());
			}
		});
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void streamStorageFile_eTagDoesNotMatch() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		this.storageOperations.streamStorageFile(new StorageFile(URL), new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				//do not read the content so the md5 will not be calculated and then will not match with the server side
			}
		});
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void streamStorageFile_invalidStorageFile() {
		this.storageOperations.streamStorageFile(new StorageFile(), new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				Assert.fail("Should not execute streamStorageFile because StorageFile is invalid");
			}
		});
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void streamStorageFileIfModifiedSince() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(header("If-Modified-Since", notNullValue()))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		StorageFile storageFile = new StorageFile(URL);
		storageFile.setLastModified(new Date(LAST_MODIFIED_DATE));
		
		boolean wasModified = this.storageOperations.streamStorageFileIfModifiedSince(storageFile, new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				IOUtils.copy(inputStream, outputStream);
				
				Assert.assertEquals(new String(CONTENT), new String(outputStream.toByteArray()));
			}
		});
		
		Assert.assertTrue(wasModified);
		
		downloadMockServer.verify();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void streamStorageFileIfModifiedSince_notModified() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(header("If-Modified-Since", notNullValue()))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.NOT_MODIFIED));
		
		StorageFile storageFile = new StorageFile(URL);
		storageFile.setLastModified(new Date(LAST_MODIFIED_DATE));
		
		boolean wasModified = this.storageOperations.streamStorageFileIfModifiedSince(storageFile, new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				Assert.fail("Should not stream the file because it was not modified");
			}
		});
		
		Assert.assertFalse(wasModified);
		
		downloadMockServer.verify();
	}
	
	@Test
	public void streamStorageFileIfNoneMatch() {
		String invalidETag = "\"1234567890\"";
		
		downloadMockServer.expect(requestTo(URL))
			.andExpect(header("If-None-Match", invalidETag))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		StorageFile storageFile = new StorageFile(URL);
		storageFile.setETag(StringUtils.removeDoubleQuotes(invalidETag));
		
		boolean wasModified = this.storageOperations.streamStorageFileIfNoneMatch(storageFile, new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				IOUtils.copy(inputStream, outputStream);
				
				Assert.assertEquals(new String(CONTENT), new String(outputStream.toByteArray()));
			}
		});
		
		Assert.assertTrue(wasModified);
		
		downloadMockServer.verify();
	}
	
	@Test
	public void streamStorageFileIfNoneMatch_notModified() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(header("If-None-Match", ETAG))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.NOT_MODIFIED));
		
		StorageFile storageFile = new StorageFile(URL);
		storageFile.setETag(StringUtils.removeDoubleQuotes(ETAG));
		
		boolean wasModified = this.storageOperations.streamStorageFileIfNoneMatch(storageFile, new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				Assert.fail("Should not stream the file because it was not modified");
			}
		});
		
		Assert.assertFalse(wasModified);
		
		downloadMockServer.verify();
	}
	
}
