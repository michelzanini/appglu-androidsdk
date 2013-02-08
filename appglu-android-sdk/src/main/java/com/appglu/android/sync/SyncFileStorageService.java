package com.appglu.android.sync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.os.StatFs;

import com.appglu.InputStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.impl.util.HashUtils;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.StringUtils;

public class SyncFileStorageService {
	
	private static final String SYNC_CHANGES_TEMPORATY_FILE = "appglu_sync_changes.tmp";
	
	private static final String SYNC_CHANGES_FILE = "appglu_sync_changes.json";
	
	private Context context;
	
	private StorageOperations storageOperations;
	
	public SyncFileStorageService(Context context, StorageOperations storageOperations) {
		this.context = context;
		this.storageOperations = storageOperations;
	}
	
	public void downloadFileToExternalStorage(final StorageFile storageFile) {
		this.storageOperations.streamStorageFile(storageFile, new InputStreamCallback() {
			
			public void doWithInputStream(InputStream inputStream) throws IOException {
				File destinationFile = getFileFromExternalStorage(storageFile);
				
				FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
				BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
				
				IOUtils.copy(inputStream, outputStream);
			}
			
		});
	}
	
	protected File getExternalAppGluStorageCacheDir() {
		File externalCacheDirectory = this.context.getExternalFilesDir("appglu_storage_cache");
		if (externalCacheDirectory == null) {
			throw new SyncFileStorageException("External storage is not accessible");
		}
		return externalCacheDirectory;
	}
	
	protected File getExternalAppGluStorageFilesDir() {
		File externalStorageDirectory = this.context.getExternalFilesDir("appglu_storage_files");
		if (externalStorageDirectory == null) {
			throw new SyncFileStorageException("External storage is not accessible");
		}
		return externalStorageDirectory;
	}

	public File getFileFromExternalStorage(StorageFile storageFile) {
		String fileName = this.fileNameFromStorageFile(storageFile);
		return this.getFileFromExternalStorage(fileName);
	}
	
	protected File getFileFromExternalStorage(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return null;
		}
		File externalStorageDirectory = this.getExternalAppGluStorageFilesDir();
		return new File(externalStorageDirectory, fileName);
	}
	
	public String fileNameFromStorageFile(StorageFile storageFile) {
		if (storageFile == null) {
			return null;
		}
		return storageFile.getETag();
	}
	
	public StorageFile storageFileFromFileName(String fileName) {
		StorageFile file = new StorageFile();
		file.setETag(fileName);
		return file;
	}
	
	public String[] allFilesOnStorage() {
		File externalStorageDirectory = this.getExternalAppGluStorageFilesDir();
		return externalStorageDirectory.list();
	}
	
	public void removeFileWithName(String fileName) {
		File file = this.getFileFromExternalStorage(fileName);
		
		if (file != null && file.exists()) {
			file.delete();	
		}
	}
	
	public boolean md5MatchesWithETag(File cachedFile, String eTag) {
		if (cachedFile == null || !cachedFile.exists()) {
			return false;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(cachedFile);
			byte[] md5Hash = HashUtils.computeMd5Hash(new BufferedInputStream(fileInputStream));
			return HashUtils.md5MatchesWithETag(md5Hash, eTag);
		} catch (IOException e) {
			throw new SyncFileStorageException(e);
		}
	}
	
	public long calculateTotalDownloadSizeInBytes(List<StorageFile> filesToBeDownloaded) {
		long totalSizeInBytes = 0;
		for (StorageFile storageFile : filesToBeDownloaded) {
			totalSizeInBytes += storageFile.getSize();
		}
		return totalSizeInBytes;
	}
	
	public long calculateExternalStorageAvailableSpaceInBytes() {
        File externalStorageDirectory = this.getExternalAppGluStorageFilesDir();
        
		StatFs stat = new StatFs(externalStorageDirectory.getPath());
		long availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
	public void checkIfThereIsEnoughSpaceAvailableOnStorage(long totalDownloadSizeInBytes) {
		long availableSpaceInBytes = this.calculateExternalStorageAvailableSpaceInBytes();
				
		if (totalDownloadSizeInBytes > availableSpaceInBytes) {
			throw new SyncFileStorageException("Not enough space available on external storage");
		}
	}
	
	protected File temporaryFile() {
		File externalCacheDirectory = this.getExternalAppGluStorageCacheDir();
		return new File(externalCacheDirectory, SYNC_CHANGES_TEMPORATY_FILE);
	}
	
	protected File changesFile() {
		File externalCacheDirectory = this.getExternalAppGluStorageCacheDir();
		return new File(externalCacheDirectory, SYNC_CHANGES_FILE);
	}
	
	public void writeTemporaryChanges(InputStream inputStream) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(this.temporaryFile());
		BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
		
		IOUtils.copy(inputStream, outputStream);
	}

	public void promoteTemporaryChanges() {
		File temporaryFile = this.temporaryFile();
		if (temporaryFile.exists()) {
			temporaryFile.renameTo(this.changesFile());
		}
	}
	
	public boolean removeTemporaryChanges() {
		File temporaryFile = this.temporaryFile();
		if (temporaryFile.exists()) {
			return temporaryFile.delete();
		}
		return false;
	}
	
	public boolean hasTemporaryChanges() {
		File temporaryFile = this.temporaryFile();
		return temporaryFile.exists();
	}
	
	public InputStream getTemporaryChanges() {
		File temporaryFile = this.temporaryFile();
		if (!temporaryFile.exists()) {
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(temporaryFile);
			return new BufferedInputStream(fileInputStream);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public boolean removeDownloadedChanges() {
		File syncChangesFile = this.changesFile();
		if (syncChangesFile.exists()) {
			return syncChangesFile.delete();
		}
		return false;
	}
	
	public boolean hasDownloadedChanges() {
		File syncChangesFile = this.changesFile();
		return syncChangesFile.exists();
	}
	
	public InputStream getDownloadedChanges() {
		File syncChangesFile = this.changesFile();
		if (!syncChangesFile.exists()) {
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(syncChangesFile);
			return new BufferedInputStream(fileInputStream);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
}