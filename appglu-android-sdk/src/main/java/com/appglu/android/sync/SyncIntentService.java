package com.appglu.android.sync;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.appglu.android.AppGlu;

public class SyncIntentService extends IntentService {
	
	public static final String SYNC_FILES_BOOLEAN_EXTRA = "SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA";
	
	public static final String TABLES_STRING_ARRAY_EXTRA = "SyncIntentService.TABLES_STRING_ARRAY_EXTRA";
	
	public static final String EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String PRE_EXECUTE_ACTION = "SyncIntentService.ON_PRE_EXECUTE_ACTION";
	
	public static final String NO_INTERNET_CONNECTION_ACTION = "SyncIntentService.ON_NO_INTERNET_CONNECTION_ACTION";
	
	public static final String RESULT_ACTION = "SyncIntentService.RESULT_ACTION";
	
	public static final String EXCEPTION_ACTION = "SyncIntentService.EXCEPTION_ACTION";
	
	public static final String FINISH_ACTION = "SyncIntentService.FINISH_ACTION";
	
	public static final String EXCEPTION_SERIALIZABLE_EXTRA = "SyncIntentService.EXCEPTION_SERIALIZABLE_EXTRA";
	
	public static final String CHANGES_WERE_APPLIED_BOOLEAN_EXTRA = "SyncIntentService.CHANGES_WERE_APPLIED_BOOLEAN_EXTRA";
	
	public SyncIntentService() {
		super("SyncIntentService");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (this.hasExecutingSyncNotificationExtra(intent)) {
			Notification notification = (Notification) intent.getParcelableExtra(EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA);
			this.sendNotification(notification, false);
		}
	}

	private boolean hasExecutingSyncNotificationExtra(Intent intent) {
		return intent.hasExtra(EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA);
	}
	
	private boolean hasChangesAppliedNotificationExtra(Intent intent) {
		return intent.hasExtra(CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean changesWereApplied = false;
		
		try {
			if (!AppGlu.hasInternetConnection()) {
				this.broadcastAction(NO_INTERNET_CONNECTION_ACTION);
				return;
			}
			
			this.broadcastAction(PRE_EXECUTE_ACTION);
			
			ArrayList<String> tables = intent.getStringArrayListExtra(TABLES_STRING_ARRAY_EXTRA);
			boolean syncFiles = intent.getBooleanExtra(SYNC_FILES_BOOLEAN_EXTRA, false);
			
			if (tables == null) {
				if (syncFiles) {
					changesWereApplied = AppGlu.syncApi().doSyncDatabaseAndFiles();
				} else {
					changesWereApplied = AppGlu.syncApi().doSyncDatabase();
				}
			} else {
				if (syncFiles) {
					changesWereApplied = AppGlu.syncApi().doSyncTablesAndFiles(tables);
				} else {
					changesWereApplied = AppGlu.syncApi().doSyncTables(tables);
				}
			}
			
			this.broadcastResult(changesWereApplied);
	
		} catch (Exception exception) {
			this.broadcastException(exception);
		} finally {
			if (this.hasExecutingSyncNotificationExtra(intent)) {
				if (changesWereApplied && this.hasChangesAppliedNotificationExtra(intent)) {
					Notification notification = (Notification) intent.getParcelableExtra(CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA);
					this.sendNotification(notification, true);
				} else {
					this.cancelNotification();
				}
			}
		}
		
		this.broadcastAction(FINISH_ACTION);
	}
	
	protected void sendNotification(Notification notification, boolean autoCancelNotification) {
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

	protected void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(SyncApi.NOTIFICATION_ID);
	}
	
	protected void broadcastAction(String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		this.sendBroadcast(intent);
	}
	
	protected void broadcastResult(boolean changesWereApplied) {
		Intent intent = new Intent();
		intent.putExtra(CHANGES_WERE_APPLIED_BOOLEAN_EXTRA, changesWereApplied);
		intent.setAction(RESULT_ACTION);
		this.sendBroadcast(intent);
	}
	
	protected void broadcastException(Exception exception) {
		Intent intent = new Intent();
		intent.putExtra(EXCEPTION_SERIALIZABLE_EXTRA, exception);
		intent.setAction(EXCEPTION_ACTION);
		this.sendBroadcast(intent);
	}

}