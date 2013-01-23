package com.appglu.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;
import com.appglu.impl.util.HashUtils;
import com.appglu.impl.util.IOUtils;
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

	public byte[] downloadStorageFile(StorageFile file) throws AppGluRestClientException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		this.streamStorageFile(file, new StorageStreamCallback() {
			
			public void doWithInputStream(InputStream inputStream) throws IOException {
				IOUtils.copy(inputStream, outputStream);
			}
		});
		
		return outputStream.toByteArray();
	}

	public void streamStorageFile(final StorageFile file, final StorageStreamCallback callback) throws AppGluRestClientException {
		URI uri = this.getStorageFileURI(file);
		
		try {
			RequestCallback requestCallback = new RequestCallback() {
				public void doWithRequest(ClientHttpRequest request) throws IOException {
					
				}
			};
			
			ResponseExtractor<Object> responseExtractor = new ResponseExtractor<Object>() {
				public Object extractData(ClientHttpResponse response) throws IOException {
					Md5DigestCalculatingInputStream inputStream = new Md5DigestCalculatingInputStream(response.getBody());
					
					callback.doWithInputStream(inputStream);
					
					byte[] contentMd5 = inputStream.getMd5Digest();
					String eTag = StringUtils.removeDoubleQuotes(response.getHeaders().getETag());
					
					if (!HashUtils.md5MatchesWithETag(contentMd5, eTag)) {
						throw new AppGluRestClientException("Unable to verify integrity of downloaded file. " +
		                        "Client calculated content hash didn't match hash calculated by server");
					}
					
					return null;
				}
			};
			
			this.downloadRestOperations.execute(uri, HttpMethod.GET, requestCallback, responseExtractor);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}

}