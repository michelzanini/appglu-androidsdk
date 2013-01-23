package com.appglu.android.sync;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.StorageFile;
import com.appglu.SyncOperations;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

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
	
	public boolean checkIfDatabaseIsSynchronized() {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForAllTables();
		return this.areTablesSynchronized(localTableVersions);
	}
	
	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForTables(tables);
		return this.areTablesSynchronized(localTableVersions);
	}

	private boolean areTablesSynchronized(List<TableVersion> localTableVersions) {
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
		return this.syncDatabaseAndFiles(false);
	}
	
	public boolean syncDatabaseAndFiles() {
		return this.syncDatabaseAndFiles(true);
	}
	
	protected boolean syncDatabaseAndFiles(boolean syncFiles) {
		List<TableVersion> tableVersions = this.syncRepository.versionsForAllTables();
		
		if (this.areTablesSynchronized(tableVersions)) {
			if (tableVersions.isEmpty()) {
				this.logger.info("No tables to synchronize");
			} else {
				this.logger.info("Database is already synchronized");
			}
			return false;
		} else {
			this.fetchAndApplyChangesToTablesAndFiles(tableVersions, syncFiles);
			return true;
		}
	}
	
	public boolean syncTables(List<String> tables) {
		return this.syncTablesAndFiles(tables, false);
	}
	
	public boolean syncTablesAndFiles(List<String> tables) {
		return this.syncTablesAndFiles(tables, true);
	}
	
	protected boolean syncTablesAndFiles(List<String> tables, boolean syncFiles) {
		if (syncFiles && !tables.contains("appglu_storage_files")) {
			tables.add("appglu_storage_files");
		}
		
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		
		if (this.areTablesSynchronized(tableVersions)) {
			if (tableVersions.isEmpty()) {
				this.logger.info("No tables to synchronize");
			} else {
				this.logger.info("Tables '" + tableVersions + "' are already synchronized");
			}
			return false;
		} else {
			this.fetchAndApplyChangesToTablesAndFiles(tableVersions, syncFiles);
			return true;
		}
	}
	
	protected void fetchAndApplyChangesToTablesAndFiles(final List<TableVersion> tableVersions, final boolean syncFiles) {
		this.logger.info("Synchronization started");
		try {
			if (tableVersions.isEmpty()) {
				this.logger.info("No changes were applied because no local tables were found");
				return;
			}
			
			this.logger.info("Fetching and applying remote changes for tables " + tableVersions);
			
			this.syncRepository.applyChangesWithTransaction(new SyncRepositoryCallback() {
				public void syncData() {
					syncOperations.changesForTables(tableVersions, syncRepository);
					logger.info("Changes were applied with success");
				}
				public void syncFiles(List<StorageFile> files) {
					if (syncFiles) {
						downloadAndApplyChangesToFiles(files);
					}
				}
			});
			
		} catch (RuntimeException e) {
			this.logger.error("Synchronization failed with exception", e);
			throw e;
		} finally {
			this.logger.info("Synchronization finished");
		}
	}
	
	protected void downloadAndApplyChangesToFiles(List<StorageFile> files) {
		if (files == null || files.isEmpty()) {
			this.logger.info("No files were downloaded because database does not containg any files");
			return;
		}
		
		List<StorageFile> filesToBeDownloaded = new ArrayList<StorageFile>();
		
		for (StorageFile storageFile : files) {
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

	protected boolean verifyIfStorageFileNeedsToBeDownloaded(StorageFile storageFile) {
		File cachedFile = this.syncStorageService.getFileFromExternalStorage(storageFile);
		
		if (cachedFile == null) {
			return false;
		}
		
		if (this.syncStorageService.md5MatchesWithETag(cachedFile, storageFile.getETag())) {
			return false;
		}
		
		return true;
	}
	
	protected void downloadFiles(List<StorageFile> filesToBeDownloaded) {
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
	
	protected void removeFiles(List<String> filesToBeRemoved) {
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