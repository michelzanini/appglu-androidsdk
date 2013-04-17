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

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.sync.SyncIntentServiceRequest.SyncRequestOperation;

import de.greenrobot.event.EventBus;

/**
 * <p>This is the Android service responsible for executing the Sync operation with the AppGlu server.<br>
 * <p>You must declare this service in your AndroidManifest.xml like bellow:
 * 
 * <p><code>
 * &lt;service android:name="com.appglu.android.sync.SyncIntentService" /&gt;
 * </code>
 * 
 * <p>To initiate the Sync operation you must use a {@link SyncIntentServiceRequest} and call {@link SyncApi#startSyncIntentService(SyncIntentServiceRequest)}:
 * 
 * <p><code>
 * SyncIntentServiceRequest request = SyncIntentServiceRequest.syncDatabaseAndFiles();<br>
 * AppGlu.syncApi().startSyncIntentService(request);
 * </code>
 * 
 * @see SyncApi
 * @see SyncIntentServiceRequest
 * @since 1.0.0
 */
public class SyncIntentService extends IntentService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	public static final String SYNC_INTENT_REQUEST_PARCELABLE = "SyncIntentService.SYNC_INTENT_REQUEST_PARCELABLE";
	
	public SyncIntentService() {
		super("SyncIntentService");
	}
	
	private SyncApi getSyncApiInstance() {
		if (SyncApi.getLastUsedInstance() == null) {
			return AppGlu.syncApi();
		}
		return SyncApi.getLastUsedInstance();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean successful = false;
		boolean exceptionWasThrown = false;
		
		SyncIntentServiceRequest request = intent.getParcelableExtra(SYNC_INTENT_REQUEST_PARCELABLE);
		
		try {
			if (request.getSyncServiceRunningNotification() != null) {
				Notification notification = request.getSyncServiceRunningNotification();
				this.sendNotification(notification, false);
			}
			
			SyncRequestOperation syncOperation = request.getSyncRequestOperation();
			
			boolean isApplyChanges = SyncRequestOperation.APPLY_CHANGES.equals(syncOperation);
			boolean isDiscardChanges = SyncRequestOperation.DISCARD_CHANGES.equals(syncOperation);

			boolean isDownloadChanges = SyncRequestOperation.DOWNLOAD_CHANGES.equals(syncOperation);
			boolean isDownloadAndApplyChanges = SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES.equals(syncOperation);

			boolean requiresInternet = isDownloadChanges || isDownloadAndApplyChanges;
			
			if (requiresInternet && !AppGlu.hasInternetConnection()) {
				this.postEvent(SyncEvent.Type.ON_NO_INTERNET_CONNECTION);
				
				logger.info("SyncIntentService did not executed because it could not connect to the Internet");
				return;
			}
			
			this.postEvent(SyncEvent.Type.ON_PRE_EXECUTE);
			
			List<String> tables = request.getTablesToSync();
			boolean syncFiles = request.getSyncFiles();
			
			if (isApplyChanges) {
				successful = this.applyChanges();
			} 
			
			if (isDiscardChanges) {
				successful = this.discardChanges();
			}
			
			if (isDownloadChanges) {
				successful = this.downloadChangesAndFilesForTables(tables, syncFiles);
			}
			
			if (isDownloadAndApplyChanges) {
				boolean hasChanges = this.downloadChangesAndFilesForTables(tables, syncFiles);
				if (hasChanges) {
					successful = this.applyChanges();
				} else {
					successful = false;
				}
			}
			
			this.postResultEvent(successful);
			
		} catch (Exception exception) {
			exceptionWasThrown = true;
			this.postExceptionEvent(exception);
		} finally {
			if (request.getSyncServiceRunningNotification() != null) {
				if (successful && request.getSyncServiceCompletedNotification() != null) {
					Notification notification = request.getSyncServiceCompletedNotification();
					this.sendNotification(notification, true);
				} else {
					this.cancelNotification();
				}
			}
		}
		
		boolean wasSuccessful = !exceptionWasThrown;
		this.postFinishEvent(wasSuccessful);
		logger.info("SyncIntentService has being stopped");
	}

	private boolean applyChanges() {
		if (!this.getSyncApiInstance().hasDownloadedChanges()) {
			this.logger.info("No changes to apply");
			return false;
		}
		
		try {
			this.postEvent(SyncEvent.Type.ON_TRANSACTION_START);
			return this.getSyncApiInstance().applyChanges();
		} finally {
			this.postEvent(SyncEvent.Type.ON_TRANSACTION_FINISH);
		}
	}
	
	private boolean discardChanges() {
		return this.getSyncApiInstance().discardChanges();
	}
	
	private boolean downloadChangesAndFilesForTables(List<String> tables, boolean syncFiles) {
		if (tables == null) {
			if (syncFiles) {
				return this.getSyncApiInstance().downloadChangesAndFiles();
			} else {
				return this.getSyncApiInstance().downloadChanges();
			}
		} else {
			if (syncFiles) {
				return this.getSyncApiInstance().downloadChangesAndFilesForTables(tables);
			} else {
				return this.getSyncApiInstance().downloadChangesForTables(tables);
			}
		}
	}
	
	private void sendNotification(Notification notification, boolean autoCancelNotification) {
		if (autoCancelNotification) {
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags &= ~Notification.FLAG_ONGOING_EVENT;
			notification.flags &= ~Notification.FLAG_NO_CLEAR;
		} else {
			notification.flags &= ~Notification.FLAG_AUTO_CANCEL;
			notification.flags &= ~Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_NO_CLEAR;
		}
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(SyncApi.NOTIFICATION_ID, notification);
	}

	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(SyncApi.NOTIFICATION_ID);
	}
	
	private void postEvent(SyncEvent.Type eventType) {
		EventBus.getDefault().postSticky(new SyncEvent(eventType));
	}
	
	private void postResultEvent(boolean changesWereApplied) {
		EventBus.getDefault().postSticky(new SyncEvent(SyncEvent.Type.ON_RESULT, changesWereApplied));
	}
	
	private void postFinishEvent(boolean wasSuccessful) {
		EventBus.getDefault().postSticky(new SyncEvent(SyncEvent.Type.ON_FINISH, wasSuccessful));
	}
	
	private void postExceptionEvent(Exception exception) {
		logger.error(exception);
		
		SyncExceptionWrapper wrapper = new SyncExceptionWrapper(exception);
		EventBus.getDefault().postSticky(new SyncEvent(wrapper));
	}

}
