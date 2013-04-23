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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.StringUtils;

/**
 * {@link CacheManager} implementation that will store the files on disk.<br>
 * {@code FileSystemCacheManager} will first try the external storage, if it is available, if not it will use the internal storage.<br>
 * It is possible to set the maximum number of bytes used on disk by calling {@link #setMaxCacheSizeInBytes(long)}. By default is {@link #DEFAULT_CACHE_SIZE_IN_BYTES}.
 * @since 1.0.0
 */
public class FileSystemCacheManager implements CacheManager {
	
	private static final String SHARED_PREFERENCES_KEY = "com.appglu.android.cache.FileSystemCacheManager.SHARED_PREFERENCES_KEY";
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Context context;
	
	private static final long DEFAULT_CACHE_SIZE_IN_BYTES = 10 * 1024 * 1024; //10 megabytes
	
	private long maxCacheSizeInBytes = DEFAULT_CACHE_SIZE_IN_BYTES;
	
	public FileSystemCacheManager(Context context) {
		this.context = context;
	}
	
	public FileSystemCacheManager(Context context, long maxCacheSizeInBytes) {
		this.context = context;
		this.setMaxCacheSizeInBytes(maxCacheSizeInBytes);
	}
	
	public void setMaxCacheSizeInBytes(long maxCacheSizeInBytes) {
		if (maxCacheSizeInBytes > 0) {
			this.maxCacheSizeInBytes = maxCacheSizeInBytes;
		}
	}
	
	protected File getCacheDir() {
		File cacheDirectory = this.context.getExternalCacheDir();
		if (cacheDirectory == null) {
			//delegates to internal cache directory if external not available
			cacheDirectory = this.context.getCacheDir();
		}
		
		File appGluCacheDirectory = new File(cacheDirectory, "appglu_storage_cache");
		
		if (appGluCacheDirectory.exists() && appGluCacheDirectory.isDirectory()) {
			return appGluCacheDirectory;
		}
		
		appGluCacheDirectory.delete();
		appGluCacheDirectory.mkdir();
		
		return appGluCacheDirectory;
	}
	
	protected File getCacheFile(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return null;
		}
		File cacheDir = this.getCacheDir();
		return new File(cacheDir, fileName);
	}
	
	protected File[] listAllFiles() {
		File cacheDir = this.getCacheDir();
		return cacheDir.listFiles();
	}
	
	protected void pruneFilesIfNecessary() {
		File[] files = this.listAllFiles();
		
		if (files.length <= 1) {
			return;
		}
		
		long totalSize = 0;
		
		for (File file : files) {
			totalSize += file.length();
		}
		
		if (totalSize <= maxCacheSizeInBytes) {
			return;
		}
		
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File file1, File file2) {
				Long thisValue = file1.lastModified();
		        Long thatValue = file2.lastModified();
		        
		        if (thisValue.equals(thatValue)) {
		        	return file1.compareTo(file2);
		        }
		        
		        return thisValue.compareTo(thatValue);
			}
		});
		
		int i = 0;
		
		while (totalSize > maxCacheSizeInBytes && i < files.length - 1) {
			File oldestFile = files[i];
			
			totalSize -= oldestFile.length();
			oldestFile.delete();
			
			i++;
		}
	}
	
	protected Date readLastModifiedDateFromSharedPreferences(String fileName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
		long time = sharedPreferences.getLong(fileName, 0);
		if (time == 0) {
			return null;
		}
		return new Date(time);
	}
	
	protected boolean writeLastModifiedDateToSharedPreferences(String fileName) {
		Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
    	editor.putLong(fileName, new Date().getTime());
    	return editor.commit();
	}
	
	public boolean exists(String fileName) {
		File file = this.getCacheFile(fileName);
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	public Date lastModifiedDate(String fileName) {
		Date dateFromPreferences = this.readLastModifiedDateFromSharedPreferences(fileName);
		
		if (dateFromPreferences != null) {
			return dateFromPreferences;
		}
		
		File file = this.getCacheFile(fileName);
		if (file != null && file.exists()) {
			return new Date(file.lastModified());
		}
		return null;
	}
	
	public boolean updateLastModifiedDate(String fileName) {
		File file = this.getCacheFile(fileName);
		if (file != null && file.exists()) {
			return this.writeLastModifiedDateToSharedPreferences(fileName);
		}
		return false;
	}

	public InputStream retrieve(String fileName) {
		File file = this.getCacheFile(fileName);
		if (file != null && file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				//ignore - should never happen
			}
		}
		return null;
	}

	public boolean store(String fileName, InputStream inputStream) {
		try {
			File destinationFile = this.getCacheFile(fileName);
			
			if (destinationFile == null) {
				return false;
			}
			
			FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
			BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream, IOUtils.BUFFER_SIZE);
			IOUtils.copy(inputStream, outputStream);
			
			this.writeLastModifiedDateToSharedPreferences(fileName);
			
			this.pruneFilesIfNecessary();
			
			return true;
		} catch (IOException e) {
			this.remove(fileName);
			
			logger.warn("Error saving file to external storage cache", e);
			return false;
		}
	}

	public boolean remove(String fileName) {
		File file = this.getCacheFile(fileName);
		if (file == null) {
			return false;
		}
		return file.delete();
	}

	public void removeAll() {
		for (File file : this.listAllFiles()) {
			file.delete();
		}
	}
	
	public long cacheSize() {
		File[] files = this.listAllFiles();
		
		long totalSize = 0;
		
		for (File file : files) {
			totalSize += file.length();
		}
		
		return totalSize;
	}

}
