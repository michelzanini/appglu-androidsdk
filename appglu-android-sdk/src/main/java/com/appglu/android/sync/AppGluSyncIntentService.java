package com.appglu.android.sync;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;

import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class AppGluSyncIntentService extends IntentService {
	
	public static final String TABLES_STRING_ARRAY_EXTRA = "AppGluSyncIntentService.TABLES_STRING_ARRAY_EXTRA";
	
	public static final String PRE_EXECUTE_ACTION = "AppGluSyncIntentService.ON_PRE_EXECUTE_ACTION";
	
	public static final String NO_INTERNET_CONNECTION_ACTION = "AppGluSyncIntentService.ON_NO_INTERNET_CONNECTION_ACTION";
	
	public static final String RESULT_ACTION = "AppGluSyncIntentService.RESULT_ACTION";
	
	public static final String EXCEPTION_ACTION = "AppGluSyncIntentService.EXCEPTION_ACTION";
	
	public static final String FINISH_ACTION = "AppGluSyncIntentService.FINISH_ACTION";
	
	public static final String EXCEPTION_SERIALIZABLE_EXTRA = "AppGluSyncIntentService.EXCEPTION_SERIALIZABLE_EXTRA";
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private static boolean isRunning = false;

	public AppGluSyncIntentService() {
		super("AppGluSyncIntentService");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		if (!isRunning) {
			isRunning = true;
			this.logger.info("AppGluSyncIntentService has being started");
			super.onStart(intent, startId);
		} else {
			this.logger.info("AppGluSyncIntentService was not started because it is already running");
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
			isRunning = false;
		}
		
		this.broadcastAction(FINISH_ACTION);
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
	
	public static boolean isRunning() {
		return isRunning;
	}

}