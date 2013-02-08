package com.appglu.android.cache;

import java.io.InputStream;
import java.util.Date;

import android.content.Context;

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