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
package com.appglu.android.sync;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
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
import com.appglu.SyncOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.AppGluNotProperlyConfiguredException;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.task.AsyncTaskExecutor;
import com.appglu.android.task.ImageViewAsyncCallback;

import de.greenrobot.event.EventBus;

/**
 * <p>{@code SyncApi} is used to synchronize the data in your local SQLite tables with the AppGlu server.<br>
 * Before using {@code SyncApi} you must first create a SQLite database, extending {@link SyncDatabaseHelper}.<br>
 * 
 * <p>There are two ways to provide a {@link SyncDatabaseHelper} instance to {@code SyncApi}:
 * 
 * <p><strong>1. Setting a default SQLite database helper on initialization:</strong>
 * <p><code>
 * AppGluSettings settings = new AppGluSettings("appKey", "appSecret");<br>
 * settings.setDefaultSyncDatabaseHelper(new ExampleSyncDatabaseHelper(this));<br>
 * 
 * SyncApi syncApi = AppGlu.syncApi();
 * </code>
 * 
 * <p><strong>2. Provide a SQLite database helper to {@link AppGlu#syncApi(SyncDatabaseHelper)}:</strong>
 * <p><code>
 * SyncApi syncApi = AppGlu.syncApi(new ExampleSyncDatabaseHelper(this));
 * </code>
 * 
 * @see SyncDatabaseHelper
 * @see com.appglu.android.AppGlu
 * @see com.appglu.SyncOperations
 * @see com.appglu.AsyncSyncOperations
 * @since 1.0.0
 */
public final class SyncApi {
	
	public static final int NOTIFICATION_ID = Integer.MAX_VALUE;
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Context context;
	
	private SyncService syncService;
	
	private static SyncApi lastUsedInstance;
	
	public SyncApi(Context context, SyncOperations syncOperations, SyncRepository syncRepository, SyncFileStorageService syncStorageService) {
		this.context = context;
		this.syncService = new SyncService(syncOperations, syncRepository, syncStorageService);
	}
	
	/* Methods to be used by SyncIntentService */
	
	/* package */ static SyncApi getLastUsedInstance() {
		return lastUsedInstance;
	}
	
	/* package */ boolean downloadChanges() {
		return syncService.downloadChanges();
	}

	/* package */ boolean downloadChangesAndFiles() {
		return syncService.downloadChangesAndFiles();
	}

	/* package */ boolean downloadChangesForTables(List<String> tables) {
		return syncService.downloadChangesForTables(tables);
	}

	/* package */ boolean downloadChangesAndFilesForTables(List<String> tables) {
		return syncService.downloadChangesAndFilesForTables(tables);
	}
	
	/* package */ boolean hasDownloadedChanges() {
		return syncService.hasDownloadedChanges();
	}
	
	/* package */ boolean discardChanges() {
		return syncService.discardChanges();
	}

	/* package */ boolean applyChanges() {
		return syncService.applyChanges();
	}
	
	/* Methods to check if database is synchronized */
	
	/**
	 * Returns <code>true</code> if all of your local SQLite tables are up to date with the server side.
	 */
	public boolean checkIfDatabaseIsSynchronized() {
		return syncService.checkIfDatabaseIsSynchronized();
	}
	
	/**
	 * Returns <code>true</code> if your local SQLite tables are up to date with the server side.
	 * @param tables the names of the tables you want to check if are up to date
	 */
	public boolean checkIfTablesAreSynchronized(String... tables) {
		return syncService.checkIfTablesAreSynchronized(Arrays.asList(tables));
	}
	
	/**
	 * Returns <code>true</code> if your local SQLite tables are up to date with the server side.
	 * @param tables the names of the tables you want to check if are up to date
	 */
	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		return syncService.checkIfTablesAreSynchronized(tables);
	}
	
	/**
	 * Asynchronous version of {@link #checkIfDatabaseIsSynchronized()}.
	 * @see #checkIfDatabaseIsSynchronized()
	 */
	public void checkIfDatabaseIsSynchronizedInBackground(AsyncCallback<Boolean> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor();
		executor.execute(asyncCallback, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return checkIfDatabaseIsSynchronized();
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #checkIfTablesAreSynchronized(String...)}.
	 * @see #checkIfTablesAreSynchronized(String...)
	 */
	public void checkIfTablesAreSynchronizedInBackground(AsyncCallback<Boolean> callback, String... tables) {
		this.checkIfTablesAreSynchronizedInBackground(Arrays.asList(tables), callback);
	}
	
	/**
	 * Asynchronous version of {@link #checkIfTablesAreSynchronized(List)}.
	 * @see #checkIfTablesAreSynchronized(List)
	 */
	public void checkIfTablesAreSynchronizedInBackground(final List<String> tables, AsyncCallback<Boolean> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor();
		executor.execute(asyncCallback, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return checkIfTablesAreSynchronized(tables);
			}
		});
	}
	
	/* Method to return all files from sync storage */
	
	/**
	 * Return all files that has previously being downloaded by the SyncIntentService.<br>
	 */
	public List<StorageFile> getAllFiles() {
		return syncService.getAllFiles();
	}
	
	/**
	 * Asynchronous version of {@link #getAllFiles()}.
	 * @see #getAllFiles()
	 */
	public void getAllFilesInBackground(AsyncCallback<List<StorageFile>> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<List<StorageFile>>() {
			public List<StorageFile> call() throws Exception {
				return getAllFiles();
			}
		});
	}
	
	/* Methods to read files from sync storage */
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @return A <code>File</code> object or <code>null</code>, if the file is not found
	 */
	public File readFileFromFileStorage(StorageFile storageFile) {
		return this.syncService.getFileFromFileStorage(storageFile);
	}
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @return A <code>InputStream</code> object or <code>null</code>, if the file is not found
	 */
	public InputStream readInputStreamFromFileStorage(StorageFile storageFile) {
		return this.syncService.readInputStreamFromFileStorage(storageFile);
	}
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @return A <code>byte[]</code> object or <code>null</code>, if the file is not found
	 */
	public byte[] readByteArrayFromFileStorage(StorageFile storageFile) {
		return this.syncService.readByteArrayFromFileStorage(storageFile);
	}
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @return A <code>Bitmap</code> object or <code>null</code>, if the file is not found
	 */
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile) {
		return this.syncService.readBitmapFromFileStorage(storageFile);
	}
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @return A <code>Bitmap</code> object or <code>null</code>, if the file is not found
	 */
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int inSampleSize) {
		return this.syncService.readBitmapFromFileStorage(storageFile, inSampleSize);
	}
	
	/**
	 * Reads a file that has previously being downloaded by the SyncIntentService.<br>
	 * @param storageFile has to contain either an ID or a URL
	 * @param requestedWidth the final image width will be close to the requestedWidth
	 * @param requestedHeight the final image height will be close to the requestedHeight
	 * @return A <code>Bitmap</code> object or <code>null</code>, if the file is not found
	 */
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int requestedWidth, int requestedHeight) {
		return this.syncService.readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
	}
	
	/* Methods to read files from sync storage in a background thread */
	
	/**
	 * Asynchronous version of {@link #readFileFromFileStorage(StorageFile)}.
	 * @see #readFileFromFileStorage(StorageFile)
	 */
	public void readFileFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<File> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<File>() {
			public File call() throws Exception {
				return readFileFromFileStorage(storageFile);
			}
		});
	}

	/**
	 * Asynchronous version of {@link #readInputStreamFromFileStorage(StorageFile)}.
	 * @see #readInputStreamFromFileStorage(StorageFile)
	 */
	public void readInputStreamFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<InputStream> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return readInputStreamFromFileStorage(storageFile);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #readByteArrayFromFileStorage(StorageFile)}.
	 * @see #readByteArrayFromFileStorage(StorageFile)
	 */
	public void readByteArrayFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<byte[]> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return readByteArrayFromFileStorage(storageFile);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #readBitmapFromFileStorage(StorageFile)}.
	 * @see #readBitmapFromFileStorage(StorageFile)
	 */
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<Bitmap> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #readBitmapFromFileStorage(StorageFile, int)}.
	 * @see #readBitmapFromFileStorage(StorageFile, int)
	 */
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, final int inSampleSize, AsyncCallback<Bitmap> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, inSampleSize);
			}
		});
	}

	/**
	 * Asynchronous version of {@link #readBitmapFromFileStorage(StorageFile, int, int)}.
	 * @see #readBitmapFromFileStorage(StorageFile, int, int)
	 */
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, final int requestedWidth, final int requestedHeight, AsyncCallback<Bitmap> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
			}
		});
	}
	
	/* Methods to read an image from sync storage in background using an ImageView and ProgressBar to update the UI  */
	
	/**
	 * Reads a file that has previously being downloaded by the {@link com.appglu.android.sync.SyncIntentService}, converts it to a <code>Bitmap</code>, and then make it available through the {@link com.appglu.android.task.ImageViewAsyncCallback}.<p>
	 * 
	 * @param storageFile has to contain either an ID or a URL
	 * @param imageViewCallback a {@link com.appglu.android.task.ImageViewAsyncCallback} can be instantiated either with a listener ({@link com.appglu.android.task.ImageViewAsyncCallback.ImageDownloadListener})
	 * or references to a <code>ImageView</code>, a <code>View</code> to display while loading the image and a <code>View</code> to use as a place holder in the case the image fails loading
	 * 
	 * @see com.appglu.android.task.ImageViewAsyncCallback
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromFile(File)
	 */
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, ImageViewAsyncCallback imageViewCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(imageViewCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile);
			}
		});
	}
	
	/**
	 * Reads a file that has previously being downloaded by the {@link com.appglu.android.sync.SyncIntentService}, converts it to a <code>Bitmap</code>, and then make it available through the {@link com.appglu.android.task.ImageViewAsyncCallback}.<p>
	 * 
	 * @param storageFile has to contain either an ID or a URL
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @param imageViewCallback a {@link com.appglu.android.task.ImageViewAsyncCallback} can be instantiated either with a listener ({@link com.appglu.android.task.ImageViewAsyncCallback.ImageDownloadListener})
	 * or references to a <code>ImageView</code>, a <code>View</code> to display while loading the image and a <code>View</code> to use as a place holder in the case the image fails loading
	 * 
	 * @see com.appglu.android.task.ImageViewAsyncCallback 
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromFile(File, int)
	 */
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, final int inSampleSize, ImageViewAsyncCallback imageViewCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(imageViewCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, inSampleSize);
			}
		});
	}

	/**
	 * Reads a file that has previously being downloaded by the {@link com.appglu.android.sync.SyncIntentService}, converts it to a <code>Bitmap</code>, and then make it available through the {@link com.appglu.android.task.ImageViewAsyncCallback}.<p>
	 * 
	 * @param storageFile has to contain either an ID or a URL
	 * @param requestedWidth the final image width will be close to the requestedWidth
	 * @param requestedHeight the final image height will be close to the requestedHeight
	 * @param imageViewCallback a {@link com.appglu.android.task.ImageViewAsyncCallback} can be instantiated either with a listener ({@link com.appglu.android.task.ImageViewAsyncCallback.ImageDownloadListener})
	 * or references to a <code>ImageView</code>, a <code>View</code> to display while loading the image and a <code>View</code> to use as a place holder in the case the image fails loading
	 * 
	 * @see com.appglu.android.task.ImageViewAsyncCallback
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromFile(File, int, int)
	 */
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, final int requestedWidth, final int requestedHeight, ImageViewAsyncCallback imageViewCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(imageViewCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
			}
		});
	}
	
	/* Methods related to SyncIntentService */
	
	/**
	 * Returns <code>true</code> if the Sync Service is currently running.<br>
	 * The Sync Service is started when {@link #startSyncIntentService(SyncIntentServiceRequest)} is called.
	 */
	public boolean isSyncIntentServiceRunning() {
		ActivityManager manager = (ActivityManager) this.context.getSystemService(Activity.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (SyncIntentService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Starts the Sync Service used to synchronize the data in your local SQLite tables with the AppGlu server.<br>
	 * Before calling this method you must make sure that {@link SyncIntentService} is declared on your AndroidManifest.xml.<br>
	 * 
	 * <p>To start the Sync Service using a {@code SyncIntentServiceRequest}, see the following example:
	 * 
	 * <p><code>
	 * SyncIntentServiceRequest request = SyncIntentServiceRequest.syncDatabaseAndFiles();<br>
	 * AppGlu.syncApi().startSyncIntentService(request);
	 * </code>
	 * 
	 * <p>The example above is going to start {@link SyncIntentService} and request it to download and apply changes to all of your tables as well as download all files that are new or updated.
	 * 
	 * @param request defines if changes are going to be downloaded only or downloaded and applied. Also, defines if files are going to be synchronized as well
	 * @see SyncIntentServiceRequest
	 */
	public void startSyncIntentService(SyncIntentServiceRequest request) {
		synchronized (SyncApi.class) {
			if (this.isSyncIntentServiceRunning()) {
				this.logger.info("SyncIntentService was not started because it is already running");
				return;
			}
			
			Intent intent = new Intent(this.context, SyncIntentService.class);
			this.validateSyncIntent(intent);
			
			intent.putExtra(SyncIntentService.SYNC_INTENT_REQUEST_PARCELABLE, request);
			
			lastUsedInstance = this;
			
			this.context.startService(intent);
			this.logger.info("SyncIntentService has being started");
		}
	}
	
	private void validateSyncIntent(Intent intent) {
		PackageManager packageManager = this.context.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
		
		if (resolveInfo.size() == 0) {
			throw new AppGluNotProperlyConfiguredException("To be able to execute sync you must declare " +
					"a service named com.appglu.android.sync.SyncIntentService in the AndroidManifest.xml");
		}
	}
	
	/**
	 * Registers a {@link SyncListener} to listen for events fired by {@link SyncIntentService}.<br>
	 * Ideally, call {@link #registerSyncListener(SyncListener)} from <code>onCreate()</code> or <code>onStart()</code> or <code>onResume()</code> methods.<br>
	 * Call {@link #unregisterSyncListener(SyncListener)} from <code>onDestroy()</code> or <code>onStop()</code> or <code>onPause()</code> methods.
	 * @see #unregisterSyncListener(SyncListener)
	 */
	public void registerSyncListener(SyncListener syncListener) {
		EventBus.getDefault().registerSticky(syncListener);
	}
	
	/**
	 * Unregister a {@link SyncListener}.<br>
	 * Ideally, call {@link #registerSyncListener(SyncListener)} from <code>onCreate()</code> or <code>onStart()</code> or <code>onResume()</code> methods.<br>
	 * Call {@link #unregisterSyncListener(SyncListener)} from <code>onDestroy()</code> or <code>onStop()</code> or <code>onPause()</code> methods.
	 * @see #registerSyncListener(SyncListener)
	 */
	public void unregisterSyncListener(SyncListener syncListener) {
		EventBus.getDefault().unregister(syncListener);
	}

}
