package com.appglu.android.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.StatFs;

import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;
import com.appglu.SyncOperations;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.Md5Utils;
import com.appglu.impl.util.StringUtils;

public class SyncService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private StorageOperations storageOperations;
	
	private SyncOperations syncOperations;
	
	private SyncRepository syncRepository;
	
	public SyncService(SyncOperations syncOperations, StorageOperations storageOperations, SyncRepository syncRepository) {
		this.storageOperations = storageOperations;
		this.syncOperations = syncOperations;
		this.syncRepository = syncRepository;
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

			if (needsToBeDownloaded) {
				if (!this.containsETag(filesToBeDownloaded, storageFile.getETag())) {
					filesToBeDownloaded.add(storageFile);
				}
			}
		}
		
		if (filesToBeDownloaded.isEmpty()) {
			this.logger.info("No files were downloaded because they are already in cache");
		} else {
			this.downloadFiles(filesToBeDownloaded);
		}
		
		List<String> filesToBeRemoved = new ArrayList<String>();
		
		for (String eTagFileName : this.allFilesOnStorage()) {
			if (!this.containsETag(files, eTagFileName)) {
				filesToBeRemoved.add(eTagFileName);
			}
		}
		
		if (filesToBeRemoved.isEmpty()) {
			this.logger.info("No files were removed");
		} else {
			this.removeFiles(filesToBeRemoved);
		}
	}

	protected boolean verifyIfStorageFileNeedsToBeDownloaded(StorageFile storageFile) {
		if (StringUtils.isEmpty(storageFile.getETag())) {
			return false;
		}
		
		File externalStorageDirectory = AppGlu.getExternalAppGluStorageFilesDir();
		File cachedFile = new File(externalStorageDirectory, storageFile.getETag());
		
		if (cachedFile.exists()) {
			if (this.md5MatchesWithETag(cachedFile, storageFile.getETag())) {
				return false;
			}
		}
		
		return true;
	}
	
	protected void downloadFiles(List<StorageFile> filesToBeDownloaded) {
		long totalDownloadSizeInBytes = this.calculateTotalDownloadSizeInBytes(filesToBeDownloaded);
		long availableSpaceInBytes = this.calculateExternalStorageAvailableSpaceInBytes();
				
		if (totalDownloadSizeInBytes > availableSpaceInBytes) {
			throw new SyncFileStorageException("Not enough space available on external storage");
		}
		
		if (this.logger.isInfoEnabled()) {
			this.logger.info(filesToBeDownloaded.size() + " file(s) will be downloaded totalizing " + totalDownloadSizeInBytes + " bytes of download");
		}
		
		for (StorageFile storageFile : filesToBeDownloaded) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Dowloading file '" + storageFile.getName() + "' of " + storageFile.getSize() + " bytes of size");
			}
			this.downloadStorageFile(storageFile);
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
			this.removeFileWithName(fileName);
		}

		this.logger.info("Files were removed with success");
	}

	protected void downloadStorageFile(final StorageFile storageFile) {
		this.storageOperations.streamStorageFile(storageFile, new StorageStreamCallback() {
			
			public void doWithInputStream(InputStream fileStream) throws IOException {
				File externalStorageDirectory = AppGlu.getExternalAppGluStorageFilesDir();
				File destinationFile = new File(externalStorageDirectory, storageFile.getETag());
				
				FileOutputStream outputStream = new FileOutputStream(destinationFile);
				
				IOUtils.copy(fileStream, outputStream);
			}
			
		});
	}
	
	protected boolean containsETag(List<StorageFile> files, String eTag) {
		if (StringUtils.isEmpty(eTag)) {
			return false;
		}
		for (StorageFile storageFile : files) {
			if (eTag.equals(storageFile.getETag())) {
				return true;
			}
		}
		return false;
	}
	
	protected String[] allFilesOnStorage() {
		File externalStorageDirectory = AppGlu.getExternalAppGluStorageFilesDir();
		return externalStorageDirectory.list();
	}
	
	protected void removeFileWithName(String fileName) {
		File externalStorageDirectory = AppGlu.getExternalAppGluStorageFilesDir();
		File file = new File(externalStorageDirectory, fileName);
		
		file.delete();
	}
	
	protected boolean md5MatchesWithETag(File cachedFile, String eTag) {
		try {
			byte[] md5Hash = Md5Utils.computeMd5Hash(new FileInputStream(cachedFile));
			return Md5Utils.md5MatchesWithETag(md5Hash, eTag);
		} catch (IOException e) {
			throw new SyncFileStorageException(e);
		}
	}
	
	protected long calculateTotalDownloadSizeInBytes(List<StorageFile> filesToBeDownloaded) {
		long totalSizeInBytes = 0;
		for (StorageFile storageFile : filesToBeDownloaded) {
			totalSizeInBytes += storageFile.getSize();
		}
		return totalSizeInBytes;
	}
	
	protected long calculateExternalStorageAvailableSpaceInBytes() {
        File externalStorageDirectory = AppGlu.getExternalAppGluStorageFilesDir();
        
		StatFs stat = new StatFs(externalStorageDirectory.getPath());
		long availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
}