package com.appglu.android.cache;

import java.io.InputStream;
import java.util.Date;

/**
 * A {@code CacheManager} is used for storing and caching files while using the {@link com.appglu.android.storage.StorageApi}.<br>
 * There are three {@code CacheManager} implementations available: memory ({@link MemoryCacheManager}), disk ({@link FileSystemCacheManager}) or both ({@link MemoryAndFileSystemCacheManager}).<br>
 * By default {@link FileSystemCacheManager} is used and it will cache the files on disk.
 * 
 * @see MemoryCacheManager
 * @see FileSystemCacheManager
 * @see MemoryAndFileSystemCacheManager
 * @since 1.0.0
 */
public interface CacheManager {
	
	boolean exists(String fileName);
	
	Date lastModifiedDate(String fileName);
	
	boolean updateLastModifiedDate(String fileName);
	
	InputStream retrieve(String fileName);
	
	boolean store(String fileName, InputStream inputStream);
	
	boolean remove(String fileName);
	
	void removeAll();
	
	long cacheSize();

}