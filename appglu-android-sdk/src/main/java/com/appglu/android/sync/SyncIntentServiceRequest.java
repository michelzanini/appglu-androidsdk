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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Notification;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>Describes what the {@link SyncIntentService} is going to do during the Sync operation.<br> 
 * <p>You can choose between:<br>
 * <ul>
 *    <li>Downloading the changes only or downloading and applying them immediately</li>
 *    <li>Downloading files as well or only the data</li>
 *    <li>Choose if all your tables are going to be synchronized or only the ones you want to</li>
 *    <li>You can also choose to apply or discard previously downloaded changes</li>
 * </ul>
 * 
 * To start the Sync Service using a {@code SyncIntentServiceRequest}, see the following example:
 * 
 * <p><code>
 * SyncIntentServiceRequest request = SyncIntentServiceRequest.syncDatabaseAndFiles();<br>
 * AppGlu.syncApi().startSyncIntentService(request);
 * </code>
 * 
 * <p>The example above is going to start {@link SyncIntentService} and request it to download and apply changes to all of your tables as well as download all files that are new or updated.
 * 
 * @see SyncApi#startSyncIntentService(SyncIntentServiceRequest)
 * @since 1.0.0
 */
public class SyncIntentServiceRequest implements Parcelable {
	
	/* package */ enum SyncRequestOperation {
		DOWNLOAD_CHANGES,
		DOWNLOAD_AND_APPLY_CHANGES,
		APPLY_CHANGES,
		DISCARD_CHANGES;
	}
	
	private final SyncRequestOperation syncRequestOperation;
	
	private final boolean syncFiles;
	
	private final List<String> tablesToSync;
	
	private Notification syncServiceRunningNotification;
	
	private Notification syncServiceCompletedNotification;
	
	/* package */ SyncIntentServiceRequest(Parcel in) {
		this.syncRequestOperation = (SyncRequestOperation) in.readSerializable();
		this.syncFiles = in.readInt() == 1 ? true : false;
		
		boolean hasTablesToSync = in.readInt() == 1 ? true : false;
		
		List<String> tablesToSync = new ArrayList<String>();
		in.readStringList(tablesToSync);
		
		this.tablesToSync = hasTablesToSync ? tablesToSync : null;
		
		this.syncServiceRunningNotification = in.readParcelable(SyncIntentServiceRequest.class.getClassLoader());
		this.syncServiceCompletedNotification = in.readParcelable(SyncIntentServiceRequest.class.getClassLoader());
	}
	
	/* package */ SyncIntentServiceRequest(SyncRequestOperation syncOperation) {
		this(syncOperation, false);
	}
	
	/* package */ SyncIntentServiceRequest(SyncRequestOperation syncOperation, boolean syncFiles) {
		this(syncOperation, syncFiles, null);
	}
	
	/* package */ SyncIntentServiceRequest(SyncRequestOperation syncOperation, boolean syncFiles, List<String> tablesToSync) {
		this.syncRequestOperation = syncOperation;
		this.syncFiles = syncFiles;
		this.tablesToSync = tablesToSync;
	}
	
	/**
	 * Requests changes from all of your SQLite tables to be downloaded only, but not applied.<br>
	 * If you also want files to be downlaoded use {@link #downloadChangesAndFiles()}.
	 */
	public static SyncIntentServiceRequest downloadChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false);
	}
	
	/**
	 * Requests changes from all of your SQLite tables to be downloaded only, but not applied.<br>
	 * Will also download all files that are new or updated.
	 */
	public static SyncIntentServiceRequest downloadChangesAndFiles() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true);
	}
	
	/**
	 * Requests changes from some of your SQLite tables to be downloaded only, but not applied.<br>
	 * If you also want files to be downlaoded use {@link #downloadChangesAndFilesForTables(List)}.
	 * @param tables the names of the tables you want to download changes from
	 */
	public static SyncIntentServiceRequest downloadChangesForTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false, tables);
	}
	
	/**
	 * Requests changes from some of your SQLite tables to be downloaded only, but not applied.<br>
	 * Will also download all files that are new or updated.
	 * @param tables the names of the tables you want to download changes from
	 */
	public static SyncIntentServiceRequest downloadChangesAndFilesForTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true, tables);
	}
	
	/**
	 * Requests changes from some of your SQLite tables to be downloaded only, but not applied.<br>
	 * If you also want files to be downlaoded use {@link #downloadChangesAndFilesForTables(String...)}.
	 * @param tables the names of the tables you want to download changes from
	 */
	public static SyncIntentServiceRequest downloadChangesForTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, false, Arrays.asList(tables));
	}
	
	/**
	 * Requests changes from some of your SQLite tables to be downloaded only, but not applied.<br>
	 * Will also download all files that are new or updated.
	 * @param tables the names of the tables you want to download changes from
	 */
	public static SyncIntentServiceRequest downloadChangesAndFilesForTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_CHANGES, true, Arrays.asList(tables));
	}

	/**
	 * Download and apply changes from all of your SQLite tables.<br>
	 * If you also want files to be synchronized use {@link #syncDatabaseAndFiles()}.
	 */
	public static SyncIntentServiceRequest syncDatabase() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false);
	}
	
	/**
	 * Download and apply changes from all of your SQLite tables.<br>
	 * Will also download and apply all files that are new or updated.
	 */
	public static SyncIntentServiceRequest syncDatabaseAndFiles() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true);
	}
	
	/**
	 * Download and apply changes from some of your SQLite tables.<br>
	 * If you also want files to be synchronized use {@link #syncTablesAndFiles(List)}.
	 * @param tables the names of the tables you want to synchronize
	 */
	public static SyncIntentServiceRequest syncTables(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false, tables);
	}
	
	/**
	 * Download and apply changes from some of your SQLite tables.<br>
	 * Will also download and apply all files that are new or updated.
	 * @param tables the names of the tables you want to synchronize
	 */
	public static SyncIntentServiceRequest syncTablesAndFiles(List<String> tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true, tables);
	}
	
	/**
	 * Download and apply changes from some of your SQLite tables.<br>
	 * If you also want files to be synchronized use {@link #syncTablesAndFiles(String...)}.
	 * @param tables the names of the tables you want to synchronize
	 */
	public static SyncIntentServiceRequest syncTables(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, false, Arrays.asList(tables));
	}
	
	/**
	 * Download and apply changes from some of your SQLite tables.<br>
	 * Will also download and apply all files that are new or updated.
	 * @param tables the names of the tables you want to synchronize
	 */
	public static SyncIntentServiceRequest syncTablesAndFiles(String... tables) {
		return new SyncIntentServiceRequest(SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES, true, Arrays.asList(tables));
	}
	
	/**
	 * Discard changes that were previously downloaded.
	 */
	public static SyncIntentServiceRequest discardChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.DISCARD_CHANGES);
	}

	/**
	 * Apply changes that were previously downloaded.
	 */
	public static SyncIntentServiceRequest applyChanges() {
		return new SyncIntentServiceRequest(SyncRequestOperation.APPLY_CHANGES);
	}

	/**
	 * Provide a <code>android.app.Notification</code> to be displayed when {@link SyncIntentService} starts running.
	 */
	public void setSyncServiceRunningNotification(Notification syncServiceRunningNotification) {
		this.syncServiceRunningNotification = syncServiceRunningNotification;
	}

	/**
	 * Provide a <code>android.app.Notification</code> to be displayed when {@link SyncIntentService} finishes running.
	 */
	public void setSyncServiceCompletedNotification(Notification syncServiceCompletedNotification) {
		this.syncServiceCompletedNotification = syncServiceCompletedNotification;
	}
	
	/* package */ SyncRequestOperation getSyncRequestOperation() {
		return syncRequestOperation;
	}

	/* package */ boolean getSyncFiles() {
		return syncFiles;
	}

	/* package */ List<String> getTablesToSync() {
		return tablesToSync;
	}

	/* package */ Notification getSyncServiceRunningNotification() {
		return syncServiceRunningNotification;
	}

	/* package */ Notification getSyncServiceCompletedNotification() {
		return syncServiceCompletedNotification;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeSerializable(this.syncRequestOperation);
		parcel.writeInt(this.syncFiles ? 1 : 0);
		parcel.writeInt(this.tablesToSync != null ? 1 : 0);
		parcel.writeStringList(this.tablesToSync);
		parcel.writeParcelable(this.syncServiceRunningNotification, flags);
		parcel.writeParcelable(this.syncServiceCompletedNotification, flags);
	}
	
	public static final Parcelable.Creator<SyncIntentServiceRequest> CREATOR = new Parcelable.Creator<SyncIntentServiceRequest>() {
		public SyncIntentServiceRequest createFromParcel(Parcel in) {
		    return new SyncIntentServiceRequest(in);
		}
		
		public SyncIntentServiceRequest[] newArray(int size) {
		    return new SyncIntentServiceRequest[size];
		}
	};
	
}
