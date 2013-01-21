package com.appglu.android.sync;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;

import com.appglu.AsyncCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.SyncOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.AppGluAsyncCallbackTask;
import com.appglu.android.AppGluNotProperlyConfiguredException;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.util.IOUtils;

public final class SyncApi {
	
	public static final int NOTIFICATION_ID = Integer.MAX_VALUE;
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Context context;
	
	private SyncService syncService;
	
	public SyncApi(Context context, SyncOperations syncOperations, StorageOperations storageOperations, SyncRepository syncRepository) {
		this.context = context;
		this.syncService = new SyncService(syncOperations, storageOperations, syncRepository);
	}
	
	protected boolean doSyncDatabase() {
		return this.syncService.syncDatabase();
	}

	protected boolean doSyncDatabaseAndFiles() {
		return this.syncService.syncDatabaseAndFiles();
	}
	
	protected boolean doSyncTables(List<String> tables) {
		return this.syncService.syncTables(tables);
	}
	
	protected boolean doSyncTablesAndFiles(List<String> tables) {
		return this.syncService.syncTablesAndFiles(tables);
	}
	
	public File readFileFromFileStorage(StorageFile storageFile) {
		return this.syncService.getFileFromFileStorage(storageFile);
	}
	
	public InputStream readInputStreamFromFileStorage(StorageFile storageFile) {
		File file = this.readFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public byte[] readByteArrayFromFileStorage(StorageFile storageFile) {
		InputStream inputStream = this.readInputStreamFromFileStorage(storageFile);
		
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
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int requestedWidth, int requestedHeight) {
		File file = this.readFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromFile(file, requestedWidth, requestedHeight);
	}
	
	public void readFileFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<File> callback) {
		AppGluAsyncCallbackTask<File> asyncTask = new AppGluAsyncCallbackTask<File>(callback, new Callable<File>() {
			public File call() throws Exception {
				return readFileFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}

	public void readInputStreamFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<InputStream> callback) {
		AppGluAsyncCallbackTask<InputStream> asyncTask = new AppGluAsyncCallbackTask<InputStream>(callback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return readInputStreamFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}
	
	
	public void readByteArrayFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<byte[]> callback) {
		AppGluAsyncCallbackTask<byte[]> asyncTask = new AppGluAsyncCallbackTask<byte[]>(callback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return readByteArrayFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}

	
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, final int requestedWidth, final int requestedHeight, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
			}
		});
		asyncTask.execute();
	}
	
	public boolean isSyncIntentServiceRunning() {
		ActivityManager manager = (ActivityManager) this.context.getSystemService(Activity.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (SyncIntentService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void startSyncIntentService(SyncIntentServiceRequest request) {
		if (this.isSyncIntentServiceRunning()) {
			this.logger.info("SyncIntentService was not started because it is already running");
			return;
		}
		
		Intent intent = new Intent(this.context, SyncIntentService.class);
		this.validateSyncIntent(intent);
		
		intent.putExtra(SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA, request.getSyncFiles());
		
		if (request.getTablesToSync() != null) {
			intent.putStringArrayListExtra(SyncIntentService.TABLES_STRING_ARRAY_EXTRA, new ArrayList<String>(request.getTablesToSync()));
		}
		
		if (request.getExecutingSyncNotification() != null) {
			intent.putExtra(SyncIntentService.EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA, request.getExecutingSyncNotification());
			
			if (request.getChangesAppliedNotification() != null) {
				intent.putExtra(SyncIntentService.CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA, request.getChangesAppliedNotification());
			}
		}
		
		this.context.startService(intent);
		this.logger.info("SyncIntentService has being started");
	}
	
	private void validateSyncIntent(Intent intent) {
		PackageManager packageManager = this.context.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
		
		if (resolveInfo.size() == 0) {
			throw new AppGluNotProperlyConfiguredException("To be able to execute sync you must declare " +
					"a service named com.appglu.android.sync.SyncIntentService in the AndroidManifest.xml");
		}
	}

}