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

import java.io.InputStream;
import java.util.Date;

import android.content.Context;

/**
 * {@link CacheManager} implementation that will store the files on memory and disk together, forming a two-level cache.<br>
 * When retrieving the file it will first look on memory, if not found it will try the disk.<br>
 * The maximum number of items on memory and the maximum number of bytes used on disk can be defined if the constructor {@link #MemoryAndFileSystemCacheManager(Context, int, long)} is used.
 * If {@link #MemoryAndFileSystemCacheManager(Context)} constructor is used then default values will be applied.
 * 
 * @see MemoryCacheManager
 * @see FileSystemCacheManager
 * 
 * @since 1.0.0
 */
public class MemoryAndFileSystemCacheManager implements CacheManager {
	
	private MemoryCacheManager memoryCacheManager;
	
	private FileSystemCacheManager fileSystemCacheManager;
	
	public MemoryAndFileSystemCacheManager(Context context) {
		this.memoryCacheManager = new MemoryCacheManager();
		this.fileSystemCacheManager = new FileSystemCacheManager(context);
	}

	public MemoryAndFileSystemCacheManager(Context context, int maxMemoryNumberOfItems, long maxFileSystemCacheSizeInBytes) {
		this.memoryCacheManager = new MemoryCacheManager(maxMemoryNumberOfItems);
		this.fileSystemCacheManager = new FileSystemCacheManager(context, maxFileSystemCacheSizeInBytes);
	}

	public synchronized boolean exists(String fileName) {
		return memoryCacheManager.exists(fileName) || fileSystemCacheManager.exists(fileName);
	}

	public synchronized Date lastModifiedDate(String fileName) {
		Date lastModifiedDate = memoryCacheManager.lastModifiedDate(fileName);
		if (lastModifiedDate != null) {
			return lastModifiedDate;
		}
		return fileSystemCacheManager.lastModifiedDate(fileName);
	}

	public synchronized boolean updateLastModifiedDate(String fileName) {
		boolean memorySuccess = memoryCacheManager.updateLastModifiedDate(fileName);
		boolean fileSystemSuccess = fileSystemCacheManager.updateLastModifiedDate(fileName);
		
		return memorySuccess || fileSystemSuccess;
	}

	public synchronized InputStream retrieve(String fileName) {
		InputStream inputStream = memoryCacheManager.retrieve(fileName);
		if (inputStream != null) {
			return inputStream;
		}
		return fileSystemCacheManager.retrieve(fileName);
	}

	public synchronized boolean store(String fileName, InputStream inputStream) {
		boolean fileSystemSuccess = fileSystemCacheManager.store(fileName, inputStream);
		
		if (!fileSystemSuccess) {
			return false;
		}
		
		InputStream cachedInputStream = fileSystemCacheManager.retrieve(fileName);
		
		if (cachedInputStream == null) {
			return false;
		}
		
		return memoryCacheManager.store(fileName, cachedInputStream);
	}

	public synchronized boolean remove(String fileName) {
		boolean memorySuccess = memoryCacheManager.remove(fileName);
		boolean fileSystemSuccess = fileSystemCacheManager.remove(fileName);
		
		return memorySuccess || fileSystemSuccess;
	}

	public synchronized void removeAll() {
		memoryCacheManager.removeAll();
		fileSystemCacheManager.removeAll();
	}

	/* file system cache size is likely to be bigger then memory cache size */
	public synchronized long cacheSize() {
		return fileSystemCacheManager.cacheSize();
	}

}
