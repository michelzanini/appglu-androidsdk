package com.appglu.android.cache;

import java.io.InputStream;
import java.util.Date;

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