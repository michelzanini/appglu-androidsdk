package com.appglu.android.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.graphics.Bitmap;

import com.appglu.InputStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.cache.CacheManager;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.util.HashUtils;
import com.appglu.impl.util.IOUtils;

public class StorageService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private StorageOperations storageOperations;

	private CacheManager cacheManager;
	
	private static final long DEFAULT_CACHE_TIME_TO_LIVE_IN_MILLISECONDS = 60 * 60 * 1000; //1 hour
	
	private long cacheTimeToLiveInMilliseconds = DEFAULT_CACHE_TIME_TO_LIVE_IN_MILLISECONDS;

	public StorageService(StorageOperations storageOperations, CacheManager cacheManager) {
		this.storageOperations = storageOperations;
		this.cacheManager = cacheManager;
	}
	
	public synchronized void setCacheTimeToLiveInMilliseconds(long cacheTimeToLiveInMilliseconds) {
		if (cacheTimeToLiveInMilliseconds > 0) {
			this.cacheTimeToLiveInMilliseconds = cacheTimeToLiveInMilliseconds;
		}
	}

	protected synchronized String cacheKeyForStorageFile(StorageFile file) {
		if (file == null || !file.hasUrl()) {
			return null;
		}
		String hash = String.valueOf(file.getUrl().hashCode());
		return HashUtils.toHexString(hash);
	}

	public synchronized InputStream downloadAsInputStream(final StorageFile file) {
		final String cacheKey = this.cacheKeyForStorageFile(file);
		
		if (this.cacheManager.exists(cacheKey)) {
			Date lastModifiedDate = this.cacheManager.lastModifiedDate(cacheKey);
			
			if (lastModifiedDate != null) {
				long now = System.currentTimeMillis();
				long lastModified = lastModifiedDate.getTime();
				
				if ( (lastModified + this.cacheTimeToLiveInMilliseconds) >= now ) {
					if (logger.isDebugEnabled()) {
						logger.debug("Retrieving '" + cacheKey + "' from cache");
					}
					return this.cacheManager.retrieve(cacheKey);
				}
				
				file.setLastModified(lastModifiedDate);
			}
		}
		
		boolean wasModified = this.storageOperations.streamStorageFileIfModifiedSince(file, new InputStreamCallback() {
			public void doWithInputStream(InputStream inputStream) throws IOException {
				if (logger.isDebugEnabled()) {
					logger.debug("Downloading '" + cacheKey + "' from network");
				}
				cacheManager.store(cacheKey, inputStream);
			}
		});
		
		if (!wasModified) {
			if (logger.isDebugEnabled()) {
				logger.debug("Updating '" + cacheKey + "' last modified date");
			}
			this.cacheManager.updateLastModifiedDate(cacheKey);
		}
		
		return this.cacheManager.retrieve(cacheKey);
	}

	public synchronized byte[] downloadAsByteArray(StorageFile file) {
		InputStream inputStream = this.downloadAsInputStream(file);
		
		if (inputStream == null) {
			return null;
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			return null;
		}
		
		return outputStream.toByteArray();
	}
	
	public synchronized Bitmap downloadAsBitmap(StorageFile file) {
		InputStream inputStream = this.downloadAsInputStream(file);
		
		if (inputStream == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromInputStream(inputStream);
	}
	
	public synchronized Bitmap downloadAsBitmap(StorageFile file, int inSampleSize) {
		InputStream inputStream = this.downloadAsInputStream(file);
		
		if (inputStream == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromInputStream(inputStream, inSampleSize);
	}

}