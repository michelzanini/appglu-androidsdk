package com.appglu.android.sync;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.appglu.android.AppGlu;

public class AppGluSyncIntentService extends IntentService {
	
	public static final String TABLES_STRING_ARRAY_EXTRA = "AppGluSyncIntentService.TABLES_STRING_ARRAY_EXTRA";
	
	public static final String NOTIFICATION_PARCELABLE_EXTRA = "AppGluSyncIntentService.NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String PRE_EXECUTE_ACTION = "AppGluSyncIntentService.ON_PRE_EXECUTE_ACTION";
	
	public static final String NO_INTERNET_CONNECTION_ACTION = "AppGluSyncIntentService.ON_NO_INTERNET_CONNECTION_ACTION";
	
	public static final String RESULT_ACTION = "AppGluSyncIntentService.RESULT_ACTION";
	
	public static final String EXCEPTION_ACTION = "AppGluSyncIntentService.EXCEPTION_ACTION";
	
	public static final String FINISH_ACTION = "AppGluSyncIntentService.FINISH_ACTION";
	
	public static final String EXCEPTION_SERIALIZABLE_EXTRA = "AppGluSyncIntentService.EXCEPTION_SERIALIZABLE_EXTRA";
	
	public AppGluSyncIntentService() {
		super("AppGluSyncIntentService");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (intent.hasExtra(NOTIFICATION_PARCELABLE_EXTRA)) {
			Notification notification = (Notification) intent.getParcelableExtra(NOTIFICATION_PARCELABLE_EXTRA);
			this.sendNotification(notification);
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			if (!AppGlu.hasInternetConnection()) {
				this.broadcastAction(NO_INTERNET_CONNECTION_ACTION);
				return;
			}
			
			this.broadcastAction(PRE_EXECUTE_ACTION);
			
			ArrayList<String> tables = intent.getStringArrayListExtra(TABLES_STRING_ARRAY_EXTRA);
			
			if (tables == null) {
				AppGlu.syncApi().doSyncDatabase();
			} else {
				AppGlu.syncApi().doSyncTables(tables);
			}
			
			this.broadcastAction(RESULT_ACTION);
	
		} catch (Exception exception) {
			this.broadcastException(exception);
		} finally {
			this.cancelNotification();
		}
		
		this.broadcastAction(FINISH_ACTION);
	}
	
	private void sendNotification(Notification notification) {
		//removes auto cancel flag
		notification.flags &= ~Notification.FLAG_AUTO_CANCEL;
		//removes ongoing flag
		notification.flags &= ~Notification.FLAG_ONGOING_EVENT;
		//adds no clear flag
		notification.flags |= Notification.FLAG_NO_CLEAR;
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(SyncApi.NOTIFICATION_ID, notification);
	}

	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(SyncApi.NOTIFICATION_ID);
	}
	
	protected void broadcastAction(String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		this.sendBroadcast(intent);
	}
	
	protected void broadcastException(Exception exception) {
		Intent intent = new Intent();
		intent.putExtra(EXCEPTION_SERIALIZABLE_EXTRA, exception);
		intent.setAction(EXCEPTION_ACTION);
		this.sendBroadcast(intent);
	}

}