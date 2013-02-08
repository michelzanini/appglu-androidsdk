package com.appglu.android.cache;

public class MemoryAndFileSystemCacheManagerTest extends AbstractCacheManagerTest {

	@Override
	protected CacheManager setupCacheManager() {
		return new MemoryAndFileSystemCacheManager(getContext());
	}

}
