package com.appglu.android.sync;

import java.util.Arrays;
import java.util.List;

import android.app.Notification;

public class SyncIntentServiceRequest {
	
	protected enum SyncRequestOperation {
		DOWNLOAD_CHANGES,
		APPLY_CHANGES,
		DOWNLOAD_AND_APPLY_CHANGES,
		DISCARD_CHANGES;
	}
	
	private final SyncRequestOperation syncRequestOperation;
	
	private final boolean syncFiles;
	
	private final List<String> tablesToSync;
	
	private Notification executingSyncNotification;
	
	private Notification changesAppliedNotification;
	
	protected SyncIntentServiceRequest(SyncRequestOperation syncOperation) {
		this(syncOperation, false);
	}
	
	protected SyncIntentServiceRequest(SyncRequestOperation syncOperation, boolean syncFiles) {
		this(syncOperation, syncFiles, null);
	}
	
	protected SyncIntentServiceRequest(SyncRequestOperation syncOperation, boolean syncFiles, List<String> tablesToSync) {
		this.syncRequestOperation = syncOperation;
		this.syncFiles = syncFiles;
		this.tablesToSync = tablesToSync;
	}

	public static SyncIntentServiceRequest syncDatabase() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false);
	}
	
	public static SyncIntentServiceRequest syncDatabaseAndFiles() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true);
	}
	
	public static SyncIntentServiceRequest syncTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false, tables);
	}
	
	public static SyncIntentServiceRequest syncTablesAndFiles(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true, tables);
	}
	
	public static SyncIntentServiceRequest syncTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false, Arrays.asList(tables));
	}
	
	public static SyncIntentServiceRequest syncTablesAndFiles(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true, Arrays.asList(tables));
	}
	
	public static SyncIntentServiceRequest downloadChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false);
	}
	
	public static SyncIntentServiceRequest downloadChangesAndFiles() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true);
	}
	
	public static SyncIntentServiceRequest downloadChangesForTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false, tables);
	}
	
	public static SyncIntentServiceRequest downloadChangesAndFilesForTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true, tables);
	}
	
	public static SyncIntentServiceRequest downloadChangesForTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false, Arrays.asList(tables));
	}
	
	public static SyncIntentServiceRequest downloadChangesAndFilesForTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true, Arrays.asList(tables));
	}
	
	public static SyncIntentServiceRequest discardChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DISCARD_CHANGES);
	}

	public static SyncIntentServiceRequest applyChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.APPLY_CHANGES);
	}

	public void setExecutingSyncNotification(Notification executingSyncNotification) {
		this.executingSyncNotification = executingSyncNotification;
	}

	public void setChangesAppliedNotification(Notification changesAppliedNotification) {
		this.changesAppliedNotification = changesAppliedNotification;
	}
	
	protected SyncRequestOperation getSyncRequestOperation() {
		return syncRequestOperation;
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