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
package com.appglu.android.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.impl.util.IOUtils;

/**
 * {@link CacheManager} implementation that will store the files on memory.<br>
 * It is possible to set the maximum number of items on memory but calling {@link #setMaxCacheSizeInNumberOfItems(int)}. By default is {@link #DEFAULT_MAX_CACHE_SIZE_IN_NUMBER_OF_ITEMS}.<br>
 * @since 1.0.0
 */
public class MemoryCacheManager implements CacheManager {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private static final int DEFAULT_MAX_CACHE_SIZE_IN_NUMBER_OF_ITEMS = 50;
	
	private int maxCacheSizeInNumberOfItems = DEFAULT_MAX_CACHE_SIZE_IN_NUMBER_OF_ITEMS;
	
	@SuppressWarnings("serial")
	private LinkedHashMap<String, MemoryFile> cache = new LinkedHashMap<String, MemoryFile>() {

		protected boolean removeEldestEntry(Entry<String, MemoryFile> eldest) {
			return size() > maxCacheSizeInNumberOfItems;
		}
		
	};
	
	private static class MemoryFile {
		private Date lastModifiedDate;
		private byte[] content;
	}
	
	public MemoryCacheManager() {
		
	}
	
	public MemoryCacheManager(int maxCacheSizeInNumberOfItems) {
		this.setMaxCacheSizeInNumberOfItems(maxCacheSizeInNumberOfItems);
	}
	
	public void setMaxCacheSizeInNumberOfItems(int maxCacheSizeInNumberOfItems) {
		if (maxCacheSizeInNumberOfItems > 0) {
			this.maxCacheSizeInNumberOfItems = maxCacheSizeInNumberOfItems;
		}
	}

	public boolean exists(String fileName) {
		return this.cache.containsKey(fileName);
	}

	public Date lastModifiedDate(String fileName) {
		MemoryFile memoryFile = this.cache.get(fileName);
		
		if (memoryFile != null) {
			return memoryFile.lastModifiedDate;
		}
		
		return null;
	}

	public boolean updateLastModifiedDate(String fileName) {
		if (this.exists(fileName)) {
			//removing and putting the object back on the list will update the last inserted order of LinkedHashMap
			MemoryFile fileToUpdate = this.cache.remove(fileName);
			fileToUpdate.lastModifiedDate = new Date();
			this.cache.put(fileName, fileToUpdate);
			
			return true;
		}
		
		return false;
	}

	public InputStream retrieve(String fileName) {
		MemoryFile memoryFile = this.cache.get(fileName);
		
		if (memoryFile != null) {
			return new ByteArrayInputStream(memoryFile.content);
		}
		
		return null;
	}

	public boolean store(String fileName, InputStream inputStream) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, outputStream);
			
			MemoryFile memoryFile = new MemoryFile();
			
			memoryFile.lastModifiedDate = new Date();
			memoryFile.content = outputStream.toByteArray();
			
			//removing the key first will update the last inserted order of LinkedHashMap
			if (this.cache.containsKey(fileName)) {
				this.cache.remove(fileName);
			}
			
			this.cache.put(fileName, memoryFile);
			
			return true;
		} catch (IOException e) {
			this.remove(fileName);
			
			logger.warn("Error saving file to memory cache", e);
			return false;
		}
	}

	public boolean remove(String fileName) {
		return this.cache.remove(fileName) != null;
	}

	public void removeAll() {
		this.cache.clear();
	}
	
	public long cacheSize() {
		return this.cache.size();
	}
	
}
