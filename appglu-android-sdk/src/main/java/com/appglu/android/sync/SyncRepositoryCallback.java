package com.appglu.android.sync;

import java.util.List;

import com.appglu.StorageFile;

public interface SyncRepositoryCallback {
	
	void syncData();
	
	void syncFiles(List<StorageFile> files);

}