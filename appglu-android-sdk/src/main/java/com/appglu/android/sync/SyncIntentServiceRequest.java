package com.appglu.android.sync;

import java.util.Arrays;
import java.util.List;

import android.app.Notification;

public class SyncIntentServiceRequest {
	
	private boolean syncFiles;
	
	private List<String> tablesToSync;
	
	private Notification executingSyncNotification;
	
	private Notification changesAppliedNotification;

	protected SyncIntentServiceRequest(boolean syncFiles) {
		this.syncFiles = syncFiles;
	}

	protected SyncIntentServiceRequest(boolean syncFiles, List<String> tablesToSync) {
		this.syncFiles = syncFiles;
		this.tablesToSync = tablesToSync;
	}
	
	public static SyncIntentServiceRequest syncDatabase() {
		return new SyncIntentServiceRequest(false);
	}
	
	public static SyncIntentServiceRequest syncDatabaseAndFiles() {
		return new SyncIntentServiceRequest(true);
	}
	
	public static SyncIntentServiceRequest syncTables(List<String> tables) {
		return new SyncIntentServiceRequest(false, tables);
	}
	
	public static SyncIntentServiceRequest syncTablesAndFiles(List<String> tables) {
		return new SyncIntentServiceRequest(true, tables);
	}
	
	public static SyncIntentServiceRequest syncTables(String... tables) {
		return new SyncIntentServiceRequest(false, Arrays.asList(tables));
	}
	
	public static SyncIntentServiceRequest syncTablesAndFiles(String... tables) {
		return new SyncIntentServiceRequest(true, Arrays.asList(tables));
	}

	public SyncIntentServiceRequest setExecutingSyncNotification(Notification executingSyncNotification) {
		this.executingSyncNotification = executingSyncNotification;
		return this;
	}

	public SyncIntentServiceRequest setChangesAppliedNotification(Notification changesAppliedNotification) {
		this.changesAppliedNotification = changesAppliedNotification;
		return this;
	}

	protected boolean getSyncFiles() {
		return syncFiles;
	}

	protected List<String> getTablesToSync() {
		return tablesToSync;
	}

	protected Notification getExecutingSyncNotification() {
		return executingSyncNotification;
	}

	protected Notification getChangesAppliedNotification() {
		return changesAppliedNotification;
	}
	
}