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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.appglu.InputStreamCallback;
import com.appglu.StorageFile;
import com.appglu.SyncOperations;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.util.IOUtils;

public class SyncService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private SyncOperations syncOperations;
	
	private SyncRepository syncRepository;
	
	private SyncFileStorageService syncStorageService;
	
	public SyncService(SyncOperations syncOperations, SyncRepository syncRepository, SyncFileStorageService syncStorageService) {
		this.syncOperations = syncOperations;
		this.syncRepository = syncRepository;
		this.syncStorageService = syncStorageService;
	}
	
	public File getFileFromFileStorage(StorageFile file) {
		StorageFile storageFile = this.syncRepository.getStorageFileByIdOrUrl(file.getId(), file.getUrl());
		
		if (storageFile == null) {
			return null;
		}
		
		File cachedFile = this.syncStorageService.getFileFromExternalStorage(storageFile);
		
		if (!cachedFile.exists()) {
			return null;
		}
		
		return cachedFile;
	}
	
	public InputStream readInputStreamFromFileStorage(StorageFile storageFile) {
		File file = this.getFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			return new BufferedInputStream(fileInputStream);
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
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile) {
		File file = this.getFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromFile(file);
	}
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int inSampleSize) {
		File file = this.getFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromFile(file, inSampleSize);
	}
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int requestedWidth, int requestedHeight) {
		File file = this.getFileFromFileStorage(storageFile);
		
		if (file == null) {
			return null;
		}
		
		return AppGluUtils.decodeSampledBitmapFromFile(file, requestedWidth, requestedHeight);
	}
	
	public boolean checkIfDatabaseIsSynchronized() {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForAllTables();
		return this.areTablesSynchronized(localTableVersions);
	}
	
	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForTables(tables);
		return this.areTablesSynchronized(localTableVersions);
	}

	protected boolean areTablesSynchronized(List<TableVersion> localTableVersions) {
		if (localTableVersions.isEmpty()) {
			return true;
		}
		
		Map<String, TableVersion> localTables = new HashMap<String, TableVersion>();
		
		for (TableVersion table : localTableVersions) {
			localTables.put(table.getTableName(), table);
		}
		
		List<String> localTableNames = new ArrayList<String>(localTables.keySet());
		List<TableVersion> remoteTableVersions = this.syncOperations.versionsForTables(localTableNames);
		
		for (TableVersion remoteTableVersion : remoteTableVersions) {
			TableVersion localTableVersion = localTables.get(remoteTableVersion.getTableName());
			
			if (localTableVersion == null) {
				continue;
			}
			
			if (remoteTableVersion.getVersion() > localTableVersion.getVersion()) {
				return false;
			}
		}
		
		return true;
	}

	public boolean syncDatabase() {
		boolean hasChanges = this.downloadChanges();
		if (hasChanges) {
			this.applyChanges();
		}
		return hasChanges;
	}
	
	public boolean syncDatabaseAndFiles() {
		boolean hasChanges = this.downloadChangesAndFiles();
		if (hasChanges) {
			this.applyChanges();
		}
		return hasChanges;
	}
	
	public boolean syncTables(List<String> tables) {
		boolean hasChanges = this.downloadChangesForTables(tables);
		if (hasChanges) {
			this.applyChanges();
		}
		return hasChanges;
	}
	
	public boolean syncTablesAndFiles(List<String> tables) {
		boolean hasChanges = this.downloadChangesAndFilesForTables(tables);
		if (hasChanges) {
			this.applyChanges();
		}
		return hasChanges;
	}
	
	public boolean downloadChanges() {
		return this.downloadChangesAndFiles(false);
	}
	
	public boolean downloadChangesAndFiles() {
		return this.downloadChangesAndFiles(true);
	}
	
	public boolean downloadChangesForTables(List<String> tables) {
		return this.downloadChangesAndFilesForTables(tables, false);
	}
	
	public boolean downloadChangesAndFilesForTables(List<String> tables) {
		return this.downloadChangesAndFilesForTables(tables, true);
	}
	
	private boolean downloadChangesAndFiles(boolean syncFiles) {
		List<TableVersion> tableVersions = this.syncRepository.versionsForAllTables();
		
		if (!syncFiles) {
			this.removeStorageFilesTable(tableVersions);
		}
		
		if (this.areTablesSynchronized(tableVersions)) {
			if (tableVersions.isEmpty()) {
				this.logger.info("No changes to download");
			} else {
				this.logger.info("Database is already synchronized");
			}
			return false;
		} else {
			this.downloadChangesAndSyncFiles(tableVersions, syncFiles);
			return true;
		}
	}

	private void removeStorageFilesTable(List<TableVersion> tableVersions) {
		for (TableVersion tableVersion : tableVersions) {
			if (SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE.equals(tableVersion.getTableName())) {
				tableVersions.remove(tableVersion);
				break;
			}
		}
	}
	
	private boolean downloadChangesAndFilesForTables(List<String> tables, boolean syncFiles) {
		if (syncFiles) {
			if (!tables.contains(SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE)) {
				tables.add(SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE);
			}
		} else {
			tables.remove(SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE);
		}
		
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		
		if (this.areTablesSynchronized(tableVersions)) {
			if (tableVersions.isEmpty()) {
				this.logger.info("No changes to download");
			} else {
				this.logger.info("Tables '" + tableVersions + "' are already synchronized");
			}
			return false;
		} else {
			this.downloadChangesAndSyncFiles(tableVersions, syncFiles);
			return true;
		}
	}
	
	public boolean hasDownloadedChanges() {
		return this.syncStorageService.hasDownloadedChanges();
	}
	
	public boolean discardChanges() {
		if (!this.syncStorageService.hasDownloadedChanges()) {
			this.logger.info("No changes to discard");
			return false;
		}
		
		this.logger.info("Discarding changes");
		
		this.syncStorageService.removeTemporaryChanges();
		this.syncStorageService.removeDownloadedChanges();
		
		this.removeFilesThatAreNotBeingUsed();
		
		return true;
	}
	
	public boolean applyChanges() {
		if (!this.syncStorageService.hasDownloadedChanges()) {
			this.logger.info("No changes to apply");
			return false;
		}
		
		this.syncData();
		
		try {
			this.removeFilesThatAreNotBeingUsed();
		} catch (RuntimeException e) {
			this.logger.warn("Error while removing unused files", e);
		}
		
		return true;
	}
	
	private void downloadChangesAndSyncFiles(List<TableVersion> tableVersions, boolean syncFiles) {
		this.logger.info("Download started");
		
		try {
			this.syncStorageService.removeDownloadedChanges();
			
			this.logger.info("Downloading remote changes for tables " + tableVersions);
			
			this.syncOperations.downloadChangesForTables(tableVersions, new InputStreamCallback() {
				public void doWithInputStream(InputStream inputStream) throws IOException {
					syncStorageService.writeTemporaryChanges(inputStream);
				}
			});
			
			this.logger.info("Remote changes downloaded");
			
			if (syncFiles) {
				this.syncFiles();
			}
			
			this.syncStorageService.promoteTemporaryChanges();
			
			this.logger.info("Changes were downloaded with success");
			
		} catch (RuntimeException e) {
			this.logger.error("Download failed with exception", e);
			throw e;
		} finally {
			this.logger.info("Download finished");
		}
	}
	
	private void syncData() {
		this.logger.info("Synchronization started");
		try {
			
			this.syncRepository.applyChangesWithTransaction(new SyncTransactionCallback() {
				public void doInTransaction() {
					applyChangesWithTransaction();
				}
			});
			
			logger.info("Changes were applied with success");
			
		} catch (RuntimeException e) {
			this.logger.error("Synchronization failed with exception", e);
			throw e;
		} finally {
			this.logger.info("Synchronization finished");
		}
	}
	
	private void applyChangesWithTransaction() {
		try {
			InputStream inputStream = syncStorageService.getDownloadedChanges();
			syncOperations.parseTableChanges(inputStream, syncRepository);
		} catch (IOException e) {
			throw new SyncFileStorageException("Error while parsing storage changes from downloaded file");
		}
		
		boolean removed = syncStorageService.removeDownloadedChanges();
		if (!removed) {
			throw new SyncFileStorageException("Error while removing downloaded changes file");
		}
	}
	
	private void syncFiles() {
		if (!this.syncStorageService.hasTemporaryChanges()) {
			throw new SyncFileStorageException("Error while saving downloaded changes to storage");
		}
		
		StorageFileTableChangesCallback fileChangesCallback = new StorageFileTableChangesCallback();
		
		try {
			InputStream inputStream = this.syncStorageService.getTemporaryChanges();
			this.syncOperations.parseTableChanges(inputStream, fileChangesCallback);
		} catch (IOException e) {
			throw new SyncFileStorageException("Error while parsing storage files from downloaded table changes");
		}
		
		List<StorageFile> parsedFiles = fileChangesCallback.getParsedFiles();
		
		if (parsedFiles.isEmpty()) {
			this.logger.info("There are no new files to downloaded");
			return;
		}
		
		List<StorageFile> filesToBeDownloaded = new ArrayList<StorageFile>();
		
		for (StorageFile storageFile : parsedFiles) {
			boolean needsToBeDownloaded = this.verifyIfStorageFileNeedsToBeDownloaded(storageFile);

			if (needsToBeDownloaded && !filesToBeDownloaded.contains(storageFile)) {
				filesToBeDownloaded.add(storageFile);
			}
		}
		
		if (filesToBeDownloaded.isEmpty()) {
			this.logger.info("No files were downloaded because they are already in cache");
		} else {
			this.downloadFiles(filesToBeDownloaded);
		}
	}
	
	private boolean verifyIfStorageFileNeedsToBeDownloaded(StorageFile storageFile) {
		File cachedFile = this.syncStorageService.getFileFromExternalStorage(storageFile);
		
		if (cachedFile == null) {
			return false;
		}
		
		if (this.syncStorageService.md5MatchesWithETag(cachedFile, storageFile.getETag())) {
			return false;
		}
		
		return true;
	}
	
	private void downloadFiles(List<StorageFile> filesToBeDownloaded) {
		long totalDownloadSizeInBytes = this.syncStorageService.calculateTotalDownloadSizeInBytes(filesToBeDownloaded);
		
		this.syncStorageService.checkIfThereIsEnoughSpaceAvailableOnStorage(totalDownloadSizeInBytes);
		
		if (this.logger.isInfoEnabled()) {
			this.logger.info(filesToBeDownloaded.size() + " file(s) will be downloaded totalizing " + totalDownloadSizeInBytes + " bytes of download");
		}
		
		for (StorageFile storageFile : filesToBeDownloaded) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Dowloading file '" + storageFile.getName() + "' of " + storageFile.getSize() + " bytes of size");
			}
			this.syncStorageService.downloadFileToExternalStorage(storageFile);
		}
		
		this.logger.info("All files were downloaded with success");
	}
	
	private void removeFilesThatAreNotBeingUsed() {
		List<StorageFile> files = this.syncRepository.getAllFiles();
		
		List<String> filesToBeRemoved = new ArrayList<String>();
		
		for (String fileName : this.syncStorageService.allFilesOnStorage()) {
			StorageFile file = this.syncStorageService.storageFileFromFileName(fileName);
			
			if (!files.contains(file)) {
				filesToBeRemoved.add(fileName);
			}
		}
		
		if (filesToBeRemoved.isEmpty()) {
			this.logger.info("No files were removed");
		} else {
			this.removeFiles(filesToBeRemoved);
		}
	}
	
	private void removeFiles(List<String> filesToBeRemoved) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info(filesToBeRemoved.size() + " file(s) will be removed");
		}
		
		for (String fileName : filesToBeRemoved) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Removing file '" + fileName + "'");
			}
			this.syncStorageService.removeFileWithName(fileName);
		}

		this.logger.info("Files were removed with success");
	}
	
}
