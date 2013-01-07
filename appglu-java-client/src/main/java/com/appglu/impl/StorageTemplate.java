package com.appglu.impl;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.StorageStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.impl.util.StringUtils;

public final class StorageTemplate implements StorageOperations {
	
	private final RestOperations downloadRestOperations;
	
	public StorageTemplate(RestOperations downloadRestOperations) {
		this.downloadRestOperations = downloadRestOperations;
	}
	
	private void validateStorageFileUrl(StorageFile file) {
		if (file == null || StringUtils.isEmpty(file.getUrl())) {
			throw new AppGluRestClientException("StorageFile must not be null and must containg an URL");
		}
	}

	public byte[] downloadStorageFile(StorageFile file) throws AppGluRestClientException {
		this.validateStorageFileUrl(file);
		try {
			return this.downloadRestOperations.getForObject(file.getUrl(), byte[].class);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

	public void streamStorageFile(StorageFile file, final StorageStreamCallback callback) throws AppGluRestClientException {
		this.validateStorageFileUrl(file);
		try {
			RequestCallback requestCallback = new RequestCallback() {
				public void doWithRequest(ClientHttpRequest request) throws IOException {
					
				}
			};
			
			ResponseExtractor<Object> responseExtractor = new ResponseExtractor<Object>() {
				public Object extractData(ClientHttpResponse response) throws IOException {
					callback.doWithInputStream(response.getBody());
					return null;
				}
			};
			
			this.downloadRestOperations.execute(file.getUrl(), HttpMethod.GET, requestCallback, responseExtractor);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

}