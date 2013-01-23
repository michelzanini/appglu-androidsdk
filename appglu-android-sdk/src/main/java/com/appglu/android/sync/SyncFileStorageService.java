package com.appglu.android.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.os.StatFs;

import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;
import com.appglu.impl.util.HashUtils;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.StringUtils;

public class SyncFileStorageService {
	
	private Context context;
	
	private StorageOperations storageOperations;
	
	public SyncFileStorageService(Context context, StorageOperations storageOperations) {
		this.context = context;
		this.storageOperations = storageOperations;
	}
	
	public void downloadFileToExternalStorage(final StorageFile storageFile) {
		this.storageOperations.streamStorageFile(storageFile, new StorageStreamCallback() {
			
			public void doWithInputStream(InputStream fileStream) throws IOException {
				File destinationFile = getFileFromExternalStorage(storageFile);
				
				FileOutputStream outputStream = new FileOutputStream(destinationFile);
				IOUtils.copy(fileStream, outputStream);
			}
			
		});
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
			byte[] md5Hash = HashUtils.computeMd5Hash(new FileInputStream(cachedFile));
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

}