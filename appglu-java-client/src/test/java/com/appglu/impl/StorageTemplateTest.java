package com.appglu.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;

import com.appglu.AppGluRestClientException;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;
import com.appglu.impl.util.IOUtils;

public class StorageTemplateTest extends AbstractAppGluApiTest {
	
	private static final String URL = "https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png";
	
	private static final byte[] CONTENT = "content".getBytes();
	
	private static final String ETAG = "\"9a0364b9e99bb480dd25e1f0284c8555\"";

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
	public void downloadFile() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		byte[] response = this.storageOperations.downloadStorageFile(new StorageFile(URL));
		
		Assert.assertEquals("content", new String(response));
		
		downloadMockServer.verify();
	}
	
	@Test
	public void streamStorageFile() {
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		this.storageOperations.streamStorageFile(new StorageFile(URL), new StorageStreamCallback() {
			
			public void doWithInputStream(InputStream fileStream) throws IOException {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				IOUtils.copy(fileStream, outputStream);
				
				Assert.assertEquals("content", new String(outputStream.toByteArray()));
			}
		});
		
		downloadMockServer.verify();
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void downloadFile_invalidETag() {
		String invalidETag = "\"1234567890\"";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setETag(invalidETag);
		
		downloadMockServer.expect(requestTo(URL))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(CONTENT).headers(responseHeaders));
		
		this.storageOperations.downloadStorageFile(new StorageFile(URL));
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void downloadFile_invalidStorageFile() {
		this.storageOperations.downloadStorageFile(new StorageFile());
	}
	
	@Test (expected = AppGluRestClientException.class)
	public void streamStorageFile_invalidStorageFile() {
		this.storageOperations.streamStorageFile(new StorageFile(), new StorageStreamCallback() {
			
			public void doWithInputStream(InputStream fileStream) throws IOException {
				Assert.fail("Should not execute streamStorageFile because StorageFile is invalid");
			}
		});
	}
	
}