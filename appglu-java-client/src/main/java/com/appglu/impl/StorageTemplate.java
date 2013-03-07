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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.InputStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.impl.util.HashUtils;
import com.appglu.impl.util.Md5DigestCalculatingInputStream;
import com.appglu.impl.util.StringUtils;

public final class StorageTemplate implements StorageOperations {
	
	private final RestOperations downloadRestOperations;
	
	public StorageTemplate(RestOperations downloadRestOperations) {
		this.downloadRestOperations = downloadRestOperations;
	}
	
	private URI getStorageFileURI(StorageFile file) {
		if (file == null || !file.hasUrl()) {
			throw new AppGluRestClientException("StorageFile must not be null and must containg an URL");
		}
		try {
            return new URI(file.getUrl());
        } catch (URISyntaxException e) {
        	throw new AppGluRestClientException("StorageFile must have a valid URL");
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFile(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		
		RequestCallback requestCallback = new RequestCallback() {
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				
			}
		};
		
		this.streamStorageFile(file, inputStreamCallback, requestCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean streamStorageFileIfModifiedSince(final StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		
		RequestCallback requestCallback = new RequestCallback() {
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				if (file.hasLastModifiedDate()) {
					request.getHeaders().setIfModifiedSince(file.getLastModified().getTime());
				}
			}
		};
		
		return this.streamStorageFile(file, inputStreamCallback, requestCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean streamStorageFileIfNoneMatch(final StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		
		RequestCallback requestCallback = new RequestCallback() {
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				if (file.hasETag()) {
					request.getHeaders().setIfNoneMatch(StringUtils.addDoubleQuotes(file.getETag()));
				}
			}
		};
		
		return this.streamStorageFile(file, inputStreamCallback, requestCallback);
	}
	
	protected boolean streamStorageFile(StorageFile file, final InputStreamCallback inputStreamCallback, RequestCallback requestCallback) throws AppGluRestClientException {
		URI uri = this.getStorageFileURI(file);
		
		try {
			ResponseExtractor<Boolean> responseExtractor = new ResponseExtractor<Boolean>() {
				public Boolean extractData(ClientHttpResponse response) throws IOException {
					if (response.getStatusCode() == HttpStatus.NOT_MODIFIED) {
						return false;
					}
					
					Md5DigestCalculatingInputStream inputStream = new Md5DigestCalculatingInputStream(response.getBody());
					
					inputStreamCallback.doWithInputStream(inputStream);
					
					byte[] contentMd5 = inputStream.getMd5Digest();
					String eTag = StringUtils.removeDoubleQuotes(response.getHeaders().getETag());
					
					if (!HashUtils.md5MatchesWithETag(contentMd5, eTag)) {
						throw new AppGluRestClientException("Unable to verify integrity of downloaded file. " +
		                        "Client calculated content hash didn't match hash calculated by server");
					}
					
					return true;
				}
			};
			
			return this.downloadRestOperations.execute(uri, HttpMethod.GET, requestCallback, responseExtractor);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

}
